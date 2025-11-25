package com.outfitlab.project.domain.model;

public class TripoTaskStatusResult {
    private final String status;
    private final String modelUrl;
    private final String errorMessage;

    public TripoTaskStatusResult(String status, String modelUrl, String errorMessage) {
        this.status = status;
        this.modelUrl = modelUrl;
        this.errorMessage = errorMessage;
    }

    public String getStatus() {
        return status;
    }

    public String getModelUrl() {
        return modelUrl;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
