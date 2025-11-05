package com.outfitlab.project.domain.exceptions;

/**
 * Excepción lanzada cuando un pago falla o no es aprobado.
 * Por ejemplo: pago rechazado, cancelado o pendiente.
 */
public class PaymentFailedException extends RuntimeException {
    
    public PaymentFailedException(String message) {
        super(message);
    }
    
    public PaymentFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
