package com.outfitlab.project.domain.exceptions;

public class PredictionFailedException extends RuntimeException {
    public PredictionFailedException(String message) {
        super(message);
    }
}
