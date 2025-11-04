package com.outfitlab.project.domain.exceptions;

public class FavoriteLimitExceededException extends RuntimeException {
    public FavoriteLimitExceededException(String message) {
        super(message);
    }
}
