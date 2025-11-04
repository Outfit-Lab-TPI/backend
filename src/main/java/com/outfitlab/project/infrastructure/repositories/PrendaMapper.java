package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.infrastructure.model.PrendaEntity;

public class PrendaMapper {

    public PrendaModel toModel(PrendaEntity entity) {
        if (entity == null) {
            return null;
        }
        return PrendaEntity.convertToModel(entity);
    }

    public PrendaEntity toEntity(PrendaModel model) {
        if (model == null) {
            return null;
        }
        return PrendaEntity.convertToEntity(model);
    }
}
