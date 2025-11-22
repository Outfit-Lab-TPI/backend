package com.outfitlab.project.domain.model.dto;

import com.outfitlab.project.domain.model.PrendaModel;
import java.util.List;

public class ConjuntoDTO {

    private String nombre;

    private List<PrendaModel> prendas;

    public ConjuntoDTO() {}

    public ConjuntoDTO(String nombre, List<PrendaModel> prendas) {
        this.nombre = nombre;
        this.prendas = prendas;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<PrendaModel> getPrendas() {
        return prendas;
    }

    public void setPrendas(List<PrendaModel> prendas) {
        this.prendas = prendas;
    }

}
