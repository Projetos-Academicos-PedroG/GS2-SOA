package br.com.fiap.pedro.domain.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "passo_resposta")
public class PassoResposta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "incidente_id")
    private Incidente incidente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private AcaoHoraDeOuro acao;

    @Column(nullable = false)
    private Integer ordem;

    @Column(name = "prazo_minutos", nullable = false)
    private Integer prazoMinutos;

    @Column(nullable = false)
    private boolean concluido;

    @Column(name = "concluido_em")
    private LocalDateTime concluidoEm;

    protected PassoResposta() {
    }

    PassoResposta(Incidente incidente, AcaoHoraDeOuro acao) {
        this.incidente = incidente;
        this.acao = acao;
        this.ordem = acao.getOrdem();
        this.prazoMinutos = acao.getPrazoMinutos();
    }

    void concluir(LocalDateTime quando) {
        if (concluido) {
            throw new IllegalStateException("Passo já foi concluído");
        }
        this.concluido = true;
        this.concluidoEm = quando;
    }

    public Long getId() { return id; }
    public Incidente getIncidente() { return incidente; }
    public AcaoHoraDeOuro getAcao() { return acao; }
    public Integer getOrdem() { return ordem; }
    public Integer getPrazoMinutos() { return prazoMinutos; }
    public boolean isConcluido() { return concluido; }
    public LocalDateTime getConcluidoEm() { return concluidoEm; }
}
