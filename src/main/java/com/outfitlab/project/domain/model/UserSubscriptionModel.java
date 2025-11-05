package com.outfitlab.project.domain.model;

import com.outfitlab.project.domain.model.enums.SubscriptionStatus;
import java.time.LocalDateTime;

public class UserSubscriptionModel {
    private Long id;
    private UserModel user;
    private SubscriptionPlanModel plan;
    private String mercadoPagoPaymentId;
    private SubscriptionStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean autoRenew;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
    
    // Helper para infraestructura
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public SubscriptionPlanModel getPlan() {
        return plan;
    }

    public void setPlan(SubscriptionPlanModel plan) {
        this.plan = plan;
    }
    
    // Helper para infraestructura
    public Long getPlanId() {
        return plan != null ? plan.getId() : null;
    }

    public String getMercadoPagoPaymentId() {
        return mercadoPagoPaymentId;
    }

    public void setMercadoPagoPaymentId(String mercadoPagoPaymentId) {
        this.mercadoPagoPaymentId = mercadoPagoPaymentId;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Boolean getAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(Boolean autoRenew) {
        this.autoRenew = autoRenew;
    }
}
