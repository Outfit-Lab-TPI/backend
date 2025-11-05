package com.outfitlab.project.domain.exceptions;

/**
 * Excepción lanzada cuando se intenta usar un plan de suscripción inválido.
 * Por ejemplo: plan que no existe o plan que está inactivo.
 */
public class InvalidPlanException extends RuntimeException {
    
    public InvalidPlanException(String message) {
        super(message);
    }
    
    public InvalidPlanException(String message, Throwable cause) {
        super(message, cause);
    }
}
