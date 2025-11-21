package com.outfitlab.project.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserSubscriptionModel {
    private Long id;
    private String userEmail;
    private String planCode;
    
    // Contadores de uso
    private int combinationsUsed;      // Total creadas en el período
    private int favoritesCount;        // Favoritos actuales en BD
    private int modelsGenerated;       // Total generados en el período
    
    // Límites del plan (denormalizados para performance)
    private Integer maxCombinations;   // null = ilimitado
    private Integer maxFavorites;      // null = ilimitado
    private Integer maxModels;         // null = ilimitado
    
    // Metadata
    private LocalDateTime subscriptionStart;
    private LocalDateTime subscriptionEnd;
    private String status;  // ACTIVE, CANCELLED, EXPIRED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserSubscriptionModel() {}
}
