package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.model.SubscriptionPlanModel;

import java.util.List;

/**
 * Puerto (interface) para repositorio de planes de suscripción.
 * Define las operaciones de persistencia para SubscriptionPlanModel.
 */
public interface SubscriptionPlanRepository {
    
    /**
     * Obtiene todos los planes activos.
     * 
     * @return Lista de planes con isActive = true
     */
    List<SubscriptionPlanModel> findAllActive();
    
    /**
     * Busca un plan por su código único.
     * 
     * @param planCode Código del plan (FREE, PREMIUM_MONTHLY, PREMIUM_YEARLY)
     * @return Modelo del plan o null si no existe
     */
    SubscriptionPlanModel findByPlanCode(String planCode);
}
