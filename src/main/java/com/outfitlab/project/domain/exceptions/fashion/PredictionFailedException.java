package com.outfitlab.project.domain.exceptions.fashion;

public class PredictionFailedException extends RuntimeException {
    public PredictionFailedException(String message) {
        super(message);
    }
}
