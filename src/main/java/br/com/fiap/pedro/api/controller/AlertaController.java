package br.com.fiap.pedro.api.controller;

import br.com.fiap.pedro.api.dto.AlertaDTO;
import br.com.fiap.pedro.domain.service.AvaliacaoRiscoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
@Tag(name = "Alertas de risco")
public class AlertaController {
    private final AvaliacaoRiscoService service;

    public AlertaController(AvaliacaoRiscoService service) {
        this.service = service;
    }

    @PostMapping("/avaliar")
    @Operation(summary = "Avalia o risco de furto na localização atual",
            description = "Chamado periodicamente pelo app com o GPS do usuário. O modelo preditivo combina "
                    + "densidade de ocorrências próximas, recência, tipo e padrão de horário para estimar o risco.")
    public AlertaDTO.Response avaliar(@RequestBody @Valid AlertaDTO.AvaliacaoRequest req) {
        var r = service.avaliar(req.latitude(), req.longitude(), req.usuarioId());
        return AlertaDTO.Response.de(r.alerta(), r.recomendacoes());
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Histórico dos últimos 20 alertas do usuário")
    public List<AlertaDTO.Response> historico(@PathVariable Long usuarioId) {
        return service.historico(usuarioId).stream().map(a -> AlertaDTO.Response.de(a, List.of())).toList();
    }
}
