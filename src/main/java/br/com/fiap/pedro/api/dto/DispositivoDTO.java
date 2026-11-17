package br.com.fiap.pedro.api.dto;

import br.com.fiap.pedro.domain.model.Dispositivo;
import br.com.fiap.pedro.domain.model.StatusDispositivo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public final class DispositivoDTO {
    private DispositivoDTO() {
    }

    public record Request(
            @Schema(example = "1") @NotNull Long usuarioId,
            @Schema(example = "Samsung") @NotBlank @Size(max = 60) String marca,
            @Schema(example = "Galaxy S24") @NotBlank @Size(max = 80) String modelo,
            @Schema(description = "IMEI de 15 dígitos (validado por Luhn). Disque *#06# para ver o seu.", example = "490154203237518")
            @NotBlank @Pattern(regexp = "\\d{15}", message = "deve conter exatamente 15 dígitos") String imei,
            @Schema(example = "Vivo") @Size(max = 40) String operadora,
            @Schema(example = "11999990000") @Pattern(regexp = "\\d{10,11}", message = "deve conter 10 ou 11 dígitos") String numeroLinha) {
    }

    public record Response(Long id, Long usuarioId, String marca, String modelo, String imei,
                           String operadora, String numeroLinha, StatusDispositivo status, LocalDateTime criadoEm) {
        public static Response de(Dispositivo d) {
            return new Response(d.getId(), d.getUsuario().getId(), d.getMarca(), d.getModelo(), d.getImei(),
                    d.getOperadora(), d.getNumeroLinha(), d.getStatus(), d.getCriadoEm());
        }
    }

    @Schema(description = "Consulta pública de IMEI: permite verificar se um aparelho usado tem registro de roubo antes de comprá-lo")
    public record ConsultaImei(String imei, String marca, String modelo, StatusDispositivo status, boolean possuiRegistroDeRoubo) {
        public static ConsultaImei de(Dispositivo d) {
            boolean roubado = d.getStatus() == StatusDispositivo.ROUBADO || d.getStatus() == StatusDispositivo.BLOQUEADO;
            return new ConsultaImei(d.getImei(), d.getMarca(), d.getModelo(), d.getStatus(), roubado);
        }
    }
}
