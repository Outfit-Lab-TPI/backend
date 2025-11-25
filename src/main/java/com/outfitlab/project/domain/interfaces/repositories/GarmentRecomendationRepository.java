package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.model.GarmentRecomendationModel;

import java.util.List;

public interface GarmentRecomendationRepository {

    List<GarmentRecomendationModel> findRecomendationsByGarmentCode(String garmentCode) throws GarmentNotFoundException;

    void deleteRecomendationsByGarmentCode(String garmentCode);

    void createSugerenciasByGarmentCode(String garmentCode, String type, List<String> sugerencias);

    void deleteRecomendationByGarmentsCode(String garmentCodePrimary, String garmentCodeSecondary, String type);
}
