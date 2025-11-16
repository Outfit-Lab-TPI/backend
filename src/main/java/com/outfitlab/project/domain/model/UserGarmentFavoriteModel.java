package com.outfitlab.project.domain.model;

import java.time.LocalDateTime;

public class UserGarmentFavoriteModel {

    private PrendaModel garment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserGarmentFavoriteModel() {}

    public UserGarmentFavoriteModel(PrendaModel garment, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.garment = garment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
