package br.com.fiap.pedro.domain.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ocorrencia")
public class Ocorrencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoOcorrencia tipo;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(length = 500)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    protected Ocorrencia() {
    }

    public Ocorrencia(TipoOcorrencia tipo, Double latitude, Double longitude, LocalDateTime dataHora,
                      String descricao, Usuario usuario) {
        this.tipo = tipo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dataHora = dataHora;
        this.descricao = descricao;
        this.usuario = usuario;
    }

    public Long getId() { return id; }
    public TipoOcorrencia getTipo() { return tipo; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public LocalDateTime getDataHora() { return dataHora; }
    public String getDescricao() { return descricao; }
    public Usuario getUsuario() { return usuario; }
}
