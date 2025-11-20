package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.exceptions.UpdateGarmentException;
import com.outfitlab.project.domain.interfaces.repositories.BrandRepository;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.model.PrendaModel;

public class UpdateGarment {

    private final GarmentRepository garmentRepository;
    private final BrandRepository brandRepository;

    public UpdateGarment(GarmentRepository garmentRepository, BrandRepository brandRepository) {
        this.garmentRepository = garmentRepository;
        this.brandRepository = brandRepository;
    }

    public void execute(String name, String type, String color, String event, String garmentCode, String brandCode, String imageUrl) {
        checkIfBrandExists(brandCode);
        checkIfCanUpdateGarment(this.garmentRepository.findByGarmentCode(garmentCode), brandCode);
        this.garmentRepository.updateGarment(name, type, color, event, garmentCode, imageUrl, this.formatGarmentCode(name));
    }

    private static void checkIfCanUpdateGarment(PrendaModel garment, String brandCode) {
        if (!garment.getMarca().getCodigoMarca().equals(brandCode)) throw new UpdateGarmentException("No puedes actualizar la prenda: " + garment.getGarmentCode() + " porque pertenece a otra marca.");
    }

    private void checkIfBrandExists(String brandCode) {
        if (this.brandRepository.findByBrandCode(brandCode) == null) throw new BrandsNotFoundException("No encontramos la marca:" + brandCode);
    }

    private String formatGarmentCode(String input) {
        if (input == null) return "";
        return input.toLowerCase().trim()
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replace("ñ", "n")
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "_");
    }
}
