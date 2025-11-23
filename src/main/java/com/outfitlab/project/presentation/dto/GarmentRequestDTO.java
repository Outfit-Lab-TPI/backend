package com.outfitlab.project.presentation.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class GarmentRequestDTO {

    private String nombre;
    private String tipo;
    private String colorNombre;
    private String evento;
    private MultipartFile imagen;
    private String climaNombre;
    private List<String> ocasionesNombres;
    private String codigoMarca;

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

    public String getColorNombre() {
        return colorNombre;
    }

    public void setColorNombre(String color) {
        this.colorNombre = color;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public MultipartFile getImagen() {
        return imagen;
    }

    public void setImagen(MultipartFile imagen) {
        this.imagen = imagen;
    }

    public String getClimaNombre() {
        return climaNombre;
    }

    public void setClimaNombre(String climaNombre) {
        this.climaNombre = climaNombre;
    }

    public List<String> getOcasionesNombres() {
        return ocasionesNombres;
    }

    public void setOcasionesNombres(List<String> ocasionesNombres) {}

    public String getCodigoMarca() {
        return codigoMarca;
    }
    public void setCodigoMarca(String codigoMarca) {
        this.codigoMarca = codigoMarca;
    }
}

