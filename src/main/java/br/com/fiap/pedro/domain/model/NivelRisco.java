package br.com.fiap.pedro.domain.model;

public enum NivelRisco {
    BAIXO,
    MEDIO,
    ALTO,
    CRITICO;

    public static NivelRisco deScore(double score) {
        if (score >= 0.80) return CRITICO;
        if (score >= 0.55) return ALTO;
        if (score >= 0.30) return MEDIO;
        return BAIXO;
    }
}
