package com.outfitlab.project.infrastructure.model;

import com.outfitlab.project.domain.model.UserCombinationFavoriteModel;
import com.outfitlab.project.domain.model.UserGarmentFavoriteModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserCombinationFavoriteEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    private String combinationUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public UserCombinationFavoriteEntity(UserEntity user, String combinationUrl) {
        this.user = user;
        this.combinationUrl = combinationUrl;
    }

    public static UserCombinationFavoriteModel convertEntityToModelWithoutUser(UserCombinationFavoriteEntity entity) {
        return new UserCombinationFavoriteModel(
                entity.getCombinationUrl(),
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
