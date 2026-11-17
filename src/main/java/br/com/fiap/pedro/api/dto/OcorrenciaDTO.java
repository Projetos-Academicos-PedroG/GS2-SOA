package br.com.fiap.pedro.api.dto;

import br.com.fiap.pedro.domain.model.Ocorrencia;
import br.com.fiap.pedro.domain.model.TipoOcorrencia;
import br.com.fiap.pedro.domain.service.Geo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public final class OcorrenciaDTO {
    private OcorrenciaDTO() {
    }

    public record Request(
            @Schema(example = "FURTO") @NotNull TipoOcorrencia tipo,
            @Schema(example = "-23.5614") @NotNull @DecimalMin("-90") @DecimalMax("90") Double latitude,
            @Schema(example = "-46.6559") @NotNull @DecimalMin("-180") @DecimalMax("180") Double longitude,
            @Schema(description = "Quando ocorreu. Se omitido, usa o horário atual.", example = "2026-11-10T19:30:00")
            @PastOrPresent LocalDateTime dataHora,
            @Schema(example = "Celular levado da mão na saída do metrô") @Size(max = 500) String descricao,
            @Schema(description = "Opcional: usuário que registrou", example = "1") Long usuarioId) {
    }

    public record Response(Long id, TipoOcorrencia tipo, Double latitude, Double longitude,
                           LocalDateTime dataHora, String descricao, Integer distanciaMetros) {
        public static Response de(Ocorrencia o) {
            return new Response(o.getId(), o.getTipo(), o.getLatitude(), o.getLongitude(),
                    o.getDataHora(), o.getDescricao(), null);
        }

        public static Response de(Ocorrencia o, double lat, double lng) {
            int dist = (int) Math.round(Geo.distanciaMetros(lat, lng, o.getLatitude(), o.getLongitude()));
            return new Response(o.getId(), o.getTipo(), o.getLatitude(), o.getLongitude(),
                    o.getDataHora(), o.getDescricao(), dist);
        }
    }
}
