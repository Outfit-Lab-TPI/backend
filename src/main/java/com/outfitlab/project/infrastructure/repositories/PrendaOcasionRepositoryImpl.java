package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.interfaces.repositories.PrendaOcacionRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.PrendaOcacionJpaRepository;

public class PrendaOcasionRepositoryImpl implements PrendaOcacionRepository {

    private final PrendaOcacionJpaRepository prendaOcacionJpaRepository;

    public PrendaOcasionRepositoryImpl(PrendaOcacionJpaRepository prendaOcacionJpaRepository){
        this.prendaOcacionJpaRepository = prendaOcacionJpaRepository;
    }

    @Override
    public void deleteAllPrendaOcacionByGarment(String garmentCode) {
        this.prendaOcacionJpaRepository.deleteAllByGarmentCode(garmentCode);
    }
}
