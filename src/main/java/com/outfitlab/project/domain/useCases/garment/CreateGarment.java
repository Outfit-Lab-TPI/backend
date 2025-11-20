package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;

public class CreateGarment {

    private final GarmentRepository garmentRepository;

    public CreateGarment(GarmentRepository garmentRepository) {
        this.garmentRepository = garmentRepository;
    }

    public void execute(String name, String type, String color, String event, String brandCode, String imageUrl){
        String garmentCode = this.formatGarmentCode(name);
        this.garmentRepository.createGarment(name, type, color, event, brandCode, imageUrl, garmentCode);
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
