package com.outfitlab.project.domain.useCases.subscription;

import com.outfitlab.project.domain.interfaces.repositories.SubscriptionRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserSubscriptionRepository;
import com.outfitlab.project.domain.model.SubscriptionModel;
import com.outfitlab.project.domain.model.UserSubscriptionModel;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssignFreePlanToUser {
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final SubscriptionRepository subscriptionRepository;
    private static final String USER_FREE_PLAN_CODE = "user-free-monthly";
    private static final String BRAND_FREE_PLAN_CODE = "brand-free-monthly";

    public AssignFreePlanToUser(UserSubscriptionRepository userSubscriptionRepository,
            SubscriptionRepository subscriptionRepository) {
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public void execute(String userEmail, boolean isBrand) {
        String planCode = isBrand ? BRAND_FREE_PLAN_CODE : USER_FREE_PLAN_CODE;
        SubscriptionModel freePlan = subscriptionRepository.getByPlanCode(planCode);

        UserSubscriptionModel userSubscription = new UserSubscriptionModel();
        userSubscription.setUserEmail(userEmail);
        userSubscription.setPlanCode(freePlan.getPlanCode());
        userSubscription.setMaxCombinations(parseLimitFromFeature(freePlan.getFeature2()));
        userSubscription.setMaxFavorites(parseLimitFromFeature(freePlan.getFeature1()));
        userSubscription.setMaxModels(parseLimitFromFeature(freePlan.getFeature3()));
        userSubscription.setStatus("ACTIVE");
        userSubscription.setSubscriptionStart(LocalDateTime.now());

        userSubscriptionRepository.save(userSubscription);
    }

    private Integer parseLimitFromFeature(String feature) {
        if (feature == null) {
            return null;
        }

        if (feature.toLowerCase().contains("ilimitado") ||
                feature.toLowerCase().contains("ilimitada")) {
            return null;
        }

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(feature);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }

        return null;
    }
}
