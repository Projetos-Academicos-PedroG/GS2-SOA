package br.com.fiap.pedro.domain.service;

import br.com.fiap.pedro.domain.model.Ocorrencia;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ModeloRiscoFurto {
    static final double B0 = -3.0;
    static final double W_DENSIDADE = 1.6;
    static final double W_HORARIO = 1.5;
    static final double W_CONCENTRACAO = 1.2;

    private static final double[] RISCO_POR_HORA = {
            0.70, 0.65, 0.60, 0.50, 0.40, 0.45,
            0.60, 0.75, 0.70, 0.45, 0.35, 0.35,
            0.40, 0.40, 0.35, 0.40, 0.50, 0.80,
            0.95, 1.00, 0.90, 0.85, 0.80, 0.75
    };

    public record Features(double densidade, double riscoHorario, double concentracaoHorario, int ocorrenciasProximas) {
    }

    public Features extrairFeatures(double lat, double lng, double raioMetros,
                                    List<Ocorrencia> ocorrencias, LocalDateTime agora) {
        double densidade = 0;
        int proximas = 0;
        int mesmaJanela = 0;

        for (Ocorrencia o : ocorrencias) {
            double d = Geo.distanciaMetros(lat, lng, o.getLatitude(), o.getLongitude());
            if (d > raioMetros) continue;
            proximas++;

            double fatorDistancia = 1.0 - (d / raioMetros);
            double dias = Math.max(0, Duration.between(o.getDataHora(), agora).toHours() / 24.0);
            double fatorRecencia = Math.exp(-dias / 30.0);
            densidade += o.getTipo().getPeso() * (0.3 + 0.7 * fatorDistancia) * fatorRecencia;

            int diffHora = Math.abs(o.getDataHora().getHour() - agora.getHour());
            diffHora = Math.min(diffHora, 24 - diffHora);
            if (diffHora <= 2) mesmaJanela++;
        }

        double concentracao = proximas == 0 ? 0 : (double) mesmaJanela / proximas;
        return new Features(densidade, RISCO_POR_HORA[agora.getHour()], concentracao, proximas);
    }

    public double prever(Features f) {
        double z = B0
                + W_DENSIDADE * Math.log1p(f.densidade())
                + W_HORARIO * f.riscoHorario()
                + W_CONCENTRACAO * f.concentracaoHorario();
        return 1.0 / (1.0 + Math.exp(-z));
    }
}
