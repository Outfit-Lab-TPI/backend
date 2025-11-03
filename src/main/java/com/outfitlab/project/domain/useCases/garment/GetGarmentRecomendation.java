package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRecomendationRepository;
import com.outfitlab.project.domain.model.GarmentRecomendationModel;

import java.util.List;

public class GetGarmentRecomendation {

    private final GarmentRecomendationRepository garmentRecomendationRepository;

    public GetGarmentRecomendation(GarmentRecomendationRepository garmentRecomendationRepository) {
        this.garmentRecomendationRepository = garmentRecomendationRepository;
    }

    public List<GarmentRecomendationModel> execute(String garmentCode) throws GarmentNotFoundException {
        return this.garmentRecomendationRepository.findRecomendationsByGarmentCode(garmentCode);
    }
}
