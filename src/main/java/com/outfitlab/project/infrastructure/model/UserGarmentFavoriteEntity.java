package com.outfitlab.project.infrastructure.model;

import com.outfitlab.project.domain.model.UserGarmentFavoriteModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserGarmentFavoriteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garment_id", nullable = false)
    private PrendaEntity garment;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public UserGarmentFavoriteEntity(UserEntity user, PrendaEntity garment) {
        this.user = user;
        this.garment = garment;
    }

    public static UserGarmentFavoriteModel convertEntityToModelWithoutUser(UserGarmentFavoriteEntity entity) {
        return new UserGarmentFavoriteModel(
                PrendaEntity.convertToModel(entity.getGarment()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
