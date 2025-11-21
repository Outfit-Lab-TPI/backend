package com.outfitlab.project.domain.model;

import java.util.Set;

public class PrendaModel {

    private String nombre;
    private BrandModel marca;
    private String tipo;
    private String imagenUrl;
    private String garmentCode;
    private String color;

    private ClimaModel climaAdecuado;
    private Set<OcasionModel> ocasiones;

    public PrendaModel() {}

    public PrendaModel(String nombre, BrandModel marca, String tipo, String imagenUrl, String garmentCode, String color, ClimaModel climaAdecuado, Set<OcasionModel> ocasiones) {
        this.nombre = nombre;
        this.marca = marca;
        this.tipo = tipo;
        this.imagenUrl = imagenUrl;
        this.garmentCode = garmentCode;
        this.color = color;
        this.climaAdecuado = climaAdecuado;
        this.ocasiones = ocasiones;
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

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public ClimaModel getClimaAdecuado() {
        return climaAdecuado;
    }

    public void setClimaAdecuado(ClimaModel climaAdecuado) {
        this.climaAdecuado = climaAdecuado;
    }

    public Set<OcasionModel> getOcasiones() {
        return ocasiones;
    }

    public void setOcasiones(Set<OcasionModel> ocasiones) {
        this.ocasiones = ocasiones;
    }
}