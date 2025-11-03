package com.outfitlab.project.domain.exceptions;

public class FashnApiException extends Exception {
    public FashnApiException(String message) {
        super(message);
    }
    public FashnApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
