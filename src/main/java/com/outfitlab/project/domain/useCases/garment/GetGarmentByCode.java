package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.model.PrendaModel;

public class GetGarmentByCode {

    private final GarmentRepository garmentRepository;

    public GetGarmentByCode(GarmentRepository garmentRepository) {
        this.garmentRepository = garmentRepository;
    }

    public PrendaModel execute(String code) {
        return this.garmentRepository.findByGarmentCode(code);
    }
}
