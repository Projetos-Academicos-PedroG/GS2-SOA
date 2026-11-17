package br.com.fiap.pedro.domain.repository;

import br.com.fiap.pedro.domain.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    List<Alerta> findTop20ByUsuarioIdOrderByCriadoEmDesc(Long usuarioId);
}
