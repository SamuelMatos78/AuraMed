package com.auramed.auramed.repository;

import com.auramed.auramed.model.Internacao;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface InternacaoRepository extends JpaRepository<Internacao, Long> {
    long countByQuartoIdAndDataAltaIsNull(Long quartoId);
    boolean existsByPacienteIdAndDataAltaIsNull(Long pacienteId);
    List<Internacao> findByPacienteIdOrderByDataEntradaDesc(Long pacienteId);

    @Query("select i from Internacao i join fetch i.paciente join fetch i.profissional join fetch i.quarto order by i.dataEntrada desc")
    List<Internacao> listarComDetalhes();

    @Query("select i.paciente.id from Internacao i where i.dataAlta is null")
    List<Long> idsPacientesInternados();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from Internacao i where i.id = :id")
    Optional<Internacao> buscarComBloqueio(@Param("id") Long id);
}
