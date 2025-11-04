package com.outfitlab.project.infrastructure.model;

import com.outfitlab.project.domain.model.UserModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String satulation;
    private String name;
    private String secondName;
    private String lastName;
    private Integer years;
    private String email;
    private String password;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public UserEntity() {}

    public UserEntity(String name, String lastName, String email, String satulation, String secondName, Integer years, String password) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.satulation = satulation;
        this.secondName = secondName;
        this.years = years;
        this.password = password;
    }

    public static UserModel convertEntityToModel(UserEntity entity) {
        return new UserModel(
                entity.getName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getSatulation(),
                entity.getSecondName(),
                entity.getYears(),
                entity.getPassword(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static UserEntity convertModelToEntity(UserModel model) {
        return new UserEntity(
                model.getName(),
                model.getLastName(),
                model.getEmail(),
                model.getSatulation(),
                model.getSecondName(),
                model.getYears(),
                model.getPassword()
        );
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
