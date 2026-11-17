package br.com.fiap.pedro.domain.repository;

import br.com.fiap.pedro.domain.model.Ocorrencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, Long> {
    @Query("""
            select o from Ocorrencia o
            where o.latitude between :latMin and :latMax
              and o.longitude between :lngMin and :lngMax
              and o.dataHora >= :desde
            """)
    List<Ocorrencia> buscarNaArea(@Param("latMin") double latMin, @Param("latMax") double latMax,
                                  @Param("lngMin") double lngMin, @Param("lngMax") double lngMax,
                                  @Param("desde") LocalDateTime desde);
}
