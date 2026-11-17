package br.com.fiap.pedro.api.dto;

import br.com.fiap.pedro.domain.model.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

public final class IncidenteDTO {
    private IncidenteDTO() {
    }

    public record Request(
            @Schema(example = "1") @NotNull Long dispositivoId,
            @Schema(example = "ROUBO") @NotNull TipoOcorrencia tipo,
            @Schema(description = "Onde aconteceu", example = "-23.5614") @NotNull @DecimalMin("-90") @DecimalMax("90") Double latitude,
            @Schema(example = "-46.6559") @NotNull @DecimalMin("-180") @DecimalMax("180") Double longitude,
            @Schema(example = "Abordado por ciclista na Av. Paulista") @Size(max = 500) String descricao) {
    }

    public record PassoResponse(Long id, Integer ordem, AcaoHoraDeOuro acao, String titulo, String instrucoes,
                                Integer prazoMinutos, boolean concluido, LocalDateTime concluidoEm) {
        public static PassoResponse de(PassoResposta p) {
            return new PassoResponse(p.getId(), p.getOrdem(), p.getAcao(), p.getAcao().getTitulo(),
                    p.getAcao().getDescricao(), p.getPrazoMinutos(), p.isConcluido(), p.getConcluidoEm());
        }
    }

    public record Response(Long id, Long dispositivoId, String imei, StatusDispositivo statusDispositivo,
                           StatusIncidente status, LocalDateTime abertoEm, LocalDateTime concluidoEm,
                           @Schema(description = "Percentual de passos concluídos") Integer progresso,
                           @Schema(description = "Minutos restantes da hora de ouro") Long minutosRestantesHoraDeOuro,
                           @Schema(description = "Próxima ação recomendada") String proximaAcao,
                           List<PassoResponse> passos) {
        public static Response de(Incidente i, LocalDateTime agora) {
            String proxima = i.getPassos().stream().filter(p -> !p.isConcluido())
                    .findFirst().map(p -> p.getAcao().getTitulo()).orElse(null);
            return new Response(i.getId(), i.getDispositivo().getId(), i.getDispositivo().getImei(),
                    i.getDispositivo().getStatus(), i.getStatus(), i.getAbertoEm(), i.getConcluidoEm(),
                    i.getProgresso(), i.minutosRestantesHoraDeOuro(agora), proxima,
                    i.getPassos().stream().map(PassoResponse::de).toList());
        }
    }
}
