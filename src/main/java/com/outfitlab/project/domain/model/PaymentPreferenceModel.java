package com.outfitlab.project.domain.model;

public class PaymentPreferenceModel {
    private String preferenceId;
    private String initPoint;

    public PaymentPreferenceModel() {
    }

    public PaymentPreferenceModel(String preferenceId, String initPoint) {
        this.preferenceId = preferenceId;
        this.initPoint = initPoint;
    }

    public String getPreferenceId() {
        return preferenceId;
    }

    public void setPreferenceId(String preferenceId) {
        this.preferenceId = preferenceId;
    }

    public String getInitPoint() {
        return initPoint;
    }

    public void setInitPoint(String initPoint) {
        this.initPoint = initPoint;
    }
}
