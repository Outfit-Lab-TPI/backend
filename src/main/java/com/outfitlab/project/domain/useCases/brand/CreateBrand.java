package com.outfitlab.project.domain.useCases.brand;

import com.outfitlab.project.domain.helper.CodeFormatter;
import com.outfitlab.project.domain.interfaces.repositories.BrandRepository;
import com.outfitlab.project.domain.model.BrandModel;

public class CreateBrand {

    private final BrandRepository brandRepository;

    public CreateBrand(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public String execute(String brandName, String logoUrl, String urlSite) {
        return this.brandRepository.createBrand(
                new BrandModel(
                        CodeFormatter.execute(brandName),
                        brandName,
                        logoUrl,
                        urlSite
                ));
    }
}
