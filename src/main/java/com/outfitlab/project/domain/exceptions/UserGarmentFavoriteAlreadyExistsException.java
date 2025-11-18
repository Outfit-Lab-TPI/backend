package com.outfitlab.project.domain.exceptions;

public class UserGarmentFavoriteAlreadyExistsException extends RuntimeException {
    public UserGarmentFavoriteAlreadyExistsException(String message) {
        super(message);
    }
}
