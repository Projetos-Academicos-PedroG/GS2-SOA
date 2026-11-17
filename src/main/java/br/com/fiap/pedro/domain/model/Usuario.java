package br.com.fiap.pedro.domain.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 160)
    private String email;

    @Column(length = 20)
    private String telefoneContato;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dispositivo> dispositivos = new ArrayList<>();

    protected Usuario() {
    }

    public Usuario(String nome, String email, String telefoneContato) {
        this.nome = nome;
        this.email = email;
        this.telefoneContato = telefoneContato;
    }

    @PrePersist
    void prePersist() {
        if (criadoEm == null) criadoEm = LocalDateTime.now();
    }

    public void atualizar(String nome, String email, String telefoneContato) {
        this.nome = nome;
        this.email = email;
        this.telefoneContato = telefoneContato;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTelefoneContato() { return telefoneContato; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public List<Dispositivo> getDispositivos() { return dispositivos; }
}
