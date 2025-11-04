package com.outfitlab.project.domain.model.enums;

public enum SubscriptionStatus {
    FREE,           // Usuario gratuito (máximo 2 favoritos)
    PREMIUM_ACTIVE, // Usuario premium activo (máximo 10+ favoritos)
    PREMIUM_EXPIRED // Usuario premium vencido (vuelve a límite gratuito)
}