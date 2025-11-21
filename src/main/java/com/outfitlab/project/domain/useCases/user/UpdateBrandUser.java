package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.interfaces.repositories.UserRepository;

public class UpdateBrandUser {

    private final UserRepository userRepository;

    public UpdateBrandUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(String userEmail, String brandCode) {
        this.userRepository.updateBrandUser(userEmail, brandCode);
    }
}
