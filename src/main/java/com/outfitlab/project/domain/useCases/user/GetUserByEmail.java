package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.UserModel;

public class GetUserByEmail {
    private final UserRepository userRepository;

    public GetUserByEmail(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserModel execute(String email) {
        return this.userRepository.findUserByEmail(email);
    }
}
