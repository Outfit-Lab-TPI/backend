package com.outfitlab.project.domain.model.dto;

import com.outfitlab.project.domain.model.BrandModel;

import java.util.List;

public class BrandDTO {
    private String codigoMarca;
    private String nombre;
    private String logoUrl;

    public BrandDTO(){}

    public BrandDTO(String codigoMarca, String nombre, String logoUrl) {
        this.codigoMarca = codigoMarca;
        this.nombre = nombre;
        this.logoUrl = logoUrl;
    }

    public static BrandDTO convertModelToDTO(BrandModel brandModel) {
        return new BrandDTO(brandModel.getCodigoMarca(), brandModel.getNombre(), brandModel.getLogoUrl());
    }

    public static List<BrandDTO> convertListModelToListDTO(List<BrandModel> brandModel) {
        return brandModel.stream()
                .map(BrandDTO::convertModelToDTO)
                .toList();
    }

    public String getCodigoMarca() {
        return codigoMarca;
    }

    public void setCodigoMarca(String codigoMarca) {
        this.codigoMarca = codigoMarca;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
