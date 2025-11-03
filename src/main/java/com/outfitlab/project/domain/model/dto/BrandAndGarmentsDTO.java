package com.outfitlab.project.domain.model.dto;

import java.util.List;

public class BrandAndGarmentsDTO {

    private BrandDTO brandDTO;
    private GarmentPageDTO garmentTop;
    private GarmentPageDTO garmentBottom;

    public BrandAndGarmentsDTO(BrandDTO brandDTO, GarmentPageDTO garmentTop, GarmentPageDTO garmentBottom) {
        this.brandDTO = brandDTO;
        this.garmentTop = garmentTop;
        this.garmentBottom = garmentBottom;
    }

    public BrandDTO getBrandDTO() {
        return brandDTO;
    }

    public void setBrandDTO(BrandDTO brandDTO) {
        this.brandDTO = brandDTO;
    }

    public GarmentPageDTO getGarmentTop() {
        return garmentTop;
    }

    public void setGarmentTop(GarmentPageDTO garmentTop) {
        this.garmentTop = garmentTop;
    }

    public GarmentPageDTO getGarmentBottom() {
        return garmentBottom;
    }

    public void setGarmentBottom(GarmentPageDTO garmentBottom) {
        this.garmentBottom = garmentBottom;
    }
}
