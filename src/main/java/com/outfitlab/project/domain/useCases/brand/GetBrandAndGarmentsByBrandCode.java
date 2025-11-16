package com.outfitlab.project.domain.useCases.brand;

import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.exceptions.PageLessThanZeroException;
import com.outfitlab.project.domain.interfaces.repositories.BrandRepository;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.model.BrandModel;
import com.outfitlab.project.domain.model.dto.BrandAndGarmentsDTO;
import com.outfitlab.project.domain.model.dto.BrandDTO;
import com.outfitlab.project.domain.model.dto.PageDTO;

public class GetBrandAndGarmentsByBrandCode {

    private final BrandRepository brandRepository;
    private final GarmentRepository garmentRepository;

    public GetBrandAndGarmentsByBrandCode(BrandRepository marcaRepository, GarmentRepository garmentRepository) {
        this.brandRepository = marcaRepository;
        this.garmentRepository = garmentRepository;
    }

    public BrandAndGarmentsDTO execute(String brandCode, int page) throws BrandsNotFoundException, PageLessThanZeroException {
        checkPageNumber(page);
        return new BrandAndGarmentsDTO(
                getBrandByBrandCode(brandCode), 
                getGarmentsByBrandCodeAndType(brandCode,"superior", page),
                getGarmentsByBrandCodeAndType(brandCode,"inferior", page)
        );
    }

    private PageDTO getGarmentsByBrandCodeAndType(String brandCode, String type, int page) {
        return this.garmentRepository.findByBrandCodeAndTipo(brandCode, type, page);
    }

    private BrandDTO getBrandByBrandCode(String brandCode) throws BrandsNotFoundException {
        BrandModel brandModel = this.brandRepository.findByBrandCode(brandCode);
        if (brandModel == null) throw new BrandsNotFoundException("No encontramos la marca: " + brandCode);
        return BrandDTO.convertModelToDTO(brandModel);
    }

    private void checkPageNumber(int page) throws PageLessThanZeroException {
        if (page < 0) throw new PageLessThanZeroException("El número de página es inválido o menor que 0.");
    }
}
