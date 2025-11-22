package com.outfitlab.project.domain.model.dto;

import com.outfitlab.project.domain.model.ClimaModel;
import com.outfitlab.project.domain.model.OcasionModel;
import java.util.List;

public class RecommendationCategoriesDTO {

    private List<ClimaModel> climas;
    private List<OcasionModel> ocasiones;

    public RecommendationCategoriesDTO(List<ClimaModel> climas, List<OcasionModel> ocasiones) {
        this.climas = climas;
        this.ocasiones = ocasiones;
    }

    public List<ClimaModel> getClimas() { return climas; }
    public void setClimas(List<ClimaModel> climas) { this.climas = climas; }
    public List<OcasionModel> getOcasiones() { return ocasiones; }
    public void setOcasiones(List<OcasionModel> ocasiones) { this.ocasiones = ocasiones; }
}