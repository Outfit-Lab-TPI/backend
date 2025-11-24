package com.outfitlab.project.domain.model;

public class CombinationModel {
    private Long id;
    private PrendaModel prendaSuperior;
    private PrendaModel prendaInferior;

    public CombinationModel() { }

    public CombinationModel(PrendaModel prendaSuperior, PrendaModel prendaInferior) {
        this.prendaSuperior = prendaSuperior;
        this.prendaInferior = prendaInferior;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public PrendaModel getPrendaSuperior() {
        return prendaSuperior;
    }
    public void setPrendaSuperior(PrendaModel prendaSuperior) {
        this.prendaSuperior = prendaSuperior;
    }
    public PrendaModel getPrendaInferior() {
        return prendaInferior;
    }
    public void setPrendaInferior(PrendaModel prendaInferior) {
        this.prendaInferior = prendaInferior;}
}
