package br.com.fiap.pedro.domain.service;

import br.com.fiap.pedro.domain.model.Ocorrencia;
import br.com.fiap.pedro.domain.model.TipoOcorrencia;
import br.com.fiap.pedro.domain.model.Usuario;
import br.com.fiap.pedro.domain.repository.OcorrenciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class OcorrenciaService {
    private final OcorrenciaRepository repository;
    private final UsuarioService usuarioService;
    private final Clock clock;

    public OcorrenciaService(OcorrenciaRepository repository, UsuarioService usuarioService, Clock clock) {
        this.repository = repository;
        this.usuarioService = usuarioService;
        this.clock = clock;
    }

    @Transactional
    public Ocorrencia registrar(TipoOcorrencia tipo, double lat, double lng, LocalDateTime dataHora,
                                String descricao, Long usuarioId) {
        LocalDateTime agora = LocalDateTime.now(clock);
        LocalDateTime quando = dataHora != null ? dataHora : agora;
        if (quando.isAfter(agora.plusMinutes(5))) {
            throw new RegraNegocioException("A data da ocorrência não pode estar no futuro");
        }
        Usuario usuario = usuarioId != null ? usuarioService.buscar(usuarioId) : null;
        return repository.save(new Ocorrencia(tipo, lat, lng, quando, descricao, usuario));
    }

    @Transactional(readOnly = true)
    public Ocorrencia buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Ocorrência", id));
    }

    @Transactional(readOnly = true)
    public List<Ocorrencia> proximas(double lat, double lng, double raioMetros, int dias) {
        LocalDateTime desde = LocalDateTime.now(clock).minusDays(dias);
        double dLat = Geo.deltaLatitude(raioMetros);
        double dLng = Geo.deltaLongitude(raioMetros, lat);
        return repository.buscarNaArea(lat - dLat, lat + dLat, lng - dLng, lng + dLng, desde).stream()
                .filter(o -> Geo.distanciaMetros(lat, lng, o.getLatitude(), o.getLongitude()) <= raioMetros)
                .sorted(Comparator.comparingDouble(o -> Geo.distanciaMetros(lat, lng, o.getLatitude(), o.getLongitude())))
                .toList();
    }
}
