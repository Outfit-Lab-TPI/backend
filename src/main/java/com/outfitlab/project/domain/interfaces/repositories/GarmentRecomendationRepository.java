package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.model.GarmentRecomendationModel;

import java.util.List;

public interface GarmentRecomendationRepository {
    // Método legacy existente
    List<GarmentRecomendationModel> findRecomendationsByGarmentCode(String garmentCode) throws GarmentNotFoundException;
    
    // Nuevo método para favoritos
    GarmentRecomendationModel findById(Long id);
}
