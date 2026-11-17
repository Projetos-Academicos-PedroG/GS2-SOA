package br.com.fiap.pedro.api.controller;

import br.com.fiap.pedro.api.dto.IncidenteDTO;
import br.com.fiap.pedro.domain.model.AcaoHoraDeOuro;
import br.com.fiap.pedro.domain.model.Incidente;
import br.com.fiap.pedro.domain.service.IncidenteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/incidentes")
@Tag(name = "Hora de ouro")
public class IncidenteController {
    private final IncidenteService service;

    public IncidenteController(IncidenteService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Aciona o modo 'hora de ouro': registra o furto e gera o checklist de resposta rápida")
    public ResponseEntity<IncidenteDTO.Response> abrir(@RequestBody @Valid IncidenteDTO.Request req) {
        Incidente i = service.abrir(req.dispositivoId(), req.tipo(), req.latitude(), req.longitude(), req.descricao());
        var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(i.getId()).toUri();
        return ResponseEntity.created(uri).body(IncidenteDTO.Response.de(i, service.agora()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta o incidente, progresso e tempo restante da hora de ouro")
    public IncidenteDTO.Response buscar(@PathVariable Long id) {
        return IncidenteDTO.Response.de(service.buscar(id), service.agora());
    }

    @GetMapping
    @Operation(summary = "Lista incidentes de um usuário")
    public List<IncidenteDTO.Response> listar(@RequestParam Long usuarioId) {
        var agora = service.agora();
        return service.listarDoUsuario(usuarioId).stream().map(i -> IncidenteDTO.Response.de(i, agora)).toList();
    }

    @PatchMapping("/{id}/passos/{passoId}/concluir")
    @Operation(summary = "Marca um passo do checklist como concluído")
    public IncidenteDTO.Response concluirPasso(@PathVariable Long id, @PathVariable Long passoId) {
        return IncidenteDTO.Response.de(service.concluirPasso(id, passoId), service.agora());
    }

    @GetMapping("/guia")
    @Operation(summary = "Guia da hora de ouro (roteiro padrão de ações)")
    public List<Map<String, Object>> guia() {
        return Arrays.stream(AcaoHoraDeOuro.values())
                .map(a -> Map.<String, Object>of("ordem", a.getOrdem(), "acao", a.name(), "titulo", a.getTitulo(),
                        "instrucoes", a.getDescricao(), "prazoMinutos", a.getPrazoMinutos()))
                .toList();
    }
}
