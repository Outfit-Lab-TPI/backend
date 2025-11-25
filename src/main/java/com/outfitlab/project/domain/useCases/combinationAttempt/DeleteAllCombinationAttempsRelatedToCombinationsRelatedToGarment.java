package com.outfitlab.project.domain.useCases.combinationAttempt;

import com.outfitlab.project.domain.interfaces.repositories.CombinationAttemptRepository;

public class DeleteAllCombinationAttempsRelatedToCombinationsRelatedToGarment {

    private final CombinationAttemptRepository combinationAttemptRepository;

    public DeleteAllCombinationAttempsRelatedToCombinationsRelatedToGarment(CombinationAttemptRepository combinationAttemptRepository) {
        this.combinationAttemptRepository = combinationAttemptRepository;
    }

    public void execute(String garmentCode){
        this.combinationAttemptRepository.deleteAllByAttempsReltedToCombinationRelatedToGarments(garmentCode);
    }
}
