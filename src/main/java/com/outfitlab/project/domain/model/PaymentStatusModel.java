package com.outfitlab.project.domain.model;

public class PaymentStatusModel {
    private String paymentId;
    private String status;
    private String statusDetail;
    private String externalReference;
    private String payerEmail;

    public PaymentStatusModel() {
    }

    public PaymentStatusModel(String paymentId, String status, String statusDetail, 
                             String externalReference, String payerEmail) {
        this.paymentId = paymentId;
        this.status = status;
        this.statusDetail = statusDetail;
        this.externalReference = externalReference;
        this.payerEmail = payerEmail;
    }

    public boolean isApproved() {
        return "approved".equalsIgnoreCase(status);
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDetail() {
        return statusDetail;
    }

    public void setStatusDetail(String statusDetail) {
        this.statusDetail = statusDetail;
    }

    public String getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
    }
}
