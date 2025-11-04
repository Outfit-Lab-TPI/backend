package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.model.UserSubscriptionModel;

public interface UserSubscriptionRepository {

    UserSubscriptionModel save(UserSubscriptionModel userSubscription);
}