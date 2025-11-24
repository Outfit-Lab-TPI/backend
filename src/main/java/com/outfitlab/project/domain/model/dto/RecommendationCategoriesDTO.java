package com.outfitlab.project.domain.model.dto;

import com.outfitlab.project.domain.model.ClimaModel;
import com.outfitlab.project.domain.model.ColorModel;
import com.outfitlab.project.domain.model.OcasionModel;
import java.util.List;

public class RecommendationCategoriesDTO {

    private List<ClimaModel> climas;
    private List<OcasionModel> ocasiones;
    private List<ColorModel> colores;

    public RecommendationCategoriesDTO(List<ClimaModel> climas, List<OcasionModel> ocasiones, List<ColorModel> colores) {
        this.climas = climas;
        this.ocasiones = ocasiones;
        this.colores = colores;
    }

    public List<ClimaModel> getClimas() {
        return climas;
    }

    public void setClimas(List<ClimaModel> climas) {
        this.climas = climas;
    }

    public List<OcasionModel> getOcasiones() {
        return ocasiones;
    }

    public void setOcasiones(List<OcasionModel> ocasiones) {
        this.ocasiones = ocasiones;
    }

    public List<ColorModel> getColores() {
        return colores;
    }

    public void setColores(List<ColorModel> colores) {
        this.colores = colores;
    }
}