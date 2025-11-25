package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.PrendaOcasionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PrendaOcacionJpaRepository extends JpaRepository<PrendaOcasionEntity, Long> {
    @Transactional
    @Modifying
    @Query("""
    DELETE FROM PrendaOcasionEntity po
    WHERE po.prenda.garmentCode = :garmentCode
    """)
    void deleteAllByGarmentCode(@Param("garmentCode") String garmentCode);
}
