package com.outfitlab.project.domain.model;

import org.apache.catalina.User;

import java.time.LocalDateTime;

public class CombinationAttemptModel {
    private UserModel user;
    private CombinationModel combination;
    private LocalDateTime createdAt;
    private String imageUrl;

    public CombinationAttemptModel() {}

    public CombinationAttemptModel(UserModel user, CombinationModel combination, LocalDateTime createdAt, String imageUrl) {
        this.user = user;
        this.combination = combination;
        this.createdAt = createdAt;
        this.imageUrl = imageUrl;
    }

    public UserModel getUser() { return user; }
    public CombinationModel getCombination() { return combination; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getImageUrl() { return imageUrl; }
}
