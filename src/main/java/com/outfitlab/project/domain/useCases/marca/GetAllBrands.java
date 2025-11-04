package com.outfitlab.project.domain.useCases.marca;

import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.exceptions.PageLessThanZeroException;
import com.outfitlab.project.domain.interfaces.repositories.BrandRepository;
import com.outfitlab.project.domain.model.BrandModel;
import com.outfitlab.project.domain.model.dto.BrandDTO;
import org.springframework.data.domain.Page;


public class GetAllBrands {

    private final BrandRepository iBrandRepository;

    public GetAllBrands(BrandRepository marcaRepository) {
        this.iBrandRepository = marcaRepository;
    }

    public Page<BrandDTO> execute(int page) throws BrandsNotFoundException, PageLessThanZeroException {
        checkPageNumber(page);
        return getAllBrands(page);
    }

    private Page<BrandDTO> getAllBrands(int page) throws BrandsNotFoundException {
        Page<BrandModel> brands = iBrandRepository.getAllBrands(page);

        if (!brands.isEmpty()) return brands.map(BrandDTO::convertModelToDTO);

        throw new BrandsNotFoundException("No encontramos marcas");
    }

    private void checkPageNumber(int page) throws PageLessThanZeroException {
        if (page < 0) throw new PageLessThanZeroException("El número de página es inválido o menor que 0.");
    }
}
