package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.helper.CodeFormatter;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;

public class CreateGarment {

    private final GarmentRepository garmentRepository;

    public CreateGarment(GarmentRepository garmentRepository) {
        this.garmentRepository = garmentRepository;
    }

    public void execute(String name, String type, String color, String event, String brandCode, String imageUrl){
        this.garmentRepository.createGarment(name, type, color, event, brandCode, imageUrl, CodeFormatter.execute(name));
    }
}
