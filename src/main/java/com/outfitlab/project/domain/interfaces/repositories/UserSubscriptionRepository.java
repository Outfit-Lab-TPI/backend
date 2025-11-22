package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.exceptions.SubscriptionNotFoundException;
import com.outfitlab.project.domain.model.UserSubscriptionModel;

public interface UserSubscriptionRepository {
    UserSubscriptionModel findByUserEmail(String userEmail) throws SubscriptionNotFoundException;
    
    UserSubscriptionModel save(UserSubscriptionModel subscription);
    
    UserSubscriptionModel update(UserSubscriptionModel subscription);
    
    void incrementCounter(String userEmail, String counterType);
    
    void decrementCounter(String userEmail, String counterType);
}
