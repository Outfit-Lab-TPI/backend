package com.outfitlab.project.domain.useCases.recomendations;

import com.outfitlab.project.domain.interfaces.repositories.ColorRepository;
import com.outfitlab.project.domain.model.ColorModel;

import java.util.List;

public class GetAllColors {

    private final ColorRepository colorRepository;

    public GetAllColors(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    public List<ColorModel> execute(){
        return this.colorRepository.findAllColores();
    }
}
