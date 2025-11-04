package com.outfitlab.project.presentation.dto;

import com.outfitlab.project.domain.model.enums.SubscriptionStatus;

import java.time.LocalDateTime;

public class SubscriptionStatusResponse {
    private Long userId;
    private SubscriptionStatus status;
    private LocalDateTime expiresAt;
    private int maxFavorites;
    private int currentFavorites;

    public SubscriptionStatusResponse() {
    }

    public SubscriptionStatusResponse(Long userId, SubscriptionStatus status, LocalDateTime expiresAt, int maxFavorites, int currentFavorites) {
        this.userId = userId;
        this.status = status;
        this.expiresAt = expiresAt;
        this.maxFavorites = maxFavorites;
        this.currentFavorites = currentFavorites;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public int getMaxFavorites() {
        return maxFavorites;
    }

    public void setMaxFavorites(int maxFavorites) {
        this.maxFavorites = maxFavorites;
    }

    public int getCurrentFavorites() {
        return currentFavorites;
    }

    public void setCurrentFavorites(int currentFavorites) {
        this.currentFavorites = currentFavorites;
    }
}
