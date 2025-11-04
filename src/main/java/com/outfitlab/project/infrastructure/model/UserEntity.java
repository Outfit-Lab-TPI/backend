package com.outfitlab.project.infrastructure.model;

import com.outfitlab.project.domain.model.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor // Required for JPA
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String satulation;
    private String name;
    private String secondName;
    private String lastName;
    private double years;
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_status")
    private SubscriptionStatus subscriptionStatus = SubscriptionStatus.FREE;

    @Column(name = "subscription_expires_at")
    private LocalDateTime subscriptionExpiresAt;

    public UserEntity(String name, String lastName, String email, String satulation, String secondName, double years) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.satulation = satulation;
        this.secondName = secondName;
        this.years = years;
        this.subscriptionStatus = SubscriptionStatus.FREE; // Default value
    }
}
