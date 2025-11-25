package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.CombinationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CombinationJpaRepository extends JpaRepository<CombinationEntity, Long> {

    @Query("""
        SELECT c
        FROM CombinationEntity c
        WHERE c.prendaSuperior.id = :prendaSuperiorId
          AND c.prendaInferior.id = :prendaInferiorId
    """)
    Optional<CombinationEntity> findByPrendas(Long prendaSuperiorId, Long prendaInferiorId);

    @Transactional
    @Modifying
    @Query("""
    DELETE FROM CombinationEntity c
    WHERE c.prendaSuperior.garmentCode = :garmentCode
       OR c.prendaInferior.garmentCode = :garmentCode
""")
    void deleteAllByGarmentCode(@Param("garmentCode") String garmentCode);
}
