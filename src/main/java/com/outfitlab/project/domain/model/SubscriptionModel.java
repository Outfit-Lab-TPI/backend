package com.outfitlab.project.domain.model;

public class SubscriptionModel {

    private String name;

    private String planCode;

    private Double price;

    private String currency;

    private String frequency;

    private String description;

    private String feature1;
    private String feature2;
    private String feature3;

    private String cardColor;

    private boolean isPopular;

    private String planType;
    private Integer maxGarments;
    private boolean hasAnalytics;
    private boolean hasAdvancedReports;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFeature1() {
        return feature1;
    }

    public void setFeature1(String feature1) {
        this.feature1 = feature1;
    }

    public String getFeature2() {
        return feature2;
    }

    public void setFeature2(String feature2) {
        this.feature2 = feature2;
    }

    public String getFeature3() {
        return feature3;
    }

    public void setFeature3(String feature3) {
        this.feature3 = feature3;
    }

    public String getCardColor() {
        return cardColor;
    }

    public void setCardColor(String cardColor) {
        this.cardColor = cardColor;
    }

    public boolean isPopular() {
        return isPopular;
    }

    public void setPopular(boolean popular) {
        isPopular = popular;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public Integer getMaxGarments() {
        return maxGarments;
    }

    public void setMaxGarments(Integer maxGarments) {
        this.maxGarments = maxGarments;
    }

    public boolean isHasAnalytics() {
        return hasAnalytics;
    }

    public void setHasAnalytics(boolean hasAnalytics) {
        this.hasAnalytics = hasAnalytics;
    }

    public boolean isHasAdvancedReports() {
        return hasAdvancedReports;
    }

    public void setHasAdvancedReports(boolean hasAdvancedReports) {
        this.hasAdvancedReports = hasAdvancedReports;
    }
}
