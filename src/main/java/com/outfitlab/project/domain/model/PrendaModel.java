package com.outfitlab.project.domain.model;

import com.outfitlab.project.infrastructure.model.MarcaEntity;

public class PrendaModel {

    private String nombre;
    private MarcaEntity marca;
    private String tipo;
    private String imagenUrl;

    public PrendaModel(String nombre, MarcaEntity marca, String tipo, String imagenUrl) {
        this.nombre = nombre;
        this.marca = marca;
        this.tipo = tipo;
        this.imagenUrl = imagenUrl;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public MarcaEntity getMarca() {
        return marca;
    }

    public void setMarca(MarcaEntity marca) {
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
}
