package com.outfitlab.project.presentation.dto;

import com.outfitlab.project.domain.model.enums.SubscriptionStatus;

import java.time.LocalDateTime;

/**
 * DTO para respuesta del estado de suscripción.
 * Contiene información sobre la suscripción actual y límites de favoritos.
 */
public class SubscriptionStatusResponse {
    private SubscriptionStatus status;
    private LocalDateTime expiresAt;
    private int currentFavorites;
    private int maxFavorites;
    private int remainingFavorites;

    public SubscriptionStatusResponse() {
    }

    public SubscriptionStatusResponse(SubscriptionStatus status, LocalDateTime expiresAt,
                                        int currentFavorites, int maxFavorites, int remainingFavorites) {
        this.status = status;
        this.expiresAt = expiresAt;
        this.currentFavorites = currentFavorites;
        this.maxFavorites = maxFavorites;
        this.remainingFavorites = remainingFavorites;
    }

    // Getters y Setters
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

    public int getCurrentFavorites() {
        return currentFavorites;
    }

    public void setCurrentFavorites(int currentFavorites) {
        this.currentFavorites = currentFavorites;
    }

    public int getMaxFavorites() {
        return maxFavorites;
    }

    public void setMaxFavorites(int maxFavorites) {
        this.maxFavorites = maxFavorites;
    }

    public int getRemainingFavorites() {
        return remainingFavorites;
    }

    public void setRemainingFavorites(int remainingFavorites) {
        this.remainingFavorites = remainingFavorites;
    }
}
