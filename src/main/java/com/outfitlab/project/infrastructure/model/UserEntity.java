package com.outfitlab.project.infrastructure.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.domain.enums.Role;
import com.outfitlab.project.domain.model.dto.UserWithBrandsDTO;
import com.outfitlab.project.infrastructure.config.security.jwt.Token;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String password;
    private String satulation;
    private String secondName;
    private String lastName;
    private Integer years;
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Token> tokens;

    @Column(unique = true)
    private String verificationToken;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean status;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean verified;

    @OneToOne
    @JoinColumn(name = "marca_id")
    private MarcaEntity brand;

    private boolean brandApproved;
    private String userImageUrl;

    public UserEntity() {
    }

    public UserEntity(String name, String lastName, String email, String satulation, String secondName, Integer years,
            String password) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.satulation = satulation;
        this.secondName = secondName;
        this.years = years;
        this.password = password;
    }

    public UserEntity(UserModel model) {
        this.email = model.getEmail();
        this.name = model.getName();
        this.lastName = model.getLastName();
        this.password = model.getHashedPassword();
        this.verificationToken = model.getVerificationToken();
        this.verified = false;
    }

    public UserEntity(String name, String lastName, String email, String satulation, String secondName, Integer years,
            String hashedPassword, String userImageUrl) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.satulation = satulation;
        this.secondName = secondName;
        this.years = years;
        this.password = hashedPassword;
        this.userImageUrl = userImageUrl;
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
                entity.getUpdatedAt(),
                entity.getRole(),
                entity.isVerified(),
                entity.isStatus(),
                entity.getVerificationToken(),
                entity.getUserImageUrl(),
                MarcaEntity.convertToModelWithoutPrendas(entity.getBrand())
        );
    }

    public static UserModel convertEntityUserOrAdminToModel(UserEntity entity) {
        return new UserModel(
                entity.getName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getSatulation(),
                entity.getSecondName(),
                entity.getYears(),
                entity.getPassword(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getRole(),
                entity.isVerified(),
                entity.isStatus(),
                entity.getVerificationToken(),
                entity.getUserImageUrl()
        );
    }

    public static UserModel convertEntityToModelWithId(UserEntity entity) {
        return new UserModel(
                entity.getId(),
                entity.getName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getSatulation(),
                entity.getSecondName(),
                entity.getYears(),
                entity.getPassword(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getRole(),
                entity.isVerified(),
                entity.isStatus(),
                entity.getVerificationToken(),
                entity.getUserImageUrl());
    }

    public static UserEntity convertModelToEntity(UserModel model) {
        return new UserEntity(
                model.getName(),
                model.getLastName(),
                model.getEmail(),
                model.getSatulation(),
                model.getSecondName(),
                model.getYears(),
                model.getHashedPassword(),
                model.getUserImg());
    }

    public static UserWithBrandsDTO convertEntityToModelWithBrand(UserEntity entity) {
        return new UserWithBrandsDTO(
                entity.getEmail(),
                entity.getLastName(),
                entity.getName(),
                entity.getRole(),
                entity.isStatus(),
                entity.isVerified(),
                entity.isBrandApproved(),
                entity.getUserImageUrl(),
                MarcaEntity.convertToModelWithoutPrendas(entity.getBrand()));
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name().toString()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }
}
