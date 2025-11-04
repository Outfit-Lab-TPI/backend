package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;

public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository UserJpaRepository) {
        this.userJpaRepository = UserJpaRepository;
    }

    @Override
    public UserModel findUserByEmail(String userEmail) throws UserNotFoundException {
        UserEntity entity = userJpaRepository.findByEmail(userEmail);
        if (entity == null) throw new UserNotFoundException("No encontramos el usuario con el email: " + userEmail);
        return UserEntity.convertEntityToModel(entity);
    }
}
