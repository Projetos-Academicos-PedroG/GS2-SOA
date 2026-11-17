package br.com.fiap.pedro.domain.service;

public final class Geo {
    private static final double RAIO_TERRA_METROS = 6_371_000;

    private Geo() {
    }

    public static double distanciaMetros(double lat1, double lng1, double lat2, double lng2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        return 2 * RAIO_TERRA_METROS * Math.asin(Math.sqrt(a));
    }

    public static double deltaLatitude(double metros) {
        return Math.toDegrees(metros / RAIO_TERRA_METROS);
    }

    public static double deltaLongitude(double metros, double latitude) {
        return Math.toDegrees(metros / (RAIO_TERRA_METROS * Math.cos(Math.toRadians(latitude))));
    }
}
