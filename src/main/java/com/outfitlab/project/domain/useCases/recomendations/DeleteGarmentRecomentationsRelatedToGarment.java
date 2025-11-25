package com.outfitlab.project.domain.useCases.recomendations;

import com.outfitlab.project.domain.interfaces.repositories.GarmentRecomendationRepository;

public class DeleteGarmentRecomentationsRelatedToGarment {

    private final GarmentRecomendationRepository garmentRecomendationRepository;

    public DeleteGarmentRecomentationsRelatedToGarment(GarmentRecomendationRepository garmentRecomendationRepository){
        this.garmentRecomendationRepository = garmentRecomendationRepository;
    }

    public void execute(String garmentCode) {
        this.garmentRecomendationRepository.deleteRecomendationsByGarmentCode(garmentCode);
    }
}
