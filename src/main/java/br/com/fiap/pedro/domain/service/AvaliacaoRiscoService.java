package br.com.fiap.pedro.domain.service;

import br.com.fiap.pedro.domain.model.Alerta;
import br.com.fiap.pedro.domain.model.NivelRisco;
import br.com.fiap.pedro.domain.model.Ocorrencia;
import br.com.fiap.pedro.domain.model.Usuario;
import br.com.fiap.pedro.domain.repository.AlertaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AvaliacaoRiscoService {
    private final OcorrenciaService ocorrenciaService;
    private final UsuarioService usuarioService;
    private final AlertaRepository alertaRepository;
    private final ModeloRiscoFurto modelo;
    private final Clock clock;
    private final double raioMetros;
    private final int janelaDias;

    public AvaliacaoRiscoService(OcorrenciaService ocorrenciaService, UsuarioService usuarioService,
                                 AlertaRepository alertaRepository, ModeloRiscoFurto modelo, Clock clock,
                                 @Value("${pedro.risco.raio-metros:500}") double raioMetros,
                                 @Value("${pedro.risco.janela-dias:90}") int janelaDias) {
        this.ocorrenciaService = ocorrenciaService;
        this.usuarioService = usuarioService;
        this.alertaRepository = alertaRepository;
        this.modelo = modelo;
        this.clock = clock;
        this.raioMetros = raioMetros;
        this.janelaDias = janelaDias;
    }

    public record Resultado(Alerta alerta, List<String> recomendacoes) {
    }

    @Transactional
    public Resultado avaliar(double lat, double lng, Long usuarioId) {
        LocalDateTime agora = LocalDateTime.now(clock);
        Usuario usuario = usuarioId != null ? usuarioService.buscar(usuarioId) : null;

        List<Ocorrencia> vizinhas = ocorrenciaService.proximas(lat, lng, raioMetros, janelaDias);
        ModeloRiscoFurto.Features f = modelo.extrairFeatures(lat, lng, raioMetros, vizinhas, agora);
        double score = arredondar(modelo.prever(f));
        NivelRisco nivel = NivelRisco.deScore(score);

        Alerta alerta = alertaRepository.save(new Alerta(usuario, lat, lng, score, nivel,
                f.ocorrenciasProximas(), mensagem(nivel, f.ocorrenciasProximas()), agora));
        return new Resultado(alerta, recomendacoes(nivel));
    }

    @Transactional(readOnly = true)
    public List<Alerta> historico(Long usuarioId) {
        usuarioService.buscar(usuarioId);
        return alertaRepository.findTop20ByUsuarioIdOrderByCriadoEmDesc(usuarioId);
    }

    private String mensagem(NivelRisco nivel, int proximas) {
        return switch (nivel) {
            case BAIXO -> "Risco baixo nesta região agora.";
            case MEDIO -> "Atenção: " + proximas + " ocorrência(s) registrada(s) nas proximidades recentemente.";
            case ALTO -> "Risco alto de furto de celular nesta área e horário. Evite usar o aparelho na rua.";
            case CRITICO -> "ALERTA: área crítica para furto de celular neste horário. Guarde o aparelho imediatamente.";
        };
    }

    private List<String> recomendacoes(NivelRisco nivel) {
        List<String> r = new ArrayList<>();
        r.add("Mantenha o bloqueio de tela e a localização remota ativados.");
        if (nivel.ordinal() >= NivelRisco.MEDIO.ordinal()) {
            r.add("Evite usar o celular próximo à rua, pontos de ônibus e saídas de metrô.");
            r.add("Use fones sem fio com o aparelho guardado em bolso interno.");
        }
        if (nivel.ordinal() >= NivelRisco.ALTO.ordinal()) {
            r.add("Se precisar atender, entre em um estabelecimento comercial.");
            r.add("Ative o modo 'proteção contra roubo' do sistema (Android/iOS).");
        }
        if (nivel == NivelRisco.CRITICO) {
            r.add("Considere mudar de rota: há concentração de ocorrências neste horário.");
        }
        return r;
    }

    private static double arredondar(double v) {
        return Math.round(v * 1000.0) / 1000.0;
    }
}
