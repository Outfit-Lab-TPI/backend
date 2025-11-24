package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.presentation.dto.UserDTO;

import java.util.List;

import static com.outfitlab.project.domain.enums.Role.USER;

public class GetAllUsers {

    private final UserRepository userRepository;

    public GetAllUsers(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> execute(){
        return this.userRepository.findAllWithRoleUserAndAdmin()
                .stream().map(UserDTO::convertToDTO)
                .toList();
    }
}
