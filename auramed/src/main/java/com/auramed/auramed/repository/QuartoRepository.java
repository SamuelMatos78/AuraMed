package com.auramed.auramed.repository;

import com.auramed.auramed.model.Quarto;
import com.auramed.auramed.model.SituacaoQuarto;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface QuartoRepository extends JpaRepository<Quarto, Long> {
    boolean existsByNumero(String numero);
    boolean existsByNumeroAndIdNot(String numero, Long id);
    List<Quarto> findBySituacaoOrderByAndarAscNumeroAsc(SituacaoQuarto situacao);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select q from Quarto q where q.id = :id")
    Optional<Quarto> buscarComBloqueio(@Param("id") Long id);
}
