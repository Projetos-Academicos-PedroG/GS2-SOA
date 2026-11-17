package br.com.fiap.pedro.domain.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class GeoTest {
    @Test
    void distanciaEntreMaspEPracaDaSeDeveSerAproximadamente2_5km() {
        double d = Geo.distanciaMetros(-23.5614, -46.6559, -23.5503, -46.6340);
        assertThat(d).isCloseTo(2560, within(250.0));
    }

    @Test
    void distanciaDeUmPontoParaEleMesmoEhZero() {
        assertThat(Geo.distanciaMetros(-23.5, -46.6, -23.5, -46.6)).isZero();
    }

    @Test
    void deltaLatitudeDe111kmEhAproximadamenteUmGrau() {
        assertThat(Geo.deltaLatitude(111_195)).isCloseTo(1.0, within(0.01));
    }
}
