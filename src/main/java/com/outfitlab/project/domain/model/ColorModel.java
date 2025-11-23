package com.outfitlab.project.domain.model;

public class ColorModel {
    private Long id;
    private String nombre;
    private int valor;

    public ColorModel() {}

    public ColorModel(Long id, String nombre, int valor) {
        this.id = id;
        this.nombre = nombre;
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

}
