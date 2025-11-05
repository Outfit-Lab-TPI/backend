package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.model.UserSubscriptionModel;

import java.util.List;

/**
 * Puerto (interface) para repositorio de suscripciones de usuario.
 * Define las operaciones de persistencia para UserSubscriptionModel.
 */
public interface UserSubscriptionRepository {
    
    /**
     * Guarda o actualiza una suscripción de usuario.
     * 
     * @param model Modelo de suscripción a guardar
     * @return Modelo guardado con ID asignado
     */
    UserSubscriptionModel save(UserSubscriptionModel model);
    
    /**
     * Busca la suscripción activa de un usuario.
     * 
     * @param userId ID del usuario
     * @return Modelo de suscripción activa o null si no tiene
     */
    UserSubscriptionModel findActiveByUserId(Long userId);
    
    /**
     * Busca todas las suscripciones que han vencido.
     * Usado para tareas de mantenimiento (cambiar PREMIUM_ACTIVE a PREMIUM_EXPIRED).
     * 
     * @return Lista de suscripciones vencidas
     */
    List<UserSubscriptionModel> findExpiredSubscriptions();
}
