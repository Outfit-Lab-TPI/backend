package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.CombinationAttemptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface CombinationAttemptJpaRepository extends JpaRepository<CombinationAttemptEntity, Long> {
    List<CombinationAttemptEntity> findByCreatedAtAfter(LocalDateTime fromDate);

    List<CombinationAttemptEntity> findByCombination_PrendaSuperior_IdOrCombination_PrendaInferior_Id(Long prendaId, Long prendaId1);

    @Transactional
    @Modifying
    @Query("""
    DELETE FROM CombinationAttemptEntity a
    WHERE a.combination.prendaSuperior.garmentCode = :garmentCode
       OR a.combination.prendaInferior.garmentCode = :garmentCode
""")
    void deleteAllAttemptsByGarmentCode(@Param("garmentCode") String garmentCode);

}