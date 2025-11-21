package com.outfitlab.project.domain.model;

import com.outfitlab.project.infrastructure.config.security.Role;

import java.time.LocalDateTime;

public class UserModel {
    private String satulation;
    private String name;
    private String secondName;
    private String lastName;
    private Integer years;
    private String email;
    private String hashedPassword;
    private String verificationToken;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Role role;
    private boolean verified;
    private boolean status;

    public UserModel(String email, String name, String lastName, String hashedPassword, String verificationToken) {
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.hashedPassword = hashedPassword;
        this.verificationToken = verificationToken;
    }

    public UserModel(String name, String lastName, String email, String satulation, String secondName, Integer years, String hashedPassword, LocalDateTime createdAt, LocalDateTime updatedAt, Role role, boolean verified, boolean status, String verificationToken) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.satulation = satulation;
        this.secondName = secondName;
        this.years = years;
        this.hashedPassword = hashedPassword;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.verified = verified;
        this.role = role;
        this.verificationToken = verificationToken;
    }

    /*public String getPassword() {
        return "";
    }*/

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

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
