package com.outfitlab.project.domain.exceptions;

/**
 * Excepción lanzada cuando no se encuentra una suscripción.
 * Por ejemplo: cuando se busca una suscripción activa que no existe.
 */
public class SubscriptionNotFoundException extends RuntimeException {
    
    public SubscriptionNotFoundException(String message) {
        super(message);
    }
    
    public SubscriptionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
