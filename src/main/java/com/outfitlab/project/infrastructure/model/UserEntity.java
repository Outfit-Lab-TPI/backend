package com.outfitlab.project.infrastructure.model;

import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.domain.model.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String satulation;
    private String name;
    private String secondName;
    private String lastName;
    private Integer years;
    private String email;
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_status", nullable = false)
    private SubscriptionStatus subscriptionStatus = SubscriptionStatus.FREE;
    
    @Column(name = "subscription_expires_at")
    private LocalDateTime subscriptionExpiresAt;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructores
    public UserEntity() {}
    
    public UserEntity(String name, String lastName, String email, String satulation, 
                      String secondName, Integer years, String password) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.satulation = satulation;
        this.secondName = secondName;
        this.years = years;
        this.password = password;
        this.subscriptionStatus = SubscriptionStatus.FREE;
    }
    
    // Conversión entity -> model
    public static UserModel toModel(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        
        UserModel model = new UserModel(
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
        
        model.setId(entity.getId());
        model.setSubscriptionStatus(entity.getSubscriptionStatus());
        model.setSubscriptionExpiresAt(entity.getSubscriptionExpiresAt());
        
        return model;
    }
    
    // Conversión model -> entity
    public static UserEntity toEntity(UserModel model) {
        if (model == null) {
            return null;
        }
        
        UserEntity entity = new UserEntity(
            model.getName(),
            model.getLastName(),
            model.getEmail(),
            model.getSatulation(),
            model.getSecondName(),
            model.getYears(),
            model.getPassword()
        );
        
        entity.setId(model.getId());
        entity.setSubscriptionStatus(model.getSubscriptionStatus());
        entity.setSubscriptionExpiresAt(model.getSubscriptionExpiresAt());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        
        return entity;
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (subscriptionStatus == null) {
            subscriptionStatus = SubscriptionStatus.FREE;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
