package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.model.PrendaModel;
import org.springframework.data.domain.Page;

public class GetGarmentsByType {

    private final GarmentRepository garmentRepository;

    public GetGarmentsByType(GarmentRepository garmentRepository) {
        this.garmentRepository = garmentRepository;
    }

    public Page<PrendaModel> execute(String type, int page) throws GarmentNotFoundException {
        Page<PrendaModel> pageResponse = this.garmentRepository.getGarmentsByType(type.toLowerCase(), page);
        if (!pageResponse.hasContent()) throw new GarmentNotFoundException("No encontramos prendas de tipo: " + type);
        return pageResponse;
    }
}
