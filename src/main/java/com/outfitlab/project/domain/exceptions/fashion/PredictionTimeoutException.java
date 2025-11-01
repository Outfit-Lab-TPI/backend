package com.outfitlab.project.domain.exceptions.fashion;

public class PredictionTimeoutException extends RuntimeException {
    public PredictionTimeoutException(String message) {
        super(message);
    }
}
