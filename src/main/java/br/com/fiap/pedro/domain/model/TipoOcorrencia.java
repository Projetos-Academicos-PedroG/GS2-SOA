package br.com.fiap.pedro.domain.model;

public enum TipoOcorrencia {
    FURTO(1.0),
    ROUBO(1.4),
    TENTATIVA(0.6);

    private final double peso;

    TipoOcorrencia(double peso) {
        this.peso = peso;
    }

    public double getPeso() {
        return peso;
    }
}
