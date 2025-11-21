package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.interfaces.repositories.UserRepository;

public class ConvertToUser {

    private final UserRepository userRepository;

    public ConvertToUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String execute(String email){
        this.userRepository.convertToUser(email);
        return "El administrador se ha convertido a Usuario con éxito.";
    }
}
