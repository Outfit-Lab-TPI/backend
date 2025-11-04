package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.model.UserModel;

import java.util.Optional;

public interface UserRepository {
    Optional<UserModel> findById(Long id);
    Optional<UserModel> findByEmail(String email);
    UserModel save(UserModel userModel);
}
