package br.com.fiap.pedro.api.dto;

import br.com.fiap.pedro.domain.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public final class UsuarioDTO {
    private UsuarioDTO() {
    }

    public record Request(
            @Schema(example = "Maria Souza") @NotBlank @Size(max = 120) String nome,
            @Schema(example = "maria@email.com") @NotBlank @Email @Size(max = 160) String email,
            @Schema(description = "Telefone de um contato de confiança (para quando o celular for roubado)", example = "11988887777")
            @Pattern(regexp = "\\d{10,11}", message = "deve conter 10 ou 11 dígitos") String telefoneContato) {
    }

    public record Response(Long id, String nome, String email, String telefoneContato, LocalDateTime criadoEm) {
        public static Response de(Usuario u) {
            return new Response(u.getId(), u.getNome(), u.getEmail(), u.getTelefoneContato(), u.getCriadoEm());
        }
    }
}
