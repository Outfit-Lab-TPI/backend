package com.outfitlab.project.presentation.dto;

import com.outfitlab.project.domain.model.SubscriptionPlanModel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SubscriptionPlanResponse {

    private String planCode;
    private String name;
    private BigDecimal price;
    private Integer maxFavorites;
    private Integer durationDays;
    private Boolean isActive;

    public static SubscriptionPlanResponse fromModel(SubscriptionPlanModel model) {
        SubscriptionPlanResponse response = new SubscriptionPlanResponse();
        response.setPlanCode(model.getPlanCode());
        response.setName(model.getName());
        response.setPrice(model.getPrice());
        response.setMaxFavorites(model.getMaxFavorites());
        response.setDurationDays(model.getDurationDays());
        response.setIsActive(model.getIsActive());
        return response;
    }
}