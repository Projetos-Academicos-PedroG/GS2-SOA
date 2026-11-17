package br.com.fiap.pedro.domain.repository;

import br.com.fiap.pedro.domain.model.Dispositivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DispositivoRepository extends JpaRepository<Dispositivo, Long> {
    boolean existsByImei(String imei);
    Optional<Dispositivo> findByImei(String imei);
    List<Dispositivo> findByUsuarioId(Long usuarioId);
}
