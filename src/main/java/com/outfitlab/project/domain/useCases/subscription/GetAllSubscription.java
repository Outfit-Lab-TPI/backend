package com.outfitlab.project.domain.useCases.subscription;

import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.interfaces.repositories.SubscriptionRepository;
import com.outfitlab.project.domain.model.SubscriptionModel;
import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;

import java.util.List;

public class GetAllSubscription {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public GetAllSubscription(SubscriptionRepository subscriptionRepository, UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }

    public List<SubscriptionModel> execute(String userEmail) {
        if (userEmail == null || userEmail.isEmpty()) {
            return subscriptionRepository.findByPlanType("USER");
        }

        try {
            UserModel user = userRepository.findUserByEmail(userEmail);
            String planType = (user.getBrand() != null) ? "BRAND" : "USER";
            return subscriptionRepository.findByPlanType(planType);
        } catch (UserNotFoundException e) {
            return subscriptionRepository.findByPlanType("USER");
        }
    }
}
