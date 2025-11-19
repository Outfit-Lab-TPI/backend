package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.exceptions.DeleteGarmentException;
import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.BrandRepository;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.model.PrendaModel;

public class DeleteGarment {

    private final GarmentRepository garmentRepository;
    private final BrandRepository brandRepository;

    public DeleteGarment(GarmentRepository garmentRepository, BrandRepository brandRepository) {
        this.garmentRepository = garmentRepository;
        this.brandRepository = brandRepository;
    }

    public void execute(String garmentCode, String brandCode) {
        checkIfBrandExists(brandCode);
        PrendaModel prenda = this.garmentRepository.findByGarmentCode(garmentCode);
        checkIfGarmentExits(garmentCode, prenda);
        checkIfCanDeleteGarment(garmentCode, brandCode, prenda);
        this.garmentRepository.deleteGarment(garmentCode);
    }

    private static void checkIfCanDeleteGarment(String garmentCode, String brandCode, PrendaModel prenda) {
        if (!prenda.getMarca().getCodigoMarca().equals(brandCode)) throw new DeleteGarmentException("No puedes eliminar la prenda: " + garmentCode + " porque pertenece a otra marca.");
    }

    private static void checkIfGarmentExits(String garmentCode, PrendaModel prenda) {
        if (prenda == null) throw new GarmentNotFoundException("No encontramos la prenda:" + garmentCode);
    }

    private void checkIfBrandExists(String brandCode) {
        if (this.brandRepository.findByBrandCode(brandCode) == null) throw new BrandsNotFoundException("No encontramos la marca:" + brandCode);
    }
}
