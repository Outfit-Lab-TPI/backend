package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;

public class VerifyEmail {

    private final UserRepository userRepository;
    private final UserJpaRepository userJpaRepository;

    public VerifyEmail(UserRepository userRepository, UserJpaRepository userJpaRepository) {
        this.userRepository = userRepository;
        this.userJpaRepository = userJpaRepository;
    }

    public void execute(String token) throws UserNotFoundException {

        UserModel userModel = userRepository.findUserByVerificationToken(token);
        UserEntity userEntity = userJpaRepository.findByEmail(userModel.getEmail());
        userEntity.setVerified(true);
        userEntity.setVerificationToken(null);
        userJpaRepository.save(userEntity);
    }

}
