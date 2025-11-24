package com.outfitlab.project.domain.useCases.combination;

import com.outfitlab.project.domain.interfaces.repositories.CombinationRepository;
import com.outfitlab.project.domain.exceptions.CombinationNotFoundException;
import com.outfitlab.project.domain.model.CombinationModel;

public class GetCombinationByPrendas {

    private final CombinationRepository combinationRepository;

    public GetCombinationByPrendas(CombinationRepository combinationRepository) {
        this.combinationRepository = combinationRepository;
    }

    public CombinationModel execute(Long prendaSuperiorId, Long prendaInferiorId)
            throws CombinationNotFoundException {

        return combinationRepository
                .findByPrendas(prendaSuperiorId, prendaInferiorId)
                .orElseThrow(() -> new CombinationNotFoundException("Combinación no encontrada"));
    }
}
