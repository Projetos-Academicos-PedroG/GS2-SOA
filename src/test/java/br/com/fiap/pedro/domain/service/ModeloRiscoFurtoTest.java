package br.com.fiap.pedro.domain.service;

import br.com.fiap.pedro.domain.model.NivelRisco;
import br.com.fiap.pedro.domain.model.Ocorrencia;
import br.com.fiap.pedro.domain.model.TipoOcorrencia;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ModeloRiscoFurtoTest {
    private final ModeloRiscoFurto modelo = new ModeloRiscoFurto();
    private static final double LAT = -23.5614, LNG = -46.6559;

    private Ocorrencia ocorrencia(TipoOcorrencia tipo, double lat, double lng, LocalDateTime quando) {
        return new Ocorrencia(tipo, lat, lng, quando, null, null);
    }

    @Test
    void semOcorrenciasDeManhaRiscoEhBaixo() {
        LocalDateTime agora = LocalDateTime.of(2026, 11, 15, 10, 0);
        var f = modelo.extrairFeatures(LAT, LNG, 500, List.of(), agora);
        double score = modelo.prever(f);

        assertThat(f.ocorrenciasProximas()).isZero();
        assertThat(NivelRisco.deScore(score)).isEqualTo(NivelRisco.BAIXO);
    }

    @Test
    void muitasOcorrenciasRecentesNoMesmoHorarioGeramRiscoCritico() {
        LocalDateTime agora = LocalDateTime.of(2026, 11, 15, 19, 0);
        List<Ocorrencia> lista = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            lista.add(ocorrencia(TipoOcorrencia.ROUBO, LAT + 0.0005, LNG, agora.minusDays(i + 1)));
        }
        double score = modelo.prever(modelo.extrairFeatures(LAT, LNG, 500, lista, agora));

        assertThat(NivelRisco.deScore(score)).isEqualTo(NivelRisco.CRITICO);
    }

    @Test
    void ocorrenciasForaDoRaioSaoIgnoradas() {
        LocalDateTime agora = LocalDateTime.of(2026, 11, 15, 19, 0);

        var longe = List.of(ocorrencia(TipoOcorrencia.ROUBO, -23.5503, -46.6340, agora.minusDays(1)));
        var f = modelo.extrairFeatures(LAT, LNG, 500, longe, agora);

        assertThat(f.ocorrenciasProximas()).isZero();
        assertThat(f.densidade()).isZero();
    }

    @Test
    void ocorrenciasAntigasPesamMenosQueRecentes() {
        LocalDateTime agora = LocalDateTime.of(2026, 11, 15, 19, 0);
        var recente = List.of(ocorrencia(TipoOcorrencia.FURTO, LAT, LNG, agora.minusDays(1)));
        var antiga = List.of(ocorrencia(TipoOcorrencia.FURTO, LAT, LNG, agora.minusDays(80)));

        double scoreRecente = modelo.prever(modelo.extrairFeatures(LAT, LNG, 500, recente, agora));
        double scoreAntiga = modelo.prever(modelo.extrairFeatures(LAT, LNG, 500, antiga, agora));

        assertThat(scoreRecente).isGreaterThan(scoreAntiga);
    }

    @Test
    void roubosPesamMaisQueTentativas() {
        LocalDateTime agora = LocalDateTime.of(2026, 11, 15, 19, 0);
        var roubo = List.of(ocorrencia(TipoOcorrencia.ROUBO, LAT, LNG, agora.minusDays(2)));
        var tentativa = List.of(ocorrencia(TipoOcorrencia.TENTATIVA, LAT, LNG, agora.minusDays(2)));

        assertThat(modelo.extrairFeatures(LAT, LNG, 500, roubo, agora).densidade())
                .isGreaterThan(modelo.extrairFeatures(LAT, LNG, 500, tentativa, agora).densidade());
    }

    @Test
    void scoreSempreEntreZeroEUm() {
        LocalDateTime agora = LocalDateTime.of(2026, 11, 15, 3, 0);
        List<Ocorrencia> lista = new ArrayList<>();
        for (int i = 0; i < 500; i++) lista.add(ocorrencia(TipoOcorrencia.ROUBO, LAT, LNG, agora));
        double score = modelo.prever(modelo.extrairFeatures(LAT, LNG, 500, lista, agora));

        assertThat(score).isBetween(0.0, 1.0);
    }
}
