package com.outfitlab.project.domain.useCases.combination;

import com.outfitlab.project.domain.interfaces.repositories.CombinationRepository;
import com.outfitlab.project.domain.model.CombinationModel;
import com.outfitlab.project.domain.model.PrendaModel;

public class CreateCombination {

    private final CombinationRepository combinationRepository;

    public CreateCombination(CombinationRepository combinationRepository) {
        this.combinationRepository = combinationRepository;
    }

    public CombinationModel execute(PrendaModel prendaSuperior, PrendaModel prendaInferior) {
        var model = new CombinationModel(
                prendaSuperior,
                prendaInferior
        );

        return combinationRepository.save(model);
    }
}
