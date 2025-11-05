package com.outfitlab.project.domain.model.enums;

/**
 * Estado de la suscripción del usuario.
 * Define los límites de favoritos según el tipo de plan.
 */
public enum SubscriptionStatus {
    /**
     * Usuario gratuito - Máximo 2 combinaciones favoritas
     */
    FREE,
    
    /**
     * Usuario premium con suscripción activa - Máximo 20 combinaciones favoritas
     */
    PREMIUM_ACTIVE,
    
    /**
     * Usuario premium con suscripción vencida - Vuelve a máximo 2 favoritos
     * (No se eliminan los favoritos existentes, pero no puede agregar más)
     */
    PREMIUM_EXPIRED
}
