package com.outfitlab.project.domain.useCases.subscription;

import com.outfitlab.project.domain.exceptions.PlanLimitExceededException;
import com.outfitlab.project.domain.exceptions.SubscriptionNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserSubscriptionRepository;
import com.outfitlab.project.domain.model.UserSubscriptionModel;

public class CheckUserPlanLimit {
    private final UserSubscriptionRepository userSubscriptionRepository;
    
    public CheckUserPlanLimit(UserSubscriptionRepository userSubscriptionRepository) {
        this.userSubscriptionRepository = userSubscriptionRepository;
    }
    
    public void execute(String userEmail, String limitType) 
            throws PlanLimitExceededException, SubscriptionNotFoundException {
        UserSubscriptionModel subscription = 
            userSubscriptionRepository.findByUserEmail(userEmail);
        
        switch(limitType) {
            case "combinations":
                checkLimit(subscription.getCombinationsUsed(), 
                          subscription.getMaxCombinations(), 
                          "combinaciones diarias");
                break;
            case "favorites":
                checkLimit(subscription.getFavoritesCount(), 
                          subscription.getMaxFavorites(), 
                          "favoritos");
                break;
            case "3d_models":
                checkLimit(subscription.getModelsGenerated(), 
                          subscription.getMaxModels(), 
                          "modelos 3D");
                break;
        }
    }
    
    private void checkLimit(int current, Integer max, String feature) 
            throws PlanLimitExceededException {
        if (max != null && current >= max) {
            throw new PlanLimitExceededException(
                "Has alcanzado el límite de " + feature + 
                " para tu plan. Actualiza a PRO para acceso ilimitado.",
                feature, current, max
            );
        }
    }
}
