package com.outfitlab.project.presentation.dto;

public class CombineRequest {
    private String superiorUrl;
    private String inferiorUrl;
    private Boolean esHombre;

    public String getSuperiorUrl() { return superiorUrl; }
    public void setSuperiorUrl(String superiorUrl) { this.superiorUrl = superiorUrl; }
    public String getInferiorUrl() { return inferiorUrl; }
    public void setInferiorUrl(String inferiorUrl) { this.inferiorUrl = inferiorUrl; }
    public Boolean getEsHombre() {return esHombre;}
    public void setEsHombre(Boolean esHombre) {this.esHombre = esHombre;}
}
