package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.GarmentRecomendationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecomendationJpaRepository extends JpaRepository<GarmentRecomendationEntity, Long> {

    List<GarmentRecomendationEntity> findByTopGarment(Long garmentId);
    List<GarmentRecomendationEntity> findByBottomGarment(Long garmentId);

}
