package com.outfitlab.project.presentation.dto;

/**
 * DTO para agregar un favorito.
 */
public class AddFavoriteRequest {
    private Long garmentRecomendationId;

    public AddFavoriteRequest() {
    }

    public AddFavoriteRequest(Long garmentRecomendationId) {
        this.garmentRecomendationId = garmentRecomendationId;
    }

    public Long getGarmentRecomendationId() {
        return garmentRecomendationId;
    }

    public void setGarmentRecomendationId(Long garmentRecomendationId) {
        this.garmentRecomendationId = garmentRecomendationId;
    }
}
