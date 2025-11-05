package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.interfaces.repositories.SubscriptionPlanRepository;
import com.outfitlab.project.domain.model.SubscriptionPlanModel;
import com.outfitlab.project.infrastructure.model.SubscriptionPlanEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.SubscriptionPlanJpaRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del puerto SubscriptionPlanRepository.
 * Adaptador que usa Spring Data JPA para persistencia.
 */
public class SubscriptionPlanRepositoryImpl implements SubscriptionPlanRepository {

    private final SubscriptionPlanJpaRepository jpaRepository;

    public SubscriptionPlanRepositoryImpl(SubscriptionPlanJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<SubscriptionPlanModel> findAllActive() {
        return jpaRepository.findByIsActiveTrue()
                .stream()
                .map(SubscriptionPlanEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public SubscriptionPlanModel findByPlanCode(String planCode) {
        return jpaRepository.findByPlanCode(planCode)
                .map(SubscriptionPlanEntity::toModel)
                .orElse(null);
    }
}
