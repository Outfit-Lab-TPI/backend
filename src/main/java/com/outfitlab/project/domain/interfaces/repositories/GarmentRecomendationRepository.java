package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.model.GarmentRecomendationModel;

import java.util.List;
import java.util.Optional;

public interface GarmentRecomendationRepository {

    Optional<GarmentRecomendationModel> findById(Long id);

    List<GarmentRecomendationModel> findRecomendationsByGarmentCode(String garmentCode) throws GarmentNotFoundException;

}
