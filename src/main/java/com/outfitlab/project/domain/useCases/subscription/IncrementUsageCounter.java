package com.outfitlab.project.domain.useCases.subscription;

import com.outfitlab.project.domain.interfaces.repositories.UserSubscriptionRepository;

public class IncrementUsageCounter {
    private final UserSubscriptionRepository userSubscriptionRepository;
    
    public IncrementUsageCounter(UserSubscriptionRepository userSubscriptionRepository) {
        this.userSubscriptionRepository = userSubscriptionRepository;
    }
    
    public void execute(String userEmail, String counterType) {
        userSubscriptionRepository.incrementCounter(userEmail, counterType);
    }
}
