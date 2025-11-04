package com.outfitlab.project.domain.exception;

public class PaymentFailedException extends RuntimeException {
    
    public PaymentFailedException(String message) {
        super(message);
    }
    
    public PaymentFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
