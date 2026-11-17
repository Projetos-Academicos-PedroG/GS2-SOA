package br.com.fiap.pedro.api.controller;

import br.com.fiap.pedro.api.dto.OcorrenciaDTO;
import br.com.fiap.pedro.domain.model.Ocorrencia;
import br.com.fiap.pedro.domain.service.OcorrenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/ocorrencias")
@Tag(name = "Ocorrências")
public class OcorrenciaController {
    private final OcorrenciaService service;

    public OcorrenciaController(OcorrenciaService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Registra uma ocorrência de furto/roubo (base colaborativa)")
    public ResponseEntity<OcorrenciaDTO.Response> registrar(@RequestBody @Valid OcorrenciaDTO.Request req) {
        Ocorrencia o = service.registrar(req.tipo(), req.latitude(), req.longitude(), req.dataHora(),
                req.descricao(), req.usuarioId());
        var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(o.getId()).toUri();
        return ResponseEntity.created(uri).body(OcorrenciaDTO.Response.de(o));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca ocorrência por id")
    public OcorrenciaDTO.Response buscar(@PathVariable Long id) {
        return OcorrenciaDTO.Response.de(service.buscar(id));
    }

    @GetMapping("/proximas")
    @Operation(summary = "Lista ocorrências próximas a um ponto (para o mapa de calor do app)")
    public List<OcorrenciaDTO.Response> proximas(
            @Parameter(example = "-23.5614") @RequestParam double latitude,
            @Parameter(example = "-46.6559") @RequestParam double longitude,
            @Parameter(description = "Raio em metros") @RequestParam(defaultValue = "1000") @Min(50) @Max(10000) double raioMetros,
            @Parameter(description = "Últimos N dias") @RequestParam(defaultValue = "90") @Min(1) @Max(365) int dias) {
        return service.proximas(latitude, longitude, raioMetros, dias).stream()
                .map(o -> OcorrenciaDTO.Response.de(o, latitude, longitude))
                .toList();
    }
}
