package com.outfitlab.project.domain.useCases.recomendations;

import com.outfitlab.project.domain.interfaces.repositories.PrendaOcacionRepository;

public class DeleteAllPrendaOcacionRelatedToGarment {

    private final PrendaOcacionRepository prendaOcacionRepository;

    public DeleteAllPrendaOcacionRelatedToGarment(PrendaOcacionRepository prendaOcacionRepository){
        this.prendaOcacionRepository = prendaOcacionRepository;
    }

    public void execute(String garmentCode){
        this.prendaOcacionRepository.deleteAllPrendaOcacionByGarment(garmentCode);
    }
}
