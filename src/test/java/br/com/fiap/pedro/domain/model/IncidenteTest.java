package br.com.fiap.pedro.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IncidenteTest {
    private final LocalDateTime abertura = LocalDateTime.of(2026, 11, 15, 19, 0);
    private Dispositivo dispositivo;
    private Incidente incidente;

    @BeforeEach
    void setUp() {
        Usuario u = new Usuario("Ana", "ana@email.com", null);
        dispositivo = new Dispositivo(u, "Apple", "iPhone 15", "490154203237518", "Claro", null);
        dispositivo.marcarComoRoubado();
        incidente = new Incidente(dispositivo, null, abertura);
        long id = 1;
        for (PassoResposta p : incidente.getPassos()) ReflectionTestUtils.setField(p, "id", id++);
    }

    @Test
    void geraChecklistCompletoEmOrdem() {
        assertThat(incidente.getPassos()).hasSize(AcaoHoraDeOuro.values().length);
        assertThat(incidente.getPassos().get(0).getAcao()).isEqualTo(AcaoHoraDeOuro.BLOQUEAR_LINHA);
        assertThat(incidente.getProgresso()).isZero();
        assertThat(incidente.getStatus()).isEqualTo(StatusIncidente.ABERTO);
    }

    @Test
    void concluirPassoAtualizaProgresso() {
        incidente.concluirPasso(1L, abertura.plusMinutes(5));
        assertThat(incidente.getProgresso()).isEqualTo(14);
        assertThatThrownBy(() -> incidente.concluirPasso(1L, abertura.plusMinutes(6)))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void concluirTodosOsPassosEncerraIncidenteEBloqueiaDispositivo() {
        incidente.getPassos().forEach(p -> incidente.concluirPasso(p.getId(), abertura.plusMinutes(40)));

        assertThat(incidente.getStatus()).isEqualTo(StatusIncidente.CONCLUIDO);
        assertThat(incidente.getProgresso()).isEqualTo(100);
        assertThat(incidente.getConcluidoEm()).isEqualTo(abertura.plusMinutes(40));
        assertThat(dispositivo.getStatus()).isEqualTo(StatusDispositivo.BLOQUEADO);
    }

    @Test
    void contagemRegressivaDaHoraDeOuro() {
        assertThat(incidente.minutosRestantesHoraDeOuro(abertura.plusMinutes(15))).isEqualTo(45);
        assertThat(incidente.minutosRestantesHoraDeOuro(abertura.plusHours(3))).isZero();
    }

    @Test
    void dispositivoJaRoubadoNaoPodeAbrirNovoIncidente() {
        assertThatThrownBy(dispositivo::marcarComoRoubado).isInstanceOf(IllegalStateException.class);
    }
}
