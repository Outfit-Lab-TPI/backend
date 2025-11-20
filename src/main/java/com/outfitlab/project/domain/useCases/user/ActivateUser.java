package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.interfaces.repositories.UserRepository;

public class ActivateUser {
    private final UserRepository userRepository;

    public ActivateUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String execute(String email) {
        this.userRepository.activateUser(email);
        return "Usuario activado con éxito.";
    }
}
