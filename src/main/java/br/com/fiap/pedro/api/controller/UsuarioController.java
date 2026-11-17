package br.com.fiap.pedro.api.controller;

import br.com.fiap.pedro.api.dto.UsuarioDTO;
import br.com.fiap.pedro.domain.model.Usuario;
import br.com.fiap.pedro.domain.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários")
public class UsuarioController {
    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastra um usuário")
    public ResponseEntity<UsuarioDTO.Response> criar(@RequestBody @Valid UsuarioDTO.Request req) {
        Usuario u = service.criar(req.nome(), req.email(), req.telefoneContato());
        var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(u.getId()).toUri();
        return ResponseEntity.created(uri).body(UsuarioDTO.Response.de(u));
    }

    @GetMapping
    @Operation(summary = "Lista usuários")
    public List<UsuarioDTO.Response> listar() {
        return service.listar().stream().map(UsuarioDTO.Response::de).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca usuário por id")
    public UsuarioDTO.Response buscar(@PathVariable Long id) {
        return UsuarioDTO.Response.de(service.buscar(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um usuário")
    public UsuarioDTO.Response atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioDTO.Request req) {
        return UsuarioDTO.Response.de(service.atualizar(id, req.nome(), req.email(), req.telefoneContato()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um usuário")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
