package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.useCases.subscription.GetAvailableSubscriptionPlans;
import com.outfitlab.project.presentation.dto.SubscriptionPlanResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subscription/plans")
public class SubscriptionPlansController {

    private final GetAvailableSubscriptionPlans getAvailableSubscriptionPlans;

    public SubscriptionPlansController(GetAvailableSubscriptionPlans getAvailableSubscriptionPlans) {
        this.getAvailableSubscriptionPlans = getAvailableSubscriptionPlans;
    }

    @GetMapping
    public List<SubscriptionPlanResponse> getAvailablePlans() {
        return getAvailableSubscriptionPlans.execute().stream()
                .map(SubscriptionPlanResponse::fromModel)
                .collect(Collectors.toList());
    }
}