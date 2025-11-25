package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.interfaces.repositories.PrendaOcacionRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.PrendaOcacionJpaRepository;

public class PrendaOcacionRepositoryImpl implements PrendaOcacionRepository {

    private final PrendaOcacionJpaRepository prendaOcacionJpaRepository;

    public PrendaOcacionRepositoryImpl(PrendaOcacionJpaRepository prendaOcacionJpaRepository){
        this.prendaOcacionJpaRepository = prendaOcacionJpaRepository;
    }

    @Override
    public void deleteAllPrendaOcacionByGarment(String garmentCode) {
        this.prendaOcacionJpaRepository.deleteAllByGarmentCode(garmentCode);
    }
}
