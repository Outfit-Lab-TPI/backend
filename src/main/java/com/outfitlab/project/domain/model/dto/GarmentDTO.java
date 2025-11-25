package com.outfitlab.project.domain.model.dto;

import com.outfitlab.project.domain.model.PrendaModel;

public class GarmentDTO {
    private String nombre;
    private String tipo;
    private String imagenUrl;
    private String garmentCode;
    private String marcaNombre;
    private String evento;
    private String color;
    private String clima;
    private String genero;

    public GarmentDTO(String nombre, String tipo, String imagenUrl, String garmentCode, String marcaNombre, String color, String clima) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.imagenUrl = imagenUrl;
        this.garmentCode = garmentCode;
        this.marcaNombre = marcaNombre;
        this.color = color;
        this.clima = clima;
    }

    public GarmentDTO(String nombre, String tipo, String imagenUrl, String garmentCode, String marcaNombre, String color, String clima, String genero) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.imagenUrl = imagenUrl;
        this.garmentCode = garmentCode;
        this.marcaNombre = marcaNombre;
        this.color = color;
        this.clima = clima;
        this.genero = genero;
    }

    public String getGarmentCode() {
        return garmentCode;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
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

    public String getEvento() {
        return evento;
    }
    public void setEvento(String evento) {
        this.evento = evento;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public String getClima() {
        return clima;
    }

    public void setClima(String clima) {
        this.clima = clima;
    }

    public static GarmentDTO convertModelToDTO(PrendaModel prendaModel) {
        return new GarmentDTO(
                prendaModel.getNombre(),
                prendaModel.getTipo(),
                prendaModel.getImagenUrl(),
                prendaModel.getGarmentCode(),
                prendaModel.getMarca().getNombre(),
                prendaModel.getColor().getNombre(),
                prendaModel.getClimaAdecuado().getNombre(),
                prendaModel.getGenero()
        );
    }

    @Override
    public String toString() {
        return "GarmentDTO{" +
                "nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", imagenUrl='" + imagenUrl + '\'' +
                ", garmentCode='" + garmentCode + '\'' +
                ", marcaNombre='" + marcaNombre + '\'' +
                ", evento='" + evento + '\'' +
                ", color='" + color + '\'' +
                ", clima='" + clima + '\'' +
                ", genero='" + genero + '\'' +
                '}';
    }
}
