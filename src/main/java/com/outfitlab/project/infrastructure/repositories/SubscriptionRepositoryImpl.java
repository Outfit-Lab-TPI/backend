package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.model.SubscriptionModel;
import com.outfitlab.project.infrastructure.model.SubscriptionEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.SubscriptionJpaRepository;
import com.outfitlab.project.domain.interfaces.repositories.SubscriptionRepository;

import java.util.List;

public class SubscriptionRepositoryImpl implements SubscriptionRepository {
    private final SubscriptionJpaRepository subscriptionJpaRepository;


    public SubscriptionRepositoryImpl(SubscriptionJpaRepository subscriptionJpaRepository) {
        this.subscriptionJpaRepository = subscriptionJpaRepository;
    }

    @Override
    public List<SubscriptionModel> getAllSubscriptions() {
        return this.subscriptionJpaRepository.findAll()
                .stream()
                .map(SubscriptionEntity::convertToModel)
                .toList();
    }
    
    @Override
    public SubscriptionModel getByPlanCode(String planCode) {
        return subscriptionJpaRepository.findByPlanCode(planCode)
                .map(SubscriptionEntity::convertToModel)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado: " + planCode));
    }
}
