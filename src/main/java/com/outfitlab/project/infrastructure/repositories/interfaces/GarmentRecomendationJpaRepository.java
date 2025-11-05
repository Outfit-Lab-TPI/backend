package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.GarmentRecomendationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para GarmentRecomendationEntity.
 * Extiende JpaRepository para obtener métodos CRUD automáticos.
 */
public interface GarmentRecomendationJpaRepository extends JpaRepository<GarmentRecomendationEntity, Long> {
}
