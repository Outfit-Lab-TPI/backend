package com.outfitlab.project.domain.model;

import java.util.Set;

public class PrendaModel {
    private Long id;
    private String nombre;
    private BrandModel marca;
    private String tipo;
    private String imagenUrl;
    private String garmentCode;
    private ColorModel color;
    private ClimaModel climaAdecuado;
    private Set<OcasionModel> ocasiones;
    private String genero;

    public PrendaModel() {}

    public PrendaModel(String nombre, BrandModel marca, String tipo, String imagenUrl, String garmentCode, ColorModel color, ClimaModel climaAdecuado, Set<OcasionModel> ocasiones) {
        this.nombre = nombre;
        this.marca = marca;
        this.tipo = tipo;
        this.imagenUrl = imagenUrl;
        this.garmentCode = garmentCode;
        this.color = color;
        this.climaAdecuado = climaAdecuado;
        this.ocasiones = ocasiones;
    }


    public PrendaModel(Long id, String nombre, String imagenUrl, BrandModel marca) {
        this.id = id;
        this.nombre = nombre;
        this.imagenUrl = imagenUrl;
        this.marca = marca;
    }

    public PrendaModel(String nombre, BrandModel marcaModel, String tipo, String imagenUrl, String garmentCode, ColorModel colorModel, ClimaModel climaModel, Set<OcasionModel> ocasionesModels, String genero) {
        this.nombre = nombre;
        this.marca = marcaModel;
        this.tipo = tipo;
        this.imagenUrl = imagenUrl;
        this.garmentCode = garmentCode;
        this.color = colorModel;
        this.climaAdecuado = climaModel;
        this.ocasiones = ocasionesModels;
        this.genero = genero;
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

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

    public ColorModel getColor() {
        return color;
    }
    public void setColor(ColorModel color) {
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

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}