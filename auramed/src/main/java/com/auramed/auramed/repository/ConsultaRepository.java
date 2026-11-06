package com.auramed.auramed.repository;

import com.auramed.auramed.model.Consulta;
import com.auramed.auramed.model.StatusConsulta;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Collection;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    @Override
    @EntityGraph(attributePaths = {"paciente", "profissional"})
    List<Consulta> findAll();

    @Override
    @EntityGraph(attributePaths = {"paciente", "profissional"})
    Optional<Consulta> findById(Long id);

    boolean existsByProfissionalIdAndDataHoraAndStatus(Long profissionalId, LocalDateTime dataHora, StatusConsulta status);
    boolean existsByProfissionalIdAndDataHoraAndStatusIn(Long profissionalId, LocalDateTime dataHora, Collection<StatusConsulta> statuses);
    List<Consulta> findByPacienteIdAndStatusOrderByDataHoraDesc(Long pacienteId, StatusConsulta status);
    List<Consulta> findByPacienteIdAndStatusInOrderByDataHoraDesc(Long pacienteId, Collection<StatusConsulta> statuses);
    @EntityGraph(attributePaths = {"paciente", "profissional"})
    List<Consulta> findByPacienteIdOrderByDataHoraDesc(Long pacienteId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(attributePaths = {"paciente", "profissional"})
    @Query("select c from Consulta c where c.id = :id")
    Optional<Consulta> buscarComBloqueio(@Param("id") Long id);
}
