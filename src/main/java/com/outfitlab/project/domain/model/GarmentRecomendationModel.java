package com.outfitlab.project.domain.model;

public class GarmentRecomendationModel {

    private Long id;
    private PrendaModel topGarment;
    private PrendaModel bottomGarment;

    public GarmentRecomendationModel() {}

    public GarmentRecomendationModel(PrendaModel topGarment, PrendaModel bottomGarment) {
        this.topGarment = topGarment;
        this.bottomGarment = bottomGarment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PrendaModel getTopGarment() {
        return topGarment;
    }

    public void setTopGarment(PrendaModel topGarment) {
        this.topGarment = topGarment;
    }

    public PrendaModel getBottomGarment() {
        return bottomGarment;
    }

    public void setBottomGarment(PrendaModel bottomGarment) {
        this.bottomGarment = bottomGarment;
    }
}

