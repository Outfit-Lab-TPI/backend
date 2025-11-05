package com.outfitlab.project.infrastructure.model;

import com.outfitlab.project.domain.model.UserFavoriteCombinationModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidad JPA para combinaciones favoritas de usuario.
 * Relaciona un usuario con una combinación de prendas (GarmentRecomendation).
 */
@Entity
@Table(name = "user_favorite_combinations")
@Getter
@Setter
public class UserFavoriteCombinationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garment_recomendation_id", nullable = false)
    private GarmentRecomendationEntity garmentRecomendation;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // Constructor vacío (requerido por JPA)
    public UserFavoriteCombinationEntity() {
    }

    // Constructor con parámetros esenciales
    public UserFavoriteCombinationEntity(UserEntity user, GarmentRecomendationEntity garmentRecomendation) {
        this.user = user;
        this.garmentRecomendation = garmentRecomendation;
        this.isActive = true;
    }

    /**
     * Convierte la entidad JPA a modelo de dominio
     */
    public static UserFavoriteCombinationModel toModel(UserFavoriteCombinationEntity entity) {
        if (entity == null) {
            return null;
        }
        
        UserFavoriteCombinationModel model = new UserFavoriteCombinationModel();
        model.setId(entity.getId());
        
        // Convertir entidades completas a modelos
        if (entity.getUser() != null) {
            model.setUser(UserEntity.toModel(entity.getUser()));
        }
        
        if (entity.getGarmentRecomendation() != null) {
            model.setGarmentRecomendation(GarmentRecomendationEntity.convertToModel(entity.getGarmentRecomendation()));
        }
        
        model.setCreatedAt(entity.getCreatedAt());
        model.setIsActive(entity.getIsActive());
        
        return model;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    /**
     * Método helper para obtener isActive (compatible con getter booleano)
     */
    public Boolean getIsActive() {
        return isActive;
    }
}
