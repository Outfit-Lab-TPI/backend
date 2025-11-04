package com.outfitlab.project.infrastructure.repositories.impl;

import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<UserModel> findById(Long id) {
        return userJpaRepository.findById(id).map(this::toModel);
    }

    @Override
    public Optional<UserModel> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(this::toModel);
    }

    @Override
    public UserModel save(UserModel userModel) {
        UserEntity userEntity = toEntity(userModel);
        UserEntity savedEntity = userJpaRepository.save(userEntity);
        return toModel(savedEntity);
    }

    private UserModel toModel(UserEntity userEntity) {
        return new UserModel(
                userEntity.getId(),
                userEntity.getSatulation(),
                userEntity.getName(),
                userEntity.getSecondName(),
                userEntity.getLastName(),
                userEntity.getYears(),
                userEntity.getEmail(),
                userEntity.getSubscriptionStatus(),
                userEntity.getSubscriptionExpiresAt()
        );
    }

    private UserEntity toEntity(UserModel userModel) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userModel.getId());
        userEntity.setSatulation(userModel.getSatulation());
        userEntity.setName(userModel.getName());
        userEntity.setSecondName(userModel.getSecondName());
        userEntity.setLastName(userModel.getLastName());
        userEntity.setYears(userModel.getYears());
        userEntity.setEmail(userModel.getEmail());
        userEntity.setSubscriptionStatus(userModel.getSubscriptionStatus());
        userEntity.setSubscriptionExpiresAt(userModel.getSubscriptionExpiresAt());
        return userEntity;
    }
}
