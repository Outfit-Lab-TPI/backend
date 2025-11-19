package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.model.SubscriptionModel;

import java.util.List;

public interface SubscriptionRepository {

    List<SubscriptionModel> getAllSubscriptions();
}
