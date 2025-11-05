package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;

public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public UserModel findUserByEmail(String userEmail) throws UserNotFoundException {
        UserEntity entity = userJpaRepository.findByEmail(userEmail);
        if (entity == null) {
            throw new UserNotFoundException("No encontramos el usuario con el email: " + userEmail);
        }
        return UserEntity.toModel(entity);
    }

    @Override
    public UserModel findByEmail(String email) throws UserNotFoundException {
        return findUserByEmail(email);
    }

    @Override
    public UserModel findById(Long id) {
        return userJpaRepository.findById(id)
                .map(UserEntity::toModel)
                .orElse(null);
    }

    @Override
    public UserModel save(UserModel user) {
        UserEntity entity = UserEntity.toEntity(user);
        UserEntity savedEntity = userJpaRepository.save(entity);
        return UserEntity.toModel(savedEntity);
    }
}
