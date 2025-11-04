package com.outfitlab.project.domain.model.dto;

import com.outfitlab.project.domain.model.PrendaModel;

public class GarmentDTO {
    private String nombre;
    private String tipo;
    private String imagenUrl;
    private String garmentCode;
    private String marcaNombre;

    public GarmentDTO(String nombre, String tipo, String imagenUrl, String garmentCode, String marcaNombre) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.imagenUrl = imagenUrl;
        this.garmentCode = garmentCode;
        this.marcaNombre = marcaNombre;
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

    public String getMarcaNombre() {
        return marcaNombre;
    }

    public void setMarcaNombre(String marcaNombre) {
        this.marcaNombre = marcaNombre;
    }

    public static GarmentDTO convertModelToDTO(PrendaModel prendaModel) {
        return new GarmentDTO(
                prendaModel.getNombre(),
                prendaModel.getTipo(),
                prendaModel.getImagenUrl(),
                prendaModel.getGarmentCode(),
                prendaModel.getMarca().getNombre()
        );
    }
}
