package com.outfitlab.project.domain.useCases.recomendations;

import com.outfitlab.project.domain.interfaces.repositories.OcacionRepository;
import com.outfitlab.project.domain.model.OcasionModel;

import java.util.List;

public class GetAllOcacion {

    private final OcacionRepository ocacionRepository;

    public GetAllOcacion(OcacionRepository ocacionRepository) {
        this.ocacionRepository = ocacionRepository;
    }

    public List<OcasionModel> execute(){
        return this.ocacionRepository.findAllOcasiones();
    }
}
