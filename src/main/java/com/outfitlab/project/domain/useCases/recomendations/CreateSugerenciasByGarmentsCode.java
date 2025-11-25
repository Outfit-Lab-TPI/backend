package com.outfitlab.project.domain.useCases.recomendations;

import com.outfitlab.project.domain.interfaces.repositories.GarmentRecomendationRepository;

import java.util.List;

public class CreateSugerenciasByGarmentsCode {

    private final GarmentRecomendationRepository garmentRecomendationRepository;

    public CreateSugerenciasByGarmentsCode(GarmentRecomendationRepository garmentRecomendationRepository){
        this.garmentRecomendationRepository = garmentRecomendationRepository;
    }

    public void execute(String garmentCode, String type, List<String> sugerencias) {
        this.garmentRecomendationRepository.createSugerenciasByGarmentCode(garmentCode, type, sugerencias);
    }
}
