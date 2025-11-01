package com.outfitlab.project.domain.exceptions;

public class PredictionTimeoutException extends RuntimeException {
    public PredictionTimeoutException(String message) {
        super(message);
    }
}
