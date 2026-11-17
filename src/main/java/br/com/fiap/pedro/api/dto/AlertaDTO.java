package br.com.fiap.pedro.api.dto;

import br.com.fiap.pedro.domain.model.Alerta;
import br.com.fiap.pedro.domain.model.NivelRisco;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public final class AlertaDTO {
    private AlertaDTO() {
    }

    public record AvaliacaoRequest(
            @Schema(description = "Latitude atual do usuário (GPS)", example = "-23.5614") @NotNull @DecimalMin("-90") @DecimalMax("90") Double latitude,
            @Schema(description = "Longitude atual do usuário (GPS)", example = "-46.6559") @NotNull @DecimalMin("-180") @DecimalMax("180") Double longitude,
            @Schema(description = "Opcional: associa o alerta ao histórico do usuário", example = "1") Long usuarioId) {
    }

    public record Response(Long alertaId, Double latitude, Double longitude,
                           @Schema(description = "Probabilidade estimada de risco (0 a 1)") Double score,
                           NivelRisco nivel, Integer ocorrenciasProximas, String mensagem,
                           List<String> recomendacoes, LocalDateTime avaliadoEm) {
        public static Response de(Alerta a, List<String> recomendacoes) {
            return new Response(a.getId(), a.getLatitude(), a.getLongitude(), a.getScore(), a.getNivel(),
                    a.getOcorrenciasProximas(), a.getMensagem(), recomendacoes, a.getCriadoEm());
        }
    }
}
