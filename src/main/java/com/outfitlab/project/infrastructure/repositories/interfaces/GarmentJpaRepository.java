package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.PrendaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GarmentJpaRepository extends JpaRepository<PrendaEntity, Long> {
    PrendaEntity findByGarmentCode(String garmentCode);
}
