package com.outfitlab.project.domain.useCases.subscription;

import com.outfitlab.project.domain.model.SubscriptionModel;
import com.outfitlab.project.domain.interfaces.repositories.SubscriptionRepository;

import java.util.List;

public class GetAllSubscription {

    private final SubscriptionRepository subscriptionRepository;
    public GetAllSubscription(SubscriptionRepository subscriptionRepository){
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<SubscriptionModel> execute(){
        return subscriptionRepository.getAllSubscriptions();
    }


}
