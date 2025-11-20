package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import static com.outfitlab.project.infrastructure.config.security.Role.ADMIN;
import static com.outfitlab.project.infrastructure.config.security.Role.USER;

import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository UserJpaRepository) {
        this.userJpaRepository = UserJpaRepository;
    }

    @Override
    public UserModel findUserByEmail(String userEmail) throws UserNotFoundException {
        UserEntity entity = getUserByEmail(userEmail);
        if (entity == null) throw new UserNotFoundException("No encontramos el usuario con el email: " + userEmail);
        return UserEntity.convertEntityToModel(entity);
    }

    @Override
    public UserModel saveUser(UserModel userModel) {
        UserEntity entityToSave = new UserEntity(userModel);
        UserEntity savedEntity = userJpaRepository.save(entityToSave);
        return UserEntity.convertEntityToModel(savedEntity);
    }

    @Override
    public List<UserModel> findAll() {
        List<UserModel> users = this.userJpaRepository.findAll()
                .stream().map(UserEntity::convertEntityToModel)
                .toList();
        if (users.isEmpty()) throw new UserNotFoundException("No encontramos usuarios.");
        return users;
    }

    @Override
    public void desactivateUser(String email) {
        UserEntity entity =  getUserByEmail(email);
        if (entity == null) throw userNotFoundException();
        entity.setStatus(false);
        this.userJpaRepository.save(entity);
    }

    @Override
    public void activateUser(String email) {
        UserEntity entity =  getUserByEmail(email);
        if (entity == null) throw userNotFoundException();
        entity.setStatus(true);
        this.userJpaRepository.save(entity);
    }

    @Override
    public void convertToAdmin(String email) {
        UserEntity user = getUserByEmail(email);
        if (user == null) throw userNotFoundException();
        user.setRole(ADMIN);
        this.userJpaRepository.save(user);
    }

    @Override
    public void convertToUser(String email) {
        UserEntity user = getUserByEmail(email);
        if (user == null) throw userNotFoundException();
        user.setRole(USER);
        this.userJpaRepository.save(user);
    }

    private UserEntity getUserByEmail(String email) {
        return this.userJpaRepository.findByEmail(email);
    }

    private UserNotFoundException userNotFoundException() {
        return new UserNotFoundException("No encontramos el usuario.");
    }
}
