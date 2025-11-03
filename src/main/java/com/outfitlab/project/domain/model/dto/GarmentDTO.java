package com.outfitlab.project.domain.model.dto;

import com.outfitlab.project.domain.model.PrendaModel;

public class GarmentDTO {
    private String nombre;
    private String tipo;
    private String imagenUrl;
    private String garmentCode;

    public GarmentDTO(String nombre, String tipo, String imagenUrl, String garmentCode) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.imagenUrl = imagenUrl;
        this.garmentCode = garmentCode;
    }

    public String getGarmentCode() {
        return garmentCode;
    }

    public void setGarmentCode(String garmentCode) {
        this.garmentCode = garmentCode;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public static GarmentDTO convertModelToDTO(PrendaModel prendaModel) {
        return new GarmentDTO(
                prendaModel.getNombre(),
                prendaModel.getTipo(),
                prendaModel.getImagenUrl(),
                prendaModel.getGarmentCode()
        );
    }
}
