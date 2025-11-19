package com.outfitlab.project.domain.model;

public class PrendaModel {

    private String nombre;
    private BrandModel marca;
    private String tipo;
    private String imagenUrl;
    private String garmentCode;
    private String evento;
    private String color;

    public PrendaModel() {}

    public PrendaModel(String nombre, BrandModel marca, String tipo, String imagenUrl, String garmentCode, String evento, String color) {
        this.nombre = nombre;
        this.marca = marca;
        this.tipo = tipo;
        this.imagenUrl = imagenUrl;
        this.garmentCode = garmentCode;
        this.evento = evento;
        this.color = color;
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

    public BrandModel getMarca() {
        return marca;
    }

    public void setMarca(BrandModel marca) {
        this.marca = marca;
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
}
