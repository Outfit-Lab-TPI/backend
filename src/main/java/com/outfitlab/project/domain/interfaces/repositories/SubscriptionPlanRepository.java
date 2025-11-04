package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.model.SubscriptionPlanModel;

import java.util.List;

public interface SubscriptionPlanRepository {

    List<SubscriptionPlanModel> findAll();

    SubscriptionPlanModel findByPlanCode(String planCode);
}