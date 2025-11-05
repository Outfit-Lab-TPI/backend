package com.outfitlab.project.domain.model;

import java.time.LocalDateTime;

public class UserFavoriteCombinationModel {
    private Long id;
    private UserModel user;
    private GarmentRecomendationModel garmentRecomendation;
    private LocalDateTime createdAt;
    private Boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
    
    // Helper para infraestructura
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public void setUserId(Long userId) {
        if (this.user == null) {
            this.user = new UserModel("", "", "", "", "", 0, "", null, null);
        }
        this.user.setId(userId);
    }

    public GarmentRecomendationModel getGarmentRecomendation() {
        return garmentRecomendation;
    }

    public void setGarmentRecomendation(GarmentRecomendationModel garmentRecomendation) {
        this.garmentRecomendation = garmentRecomendation;
    }
    
    // Helper para infraestructura
    public Long getGarmentRecomendationId() {
        return garmentRecomendation != null ? garmentRecomendation.getId() : null;
    }

    public void setGarmentRecomendationId(Long garmentRecomendationId) {
        if (this.garmentRecomendation == null) {
            this.garmentRecomendation = new GarmentRecomendationModel();
        }
        this.garmentRecomendation.setId(garmentRecomendationId);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
