package br.com.fiap.pedro.domain.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "dispositivo")
public class Dispositivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false, length = 60)
    private String marca;

    @Column(nullable = false, length = 80)
    private String modelo;

    @Column(nullable = false, unique = true, length = 15)
    private String imei;

    @Column(length = 40)
    private String operadora;

    @Column(name = "numero_linha", length = 20)
    private String numeroLinha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusDispositivo status = StatusDispositivo.ATIVO;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    protected Dispositivo() {
    }

    public Dispositivo(Usuario usuario, String marca, String modelo, String imei, String operadora, String numeroLinha) {
        this.usuario = usuario;
        this.marca = marca;
        this.modelo = modelo;
        this.imei = imei;
        this.operadora = operadora;
        this.numeroLinha = numeroLinha;
    }

    @PrePersist
    void prePersist() {
        if (criadoEm == null) criadoEm = LocalDateTime.now();
    }

    public void marcarComoRoubado() {
        if (status == StatusDispositivo.ROUBADO || status == StatusDispositivo.BLOQUEADO) {
            throw new IllegalStateException("Dispositivo já possui incidente em andamento");
        }
        this.status = StatusDispositivo.ROUBADO;
    }

    public void marcarComoBloqueado() {
        this.status = StatusDispositivo.BLOQUEADO;
    }

    public void marcarComoRecuperado() {
        this.status = StatusDispositivo.RECUPERADO;
    }

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public String getImei() { return imei; }
    public String getOperadora() { return operadora; }
    public String getNumeroLinha() { return numeroLinha; }
    public StatusDispositivo getStatus() { return status; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}
