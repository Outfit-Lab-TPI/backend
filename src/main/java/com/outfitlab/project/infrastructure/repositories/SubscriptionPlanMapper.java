package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.model.SubscriptionPlanModel;
import com.outfitlab.project.infrastructure.model.SubscriptionPlanEntity;

public class SubscriptionPlanMapper {

    public SubscriptionPlanModel toModel(SubscriptionPlanEntity entity) {
        if (entity == null) {
            return null;
        }
        SubscriptionPlanModel model = new SubscriptionPlanModel();
        model.setPlanCode(entity.getPlanCode());
        model.setName(entity.getName());
        model.setPrice(entity.getPrice());
        model.setMaxFavorites(entity.getMaxFavorites());
        model.setDurationDays(entity.getDurationDays());
        model.setIsActive(entity.getIsActive());
        return model;
    }

    public SubscriptionPlanEntity toEntity(SubscriptionPlanModel model) {
        if (model == null) {
            return null;
        }
        SubscriptionPlanEntity entity = new SubscriptionPlanEntity();
        entity.setPlanCode(model.getPlanCode());
        entity.setName(model.getName());
        entity.setPrice(model.getPrice());
        entity.setMaxFavorites(model.getMaxFavorites());
        entity.setDurationDays(model.getDurationDays());
        entity.setIsActive(model.getIsActive());
        return entity;
    }
}
