package com.outfitlab.project.domain.exceptions;

/**
 * Excepción lanzada cuando un usuario intenta agregar más favoritos de los permitidos.
 * Por ejemplo: usuario FREE intenta agregar 3er favorito (límite: 2).
 */
public class FavoriteLimitExceededException extends RuntimeException {
    
    public FavoriteLimitExceededException(String message) {
        super(message);
    }
    
    public FavoriteLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
