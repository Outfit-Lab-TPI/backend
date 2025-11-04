package com.outfitlab.project.domain.exception;

public class InvalidPlanException extends RuntimeException {
    
    public InvalidPlanException(String message) {
        super(message);
    }
    
    public InvalidPlanException(String message, Throwable cause) {
        super(message, cause);
    }
}
