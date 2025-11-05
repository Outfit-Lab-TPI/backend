package com.outfitlab.project.domain.model;

import java.time.LocalDateTime;

public class UserCombinationFavoriteModel {

    private String combinationUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserCombinationFavoriteModel(){}

    public UserCombinationFavoriteModel(String combinationUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.combinationUrl = combinationUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getCombinationUrl() {
        return combinationUrl;
    }

    public void setCombinationUrl(String combinationUrl) {
        this.combinationUrl = combinationUrl;
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
