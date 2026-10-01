package com.auramed.auramed.repository;

import com.auramed.auramed.model.ProfissionalSaude;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface ProfissionalSaudeRepository extends JpaRepository<ProfissionalSaude, Long> {
    boolean existsByRegistroProfissional(String registroProfissional);
    boolean existsByRegistroProfissionalAndIdNot(String registroProfissional, Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from ProfissionalSaude p where p.id = :id")
    Optional<ProfissionalSaude> buscarComBloqueio(@Param("id") Long id);
}
