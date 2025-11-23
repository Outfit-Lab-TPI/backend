package com.outfitlab.project.domain.useCases.brand;

import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.exceptions.PageLessThanZeroException;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.dto.UserWithBrandsDTO;
import org.springframework.data.domain.Page;

public class GetAllBrandsWithRelatedUsers {

    private final UserRepository userRepository;

    public GetAllBrandsWithRelatedUsers(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<UserWithBrandsDTO> execute(int page) {
        checkPageNumber(page);
        return getAllBrandsWithUserRelated(page);
    }

    private Page<UserWithBrandsDTO> getAllBrandsWithUserRelated(int page) {
        return this.userRepository.getAllBrandsWithUserRelated(page);
    }

    private void checkPageNumber(int page) throws PageLessThanZeroException {
        if (page < 0) throw new PageLessThanZeroException("El número de página es inválido o menor que 0.");
    }
}
