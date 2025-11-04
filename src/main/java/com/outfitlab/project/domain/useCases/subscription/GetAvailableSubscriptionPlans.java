package com.outfitlab.project.domain.useCases.subscription;

import com.outfitlab.project.domain.interfaces.repositories.SubscriptionPlanRepository;
import com.outfitlab.project.domain.model.SubscriptionPlanModel;

import java.util.List;

public class GetAvailableSubscriptionPlans {

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public GetAvailableSubscriptionPlans(SubscriptionPlanRepository subscriptionPlanRepository) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    public List<SubscriptionPlanModel> execute() {
        return subscriptionPlanRepository.findAll();
    }
}