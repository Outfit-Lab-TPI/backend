package com.outfitlab.project.domain.useCases.brand;

import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.dto.UserWithBrandsDTO;

import java.util.List;

public class GetNotificationsNewBrands {

    private final UserRepository userRepository;

    public GetNotificationsNewBrands(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserWithBrandsDTO> execute(){
        return this.userRepository.getNotApprovedBrands();
    }
}
