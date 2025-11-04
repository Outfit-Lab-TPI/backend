package com.outfitlab.project.domain.exception;

public class FavoriteLimitExceededException extends RuntimeException {
    
    public FavoriteLimitExceededException(String message) {
        super(message);
    }
    
    public FavoriteLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
