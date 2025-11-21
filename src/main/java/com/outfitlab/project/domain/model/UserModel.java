package com.outfitlab.project.domain.model;

import com.outfitlab.project.infrastructure.config.security.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
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

    public String getPassword() {
        return "";
    }
}
