package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.interfaces.repositories.UserRepository;

public class ConvertToAdmin {

    private final UserRepository userRepository;

    public ConvertToAdmin(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String execute(String email) {
        this.userRepository.convertToAdmin(email);
        return "El usuario se ha convertido a Administrador con éxito.";
    }
}
