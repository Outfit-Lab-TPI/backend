package com.outfitlab.project.domain.model.dto;

import com.outfitlab.project.domain.enums.Role;
import com.outfitlab.project.domain.model.BrandModel;

public class UserWithBrandsDTO {

    private String email;
    private String name;
    private Role role;
    private boolean status;
    private boolean verified;
    private BrandModel brand;
    private boolean brandApproved;
    private String userImageUrl;
    private String lastname;

    public UserWithBrandsDTO() {}

    public UserWithBrandsDTO(String email, String name, Role role, boolean status, boolean verified, boolean brandApproved, String userImageUrl, BrandModel brandModel) {
        this.email = email;
        this.name = name;
        this.role = role;
        this.status = status;
        this.verified = verified;
        this.brand = brandModel;
        this.userImageUrl = userImageUrl;
        this.brandApproved = brandApproved;
    }

    public UserWithBrandsDTO(String email, String lastName, String name, Role role, boolean status, boolean verified, boolean brandApproved, String userImageUrl, BrandModel brandModel) {
        this.email = email;
        this.name = name;
        this.role = role;
        this.status = status;
        this.verified = verified;
        this.brand = brandModel;
        this.userImageUrl = userImageUrl;
        this.brandApproved = brandApproved;
        this.lastname = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isBrandApproved() {
        return brandApproved;
    }

    public void setBrandApproved(boolean brandApproved) {
        this.brandApproved = brandApproved;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public BrandModel getBrand() {
        return brand;
    }

    public void setBrand(BrandModel brand) {
        this.brand = brand;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
