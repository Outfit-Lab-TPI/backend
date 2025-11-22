package com.outfitlab.project.domain.useCases.brand;

import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.BrandRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;

public class ActivateBrand {

    private final BrandRepository brandRepository;
    private final UserRepository userRepository;

    public ActivateBrand(BrandRepository brandRepository, UserRepository userRepository) {
        this.brandRepository = brandRepository;
        this.userRepository = userRepository;
    }

    public String execute(String brandCode) {
        checkIfBrandExists(brandCode);
        this.userRepository.activateUser(this.userRepository.getEmailUserRelatedToBrandByBrandCode(brandCode)); // activo el user pq es quien tiene relacionada la brand
        return "Marca activada con éxito.";
    }

    private void checkIfBrandExists(String brandCode) {
        if (this.brandRepository.findByBrandCode(brandCode) == null) throw new BrandsNotFoundException("No encontramos la marca: " + brandCode);
    }
}
