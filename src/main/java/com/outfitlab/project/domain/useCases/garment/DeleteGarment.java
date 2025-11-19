package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.exceptions.DeleteGarmentException;
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

    public void execute(PrendaModel garment, String brandCode) {
        checkIfBrandExists(brandCode);
        checkIfCanDeleteGarment(garment, brandCode);
        this.garmentRepository.deleteGarment(garment.getGarmentCode());
    }

    private static void checkIfCanDeleteGarment(PrendaModel garment, String brandCode) {
        if (!garment.getMarca().getCodigoMarca().equals(brandCode)) throw new DeleteGarmentException("No puedes eliminar la prenda: " + garment.getGarmentCode() + " porque pertenece a otra marca.");
    }

    private void checkIfBrandExists(String brandCode) {
        if (this.brandRepository.findByBrandCode(brandCode) == null) throw new BrandsNotFoundException("No encontramos la marca:" + brandCode);
    }
}
