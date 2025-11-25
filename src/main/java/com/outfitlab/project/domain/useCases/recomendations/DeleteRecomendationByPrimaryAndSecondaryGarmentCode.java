package com.outfitlab.project.domain.useCases.recomendations;

import com.outfitlab.project.domain.interfaces.repositories.GarmentRecomendationRepository;

public class DeleteRecomendationByPrimaryAndSecondaryGarmentCode {

    private final GarmentRecomendationRepository garmentRecomendationRepository;

    public DeleteRecomendationByPrimaryAndSecondaryGarmentCode(GarmentRecomendationRepository garmentRecomendationRepository) {
        this.garmentRecomendationRepository = garmentRecomendationRepository;
    }

    public String execute(String garmentCodePrimary, String garmentCodeSecondary, String type) {
        this.garmentRecomendationRepository.deleteRecomendationByGarmentsCode(garmentCodePrimary, garmentCodeSecondary, type);
        return "Sugerencia eliminada con éxito.";
    }
}
