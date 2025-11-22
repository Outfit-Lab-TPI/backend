package com.outfitlab.project.domain.exceptions;

public class PlanLimitExceededException extends Exception {
    private String limitType;
    private int currentUsage;
    private int maxAllowed;

    public PlanLimitExceededException(String message, String limitType, int currentUsage, int maxAllowed) {
        super(message);
        this.limitType = limitType;
        this.currentUsage = currentUsage;
        this.maxAllowed = maxAllowed;
    }

    public String getLimitType() {
        return limitType;
    }

    public int getCurrentUsage() {
        return currentUsage;
    }

    public int getMaxAllowed() {
        return maxAllowed;
    }
}
