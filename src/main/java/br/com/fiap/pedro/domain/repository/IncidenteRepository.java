package br.com.fiap.pedro.domain.repository;

import br.com.fiap.pedro.domain.model.Incidente;
import br.com.fiap.pedro.domain.model.StatusIncidente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidenteRepository extends JpaRepository<Incidente, Long> {
    List<Incidente> findByDispositivoUsuarioIdOrderByAbertoEmDesc(Long usuarioId);
    boolean existsByDispositivoIdAndStatus(Long dispositivoId, StatusIncidente status);
}
