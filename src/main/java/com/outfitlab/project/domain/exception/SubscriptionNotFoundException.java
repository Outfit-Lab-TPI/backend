package com.outfitlab.project.domain.exception;

public class SubscriptionNotFoundException extends RuntimeException {
    
    public SubscriptionNotFoundException(String message) {
        super(message);
    }
    
    public SubscriptionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
