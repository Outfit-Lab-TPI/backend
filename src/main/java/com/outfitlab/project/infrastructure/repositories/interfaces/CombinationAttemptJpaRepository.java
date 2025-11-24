package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.CombinationAttemptEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CombinationAttemptJpaRepository extends JpaRepository<CombinationAttemptEntity, Long> {
    List<CombinationAttemptEntity> findByCreatedAtAfter(LocalDateTime fromDate);

    List<CombinationAttemptEntity> findByCombination_PrendaSuperior_IdOrCombination_PrendaInferior_Id(Long prendaId, Long prendaId1);
}