package com.outfitlab.project.presentation.dto;

public class GeneratedResponse {
    private String status;
    private String imageUrl;
    private String errorMessage;

    public GeneratedResponse() {}
    public GeneratedResponse(String status, String imageUrl, String errorMessage) {
        this.status = status;
        this.imageUrl = imageUrl;
        this.errorMessage = errorMessage;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
