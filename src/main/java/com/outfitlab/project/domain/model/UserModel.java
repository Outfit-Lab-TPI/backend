package com.outfitlab.project.domain.model;

import com.outfitlab.project.domain.model.enums.SubscriptionStatus;

import java.time.LocalDateTime;

public class UserModel {
    private Long id;
    private String satulation;
    private String name;
    private String secondName;
    private String lastName;
    private double years;
    private String email;
    private SubscriptionStatus subscriptionStatus;
    private LocalDateTime subscriptionExpiresAt;

    public UserModel() {
    }

    public UserModel(Long id, String satulation, String name, String secondName, String lastName, double years, String email, SubscriptionStatus subscriptionStatus, LocalDateTime subscriptionExpiresAt) {
        this.id = id;
        this.satulation = satulation;
        this.name = name;
        this.secondName = secondName;
        this.lastName = lastName;
        this.years = years;
        this.email = email;
        this.subscriptionStatus = subscriptionStatus;
        this.subscriptionExpiresAt = subscriptionExpiresAt;
    }

    // Constructor compatible con UserService (sin subscriptions)
    public UserModel(String name, String lastName, String email, String satulation, String secondName, double years) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.satulation = satulation;
        this.secondName = secondName;
        this.years = years;
        this.subscriptionStatus = SubscriptionStatus.FREE; // Default
        this.subscriptionExpiresAt = null;
    }

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

    public double getYears() {
        return years;
    }

    public void setYears(double years) {
        this.years = years;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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
