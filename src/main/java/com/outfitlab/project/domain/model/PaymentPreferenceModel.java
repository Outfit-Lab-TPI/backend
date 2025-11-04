package com.outfitlab.project.domain.model;

/**
 * Modelo del dominio que representa una preferencia de pago.
 * No depende de ningún SDK externo (MercadoPago, Stripe, etc.)
 */
public class PaymentPreferenceModel {

    private String initPoint;
    private String preferenceId;
    private String sandboxInitPoint;

    public PaymentPreferenceModel() {
    }

    public PaymentPreferenceModel(String initPoint, String preferenceId, String sandboxInitPoint) {
        this.initPoint = initPoint;
        this.preferenceId = preferenceId;
        this.sandboxInitPoint = sandboxInitPoint;
    }

    public String getInitPoint() {
        return initPoint;
    }

    public void setInitPoint(String initPoint) {
        this.initPoint = initPoint;
    }

    public String getPreferenceId() {
        return preferenceId;
    }

    public void setPreferenceId(String preferenceId) {
        this.preferenceId = preferenceId;
    }

    public String getSandboxInitPoint() {
        return sandboxInitPoint;
    }

    public void setSandboxInitPoint(String sandboxInitPoint) {
        this.sandboxInitPoint = sandboxInitPoint;
    }
}
