package com.outfitlab.project.infrastructure.repositories.impl;

import com.outfitlab.project.domain.interfaces.repositories.SubscriptionPlanRepository;
import com.outfitlab.project.domain.model.SubscriptionPlanModel;
import com.outfitlab.project.infrastructure.model.SubscriptionPlanEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.SubscriptionPlanJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class SubscriptionPlanRepositoryImpl implements SubscriptionPlanRepository {

    private final SubscriptionPlanJpaRepository jpaRepository;

    public SubscriptionPlanRepositoryImpl(SubscriptionPlanJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public SubscriptionPlanModel findByPlanCode(String planCode) {
        return jpaRepository.findByPlanCode(planCode)
                .map(this::toModel)
                .orElse(null);
    }

    @Override
    public List<SubscriptionPlanModel> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    private SubscriptionPlanModel toModel(SubscriptionPlanEntity entity) {
        SubscriptionPlanModel model = new SubscriptionPlanModel();
        model.setId(entity.getId());
        model.setPlanCode(entity.getPlanCode());
        model.setName(entity.getName());
        model.setPrice(entity.getPrice());
        model.setMaxFavorites(entity.getMaxFavorites());
        model.setDurationDays(entity.getDurationDays());
        model.setIsActive(entity.getIsActive());
        return model;
    }

    private SubscriptionPlanEntity toEntity(SubscriptionPlanModel model) {
        SubscriptionPlanEntity entity = new SubscriptionPlanEntity();
        entity.setId(model.getId());
        entity.setPlanCode(model.getPlanCode());
        entity.setName(model.getName());
        entity.setPrice(model.getPrice());
        entity.setMaxFavorites(model.getMaxFavorites());
        entity.setDurationDays(model.getDurationDays());
        entity.setIsActive(model.getIsActive());
        return entity;
    }
}
