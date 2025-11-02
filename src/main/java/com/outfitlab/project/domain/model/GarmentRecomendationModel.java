package com.outfitlab.project.domain.model;

public class GarmentRecomendationModel {

    private PrendaModel topGarment;
    private PrendaModel bottomGarment;

    public GarmentRecomendationModel(PrendaModel topGarment, PrendaModel bottomGarment) {
        this.topGarment = topGarment;
        this.bottomGarment = bottomGarment;
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

