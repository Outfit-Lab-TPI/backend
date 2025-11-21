package com.outfitlab.project.infrastructure.model;

import com.outfitlab.project.domain.model.UserSubscriptionModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_subscription")
@Getter
@Setter
public class UserSubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    private SubscriptionEntity subscription;
    
    // Contadores de uso
    @Column(name = "combinations_used", nullable = false)
    private int combinationsUsed = 0;
    
    @Column(name = "favorites_count", nullable = false)
    private int favoritesCount = 0;
    
    @Column(name = "models_generated", nullable = false)
    private int modelsGenerated = 0;
    
    // Límites denormalizados (cache del plan)
    @Column(name = "max_combinations")
    private Integer maxCombinations;
    
    @Column(name = "max_favorites")
    private Integer maxFavorites;
    
    @Column(name = "max_models")
    private Integer maxModels;
    
    // Metadata
    @Column(name = "subscription_start", nullable = false)
    private LocalDateTime subscriptionStart;
    
    @Column(name = "subscription_end")
    private LocalDateTime subscriptionEnd;
    
    @Column(nullable = false, length = 20)
    private String status = "ACTIVE";  // ACTIVE, CANCELLED, EXPIRED
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public static UserSubscriptionModel convertToModel(UserSubscriptionEntity entity) {
        UserSubscriptionModel model = new UserSubscriptionModel();
        model.setId(entity.getId());
        model.setUserEmail(entity.getUser().getEmail());
        model.setPlanCode(entity.getSubscription().getPlanCode());
        model.setCombinationsUsed(entity.getCombinationsUsed());
        model.setFavoritesCount(entity.getFavoritesCount());
        model.setModelsGenerated(entity.getModelsGenerated());
        model.setMaxCombinations(entity.getMaxCombinations());
        model.setMaxFavorites(entity.getMaxFavorites());
        model.setMaxModels(entity.getMaxModels());
        model.setSubscriptionStart(entity.getSubscriptionStart());
        model.setSubscriptionEnd(entity.getSubscriptionEnd());
        model.setStatus(entity.getStatus());
        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());
        return model;
    }
}
