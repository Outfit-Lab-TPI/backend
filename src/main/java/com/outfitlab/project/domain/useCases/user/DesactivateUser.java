package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.interfaces.repositories.UserRepository;

public class DesactivateUser {

    private final UserRepository userRepository;

    public DesactivateUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String execute(String email) {
        this.userRepository.desactivateUser(email);
        return "Usuario desactivado con éxito.";
    }
}
