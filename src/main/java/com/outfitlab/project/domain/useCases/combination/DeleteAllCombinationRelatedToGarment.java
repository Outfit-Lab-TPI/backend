package com.outfitlab.project.domain.useCases.combination;

import com.outfitlab.project.domain.interfaces.repositories.CombinationRepository;

public class DeleteAllCombinationRelatedToGarment {

    private final CombinationRepository combinationRepository;

    public DeleteAllCombinationRelatedToGarment(final CombinationRepository combinationRepository) {
        this.combinationRepository = combinationRepository;
    }

    public void execute(String garmentCode) {
        this.combinationRepository.deleteAllByGarmentcode(garmentCode);
    }
}
