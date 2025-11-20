package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.presentation.dto.UserDTO;

import java.util.List;

public class GetAllUsers {

    private final UserRepository userRepository;

    public GetAllUsers(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> execute(){
        return this.userRepository.findAll()
                .stream().map(UserDTO::convertToDTO)
                .toList();
    }
}
