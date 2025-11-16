package com.outfitlab.project.domain.model;

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserModel(String email, String name, String lastName, String hashedPassword) {
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.hashedPassword = hashedPassword;
    }

    public UserModel(String name, String lastName, String email, String satulation, String secondName, Integer years, String hashedPassword, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.satulation = satulation;
        this.secondName = secondName;
        this.years = years;
        this.hashedPassword = hashedPassword;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getPassword() {
        return "";
    }
}
