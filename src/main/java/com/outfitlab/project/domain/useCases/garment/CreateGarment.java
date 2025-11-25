package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;

import java.util.List;

public class CreateGarment {

    private final GarmentRepository garmentRepository;

    public CreateGarment(GarmentRepository garmentRepository) {
        this.garmentRepository = garmentRepository;
    }

    public void execute(String name, String type, String color, String brandCode, String imageUrl, String climaNombre,  List<String> ocasionesNombres, String genero) {
        this.garmentRepository.createGarment(
                name,
                formatGarmentCode(name),
                type,
                color,
                brandCode,
                imageUrl,
                climaNombre,
                ocasionesNombres,
                genero
        );
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
