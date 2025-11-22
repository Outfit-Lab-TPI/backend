package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String userEmail);

    Optional<UserEntity> getByEmail(String userEmail);

    UserEntity findByVerificationToken(String token);

    UserEntity findByBrand_BrandCode(String brandCode);
}
