package com.auramed.auramed.repository;

import com.auramed.auramed.model.Consulta;
import com.auramed.auramed.model.StatusConsulta;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    boolean existsByProfissionalIdAndDataHoraAndStatus(Long profissionalId, LocalDateTime dataHora, StatusConsulta status);
    List<Consulta> findByPacienteIdAndStatusOrderByDataHoraDesc(Long pacienteId, StatusConsulta status);
    List<Consulta> findByProfissionalIdAndDataHoraBetweenOrderByDataHora(Long profissionalId, LocalDateTime inicio, LocalDateTime fim);
    boolean existsByPacienteId(Long pacienteId);
    boolean existsByProfissionalId(Long profissionalId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Consulta c where c.id = :id")
    Optional<Consulta> buscarComBloqueio(@Param("id") Long id);
}
