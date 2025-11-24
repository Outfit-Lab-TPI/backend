package com.outfitlab.project.domain.useCases.recomendations;

import com.outfitlab.project.domain.interfaces.repositories.ClimaRepository;
import com.outfitlab.project.domain.model.ClimaModel;

import java.util.List;

public class GetAllClima {

    private final ClimaRepository climaRepository;

    public GetAllClima(ClimaRepository climaRepository) {
        this.climaRepository = climaRepository;
    }

    public List<ClimaModel> execute(){
        return this.climaRepository.findAllClimas();
    }
}
