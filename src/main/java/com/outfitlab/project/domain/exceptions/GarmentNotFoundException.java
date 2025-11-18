package com.outfitlab.project.domain.exceptions;

public class GarmentNotFoundException extends RuntimeException {
    public GarmentNotFoundException(String message) {
        super(message);
    }
}
