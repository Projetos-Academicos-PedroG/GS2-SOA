package br.com.fiap.pedro.domain.service;

import br.com.fiap.pedro.domain.model.*;
import br.com.fiap.pedro.domain.repository.IncidenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class IncidenteService {
    private final IncidenteRepository repository;
    private final DispositivoService dispositivoService;
    private final Clock clock;

    public IncidenteService(IncidenteRepository repository, DispositivoService dispositivoService, Clock clock) {
        this.repository = repository;
        this.dispositivoService = dispositivoService;
        this.clock = clock;
    }

    @Transactional
    public Incidente abrir(Long dispositivoId, TipoOcorrencia tipo, double lat, double lng, String descricao) {
        Dispositivo dispositivo = dispositivoService.buscar(dispositivoId);
        if (repository.existsByDispositivoIdAndStatus(dispositivoId, StatusIncidente.ABERTO)) {
            throw new RegraNegocioException("Já existe um incidente aberto para este dispositivo");
        }
        try {
            dispositivo.marcarComoRoubado();
        } catch (IllegalStateException e) {
            throw new RegraNegocioException(e.getMessage());
        }
        LocalDateTime agora = LocalDateTime.now(clock);

        Ocorrencia ocorrencia = new Ocorrencia(tipo, lat, lng, agora, descricao, dispositivo.getUsuario());
        return repository.save(new Incidente(dispositivo, ocorrencia, agora));
    }

    @Transactional(readOnly = true)
    public Incidente buscar(Long id) {
        Incidente incidente = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Incidente", id));
        incidente.getPassos().size();
        return incidente;
    }

    @Transactional(readOnly = true)
    public List<Incidente> listarDoUsuario(Long usuarioId) {
        List<Incidente> lista = repository.findByDispositivoUsuarioIdOrderByAbertoEmDesc(usuarioId);
        lista.forEach(i -> i.getPassos().size());
        return lista;
    }

    @Transactional
    public Incidente concluirPasso(Long incidenteId, Long passoId) {
        Incidente incidente = buscar(incidenteId);
        try {
            incidente.concluirPasso(passoId, LocalDateTime.now(clock));
        } catch (IllegalArgumentException e) {
            throw new RecursoNaoEncontradoException("Passo", passoId);
        } catch (IllegalStateException e) {
            throw new RegraNegocioException(e.getMessage());
        }
        return incidente;
    }

    public LocalDateTime agora() {
        return LocalDateTime.now(clock);
    }
}
