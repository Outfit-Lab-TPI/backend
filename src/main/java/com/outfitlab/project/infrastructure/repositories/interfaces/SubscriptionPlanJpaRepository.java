package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.SubscriptionPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para SubscriptionPlanEntity.
 * Extiende JpaRepository para obtener métodos CRUD automáticos.
 */
public interface SubscriptionPlanJpaRepository extends JpaRepository<SubscriptionPlanEntity, Long> {
    
    /**
     * Busca todos los planes que están activos.
     */
    List<SubscriptionPlanEntity> findByIsActiveTrue();
    
    /**
     * Busca un plan por su código único.
     */
    Optional<SubscriptionPlanEntity> findByPlanCode(String planCode);
}
