package com.outfitlab.project.domain.model.dto;

public class GeminiRecommendationDTO {

    private String clima;
    private String ocasion;

    public GeminiRecommendationDTO() {}

    public GeminiRecommendationDTO(String clima, String ocasion) {
        this.clima = clima;
        this.ocasion = ocasion;
    }

    public String getClima() {
        return this.clima;
    }

    public String getOcasion() {
        return this.ocasion;
    }

    public void setClima(String clima) {
        this.clima = clima;
    }

    public void setOcasion(String ocasion) {
        this.ocasion = ocasion;
    }
}