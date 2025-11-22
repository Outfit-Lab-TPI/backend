package com.outfitlab.project.domain.exceptions;

public class PasswordIsNotTheSame extends RuntimeException {
    public PasswordIsNotTheSame(String message) {
        super(message);

    }
}
