package com.outfitlab.project.domain.exceptions.fashion;

public class FashnApiException extends RuntimeException {
    public FashnApiException(String message) {
        super(message);
    }
    public FashnApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
