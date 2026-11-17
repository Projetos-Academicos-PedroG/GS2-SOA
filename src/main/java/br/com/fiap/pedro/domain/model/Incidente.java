package br.com.fiap.pedro.domain.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "incidente")
public class Incidente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dispositivo_id")
    private Dispositivo dispositivo;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ocorrencia_id")
    private Ocorrencia ocorrencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusIncidente status = StatusIncidente.ABERTO;

    @Column(name = "aberto_em", nullable = false)
    private LocalDateTime abertoEm;

    @Column(name = "concluido_em")
    private LocalDateTime concluidoEm;

    @OneToMany(mappedBy = "incidente", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordem ASC")
    private List<PassoResposta> passos = new ArrayList<>();

    protected Incidente() {
    }

    public Incidente(Dispositivo dispositivo, Ocorrencia ocorrencia, LocalDateTime abertoEm) {
        this.dispositivo = dispositivo;
        this.ocorrencia = ocorrencia;
        this.abertoEm = abertoEm;
        for (AcaoHoraDeOuro acao : AcaoHoraDeOuro.values()) {
            passos.add(new PassoResposta(this, acao));
        }
        passos.sort(Comparator.comparing(PassoResposta::getOrdem));
    }

    public PassoResposta concluirPasso(Long passoId, LocalDateTime quando) {
        if (status == StatusIncidente.CONCLUIDO) {
            throw new IllegalStateException("Incidente já está concluído");
        }
        PassoResposta passo = passos.stream()
                .filter(p -> p.getId().equals(passoId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Passo " + passoId + " não pertence ao incidente " + id));
        passo.concluir(quando);

        if (passo.getAcao() == AcaoHoraDeOuro.BLOQUEAR_IMEI) {
            dispositivo.marcarComoBloqueado();
        }
        if (passos.stream().allMatch(PassoResposta::isConcluido)) {
            status = StatusIncidente.CONCLUIDO;
            concluidoEm = quando;
        }
        return passo;
    }

    public int getProgresso() {
        if (passos.isEmpty()) return 0;
        long feitos = passos.stream().filter(PassoResposta::isConcluido).count();
        return (int) Math.round(feitos * 100.0 / passos.size());
    }

    public long minutosRestantesHoraDeOuro(LocalDateTime agora) {
        long decorridos = java.time.Duration.between(abertoEm, agora).toMinutes();
        return Math.max(0, 60 - decorridos);
    }

    public Long getId() { return id; }
    public Dispositivo getDispositivo() { return dispositivo; }
    public Ocorrencia getOcorrencia() { return ocorrencia; }
    public StatusIncidente getStatus() { return status; }
    public LocalDateTime getAbertoEm() { return abertoEm; }
    public LocalDateTime getConcluidoEm() { return concluidoEm; }
    public List<PassoResposta> getPassos() { return passos; }
}
