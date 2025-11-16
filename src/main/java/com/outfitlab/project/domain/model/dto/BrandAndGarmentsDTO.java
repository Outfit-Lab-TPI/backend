package com.outfitlab.project.domain.model.dto;

public class BrandAndGarmentsDTO {

    private BrandDTO brandDTO;
    private PageDTO garmentTop;
    private PageDTO garmentBottom;

    public BrandAndGarmentsDTO(BrandDTO brandDTO, PageDTO garmentTop, PageDTO garmentBottom) {
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

    public PageDTO getGarmentTop() {
        return garmentTop;
    }

    public void setGarmentTop(PageDTO garmentTop) {
        this.garmentTop = garmentTop;
    }

    public PageDTO getGarmentBottom() {
        return garmentBottom;
    }

    public void setGarmentBottom(PageDTO garmentBottom) {
        this.garmentBottom = garmentBottom;
    }
}
