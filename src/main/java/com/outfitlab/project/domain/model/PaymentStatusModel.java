package com.outfitlab.project.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Modelo del dominio que representa el estado de un pago.
 * No depende de ningún SDK externo (MercadoPago, Stripe, etc.)
 */
public class PaymentStatusModel {

    private String paymentId;
    private String status; // approved, rejected, pending, cancelled
    private String statusDetail;
    private BigDecimal transactionAmount;
    private String payerEmail;
    private String externalReference;
    private LocalDateTime dateApproved;
    private LocalDateTime dateCreated;

    public PaymentStatusModel() {
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

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
    }

    public String getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    public LocalDateTime getDateApproved() {
        return dateApproved;
    }

    public void setDateApproved(LocalDateTime dateApproved) {
        this.dateApproved = dateApproved;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isApproved() {
        return "approved".equalsIgnoreCase(status);
    }
}
