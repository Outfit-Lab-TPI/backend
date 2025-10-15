package com.outfitlab.project.presentation.dto;

public class CombineRequest {
    private String superior;
    private String inferior;
    private Boolean esHombre;

    public String getSuperior() { return superior; }
    public void setSuperior(String superior) { this.superior = superior; }
    public String getInferior() { return inferior; }
    public void setInferior(String inferior) { this.inferior = inferior; }
    public Boolean getEsHombre() {return esHombre;}
    public void setEsHombre(Boolean esHombre) {this.esHombre = esHombre;}
}
