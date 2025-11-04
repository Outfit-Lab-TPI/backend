package com.outfitlab.project.domain.model;

import java.time.LocalDateTime;

public class UserFavoriteCombinationModel {

    private Long id;
    private UserModel user;
    private GarmentRecomendationModel garmentRecomendation;
    private LocalDateTime createdAt;
    private boolean isActive;

    public UserFavoriteCombinationModel() {
    }

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

    public GarmentRecomendationModel getGarmentRecomendation() {
        return garmentRecomendation;
    }

    public void setGarmentRecomendation(GarmentRecomendationModel garmentRecomendation) {
        this.garmentRecomendation = garmentRecomendation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
