package com.outfitlab.project.domain.model;

import java.math.BigDecimal;

public class SubscriptionPlanModel {
    private Long id;
    private String planCode;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer maxFavorites;
    private Integer durationDays;
    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getMaxFavorites() {
        return maxFavorites;
    }

    public void setMaxFavorites(Integer maxFavorites) {
        this.maxFavorites = maxFavorites;
    }

    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
