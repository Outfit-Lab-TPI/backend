package com.outfitlab.project.domain.model;

import com.outfitlab.project.domain.model.enums.SubscriptionStatus;
import java.time.LocalDateTime;

public class UserModel {
    private Long id;
    private String satulation;
    private String name;
    private String secondName;
    private String lastName;
    private Integer years;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Nuevos campos para sistema de suscripciones
    private SubscriptionStatus subscriptionStatus;
    private LocalDateTime subscriptionExpiresAt;

    public UserModel(String name, String lastName, String email, String satulation, String secondName, Integer years, String password, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.satulation = satulation;
        this.secondName = secondName;
        this.years = years;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.subscriptionStatus = SubscriptionStatus.FREE; // Default
    }

    // GETTERS Y SETTERS
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSatulation() {
        return satulation;
    }

    public void setSatulation(String satulation) {
        this.satulation = satulation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getYears() {
        return years;
    }

    public void setYears(Integer years) {
        this.years = years;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    // NUEVOS GETTERS/SETTERS PARA SUSCRIPCIÓN
    
    public SubscriptionStatus getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(SubscriptionStatus subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public LocalDateTime getSubscriptionExpiresAt() {
        return subscriptionExpiresAt;
    }

    public void setSubscriptionExpiresAt(LocalDateTime subscriptionExpiresAt) {
        this.subscriptionExpiresAt = subscriptionExpiresAt;
    }
}
