package com.outfitlab.project.domain.useCases.recomendations;

import com.outfitlab.project.domain.interfaces.repositories.PrendaOcasionRepository;

public class DeleteAllPrendaOcacionRelatedToGarment {

    private final PrendaOcasionRepository prendaOcacionRepository;

    public DeleteAllPrendaOcacionRelatedToGarment(PrendaOcasionRepository prendaOcacionRepository){
        this.prendaOcacionRepository = prendaOcacionRepository;
    }

    public void execute(String garmentCode){
        this.prendaOcacionRepository.deleteAllPrendaOcasionByGarment(garmentCode);
    }
}
