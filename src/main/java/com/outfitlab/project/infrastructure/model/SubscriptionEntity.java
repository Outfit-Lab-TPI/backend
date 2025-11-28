package com.outfitlab.project.infrastructure.model;

import com.outfitlab.project.domain.model.SubscriptionModel;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "suscription_entity")
@NoArgsConstructor
public class SubscriptionEntity {

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String planCode;

    @Column(nullable = false)
    private Double price;

    private String currency;

    private String frequency;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String feature1;
    private String feature2;
    private String feature3;

    private String cardColor;

    private boolean isPopular;

    @Column(name = "plan_type", nullable = false)
    private String planType = "USER";

    @Column(name = "max_garments")
    private Integer maxGarments;

    @Column(name = "has_analytics", columnDefinition = "boolean default false")
    private boolean hasAnalytics = false;

    @Column(name = "has_advanced_reports", columnDefinition = "boolean default false")
    private boolean hasAdvancedReports = false;

    public SubscriptionEntity(String name, String planCode) {
        this.name = name;
        this.planCode = planCode;
    }

    public Long getId() {
        return id;
    }

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

    public boolean getIsPopular() {
        return isPopular;
    }

    public void setIsPopular(boolean popular) {
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

    public static SubscriptionModel convertToModel(SubscriptionEntity subscriptionEntity) {
        SubscriptionModel subscriptionModel = new SubscriptionModel();
        subscriptionModel.setCardColor(String.valueOf(subscriptionEntity.getCardColor()));
        subscriptionModel.setName(subscriptionEntity.getName());
        subscriptionModel.setFrequency(subscriptionEntity.getFrequency());
        subscriptionModel.setPopular(subscriptionEntity.getIsPopular());
        subscriptionModel.setDescription(subscriptionEntity.getDescription());
        subscriptionModel.setFeature1(subscriptionEntity.getFeature1());
        subscriptionModel.setFeature2(subscriptionEntity.getFeature2());
        subscriptionModel.setFeature3(subscriptionEntity.getFeature3());
        subscriptionModel.setPlanCode(subscriptionEntity.getPlanCode());
        subscriptionModel.setPrice(subscriptionEntity.getPrice());
        subscriptionModel.setCurrency(subscriptionEntity.getCurrency());
        subscriptionModel.setPlanType(subscriptionEntity.getPlanType());
        subscriptionModel.setMaxGarments(subscriptionEntity.getMaxGarments());
        subscriptionModel.setHasAnalytics(subscriptionEntity.isHasAnalytics());
        subscriptionModel.setHasAdvancedReports(subscriptionEntity.isHasAdvancedReports());

        return subscriptionModel;
    }
}
