package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.model.GarmentRecomendationModel;
import com.outfitlab.project.infrastructure.model.GarmentRecomendationEntity;
import com.outfitlab.project.infrastructure.repositories.PrendaMapper;

public class GarmentRecomendationMapper {

    public GarmentRecomendationModel toModel(GarmentRecomendationEntity entity) {
        if (entity == null) {
            return null;
        }
        GarmentRecomendationModel model = new GarmentRecomendationModel();
        model.setId(entity.getId());
        model.setTopGarment(new PrendaMapper().toModel(entity.getTopGarment()));
        model.setBottomGarment(new PrendaMapper().toModel(entity.getBottomGarment()));
        return model;
    }

    public GarmentRecomendationEntity toEntity(GarmentRecomendationModel model) {
        if (model == null) {
            return null;
        }
        GarmentRecomendationEntity entity = new GarmentRecomendationEntity();
        entity.setId(model.getId());
        entity.setTopGarment(new PrendaMapper().toEntity(model.getTopGarment()));
        entity.setBottomGarment(new PrendaMapper().toEntity(model.getBottomGarment()));
        return entity;
    }
}
