package com.outfitlab.project.domain.model;

import java.time.LocalDateTime;

public class UserGarmentFavoriteModel {

    private UserModel user;
    private PrendaModel garment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserGarmentFavoriteModel(UserModel user, PrendaModel garment, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.user = user;
        this.garment = garment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public PrendaModel getGarment() {
        return garment;
    }

    public void setGarment(PrendaModel garment) {
        this.garment = garment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
