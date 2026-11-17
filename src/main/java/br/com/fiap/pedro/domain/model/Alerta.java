package br.com.fiap.pedro.domain.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "alerta")
public class Alerta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Double score;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NivelRisco nivel;

    @Column(name = "ocorrencias_proximas", nullable = false)
    private Integer ocorrenciasProximas;

    @Column(nullable = false, length = 500)
    private String mensagem;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    protected Alerta() {
    }

    public Alerta(Usuario usuario, Double latitude, Double longitude, Double score, NivelRisco nivel,
                  Integer ocorrenciasProximas, String mensagem, LocalDateTime criadoEm) {
        this.usuario = usuario;
        this.latitude = latitude;
        this.longitude = longitude;
        this.score = score;
        this.nivel = nivel;
        this.ocorrenciasProximas = ocorrenciasProximas;
        this.mensagem = mensagem;
        this.criadoEm = criadoEm;
    }

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public Double getScore() { return score; }
    public NivelRisco getNivel() { return nivel; }
    public Integer getOcorrenciasProximas() { return ocorrenciasProximas; }
    public String getMensagem() { return mensagem; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}
