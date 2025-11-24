package com.outfitlab.project.domain.model;

import com.outfitlab.project.domain.enums.Role;

import java.time.LocalDateTime;

public class UserModel {
    private Long id;
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
    private String userImg; // ← Unificado: develop usa userImg

    private Role role;
    private boolean verified;
    private boolean status;
    private BrandModel brand; // ← De develop

    public UserModel(String email, String name, String lastName, String hashedPassword, String verificationToken) {
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.hashedPassword = hashedPassword;
        this.verificationToken = verificationToken;
    }

    // Constructor completo
    public UserModel(String name, String lastName, String email, String satulation, String secondName, Integer years,
            String hashedPassword, LocalDateTime createdAt, LocalDateTime updatedAt, Role role, boolean verified,
            boolean status, String verificationToken, String userImg) {
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
        this.userImg = userImg;
    }

    // Constructor con brand (de develop)
    public UserModel(String name, String lastName, String email, Role role, boolean verified, boolean status,
            String userImg, BrandModel brand) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.verified = verified;
        this.status = status;
        this.userImg = userImg;
        this.brand = brand;
    }

    /*
     * public String getPassword() {
     * return "";
     * }
     */

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

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public BrandModel getBrand() {
        return brand;
    }

    public void setBrand(BrandModel brand) {
        this.brand = brand;
    }

    public Long getId() {return id;}
}
