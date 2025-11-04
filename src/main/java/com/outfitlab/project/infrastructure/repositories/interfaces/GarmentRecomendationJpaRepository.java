package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.GarmentRecomendationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GarmentRecomendationJpaRepository extends JpaRepository<GarmentRecomendationEntity, Long> {
}
