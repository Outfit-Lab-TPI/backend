package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.interfaces.repositories.PrendaOcasionRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.PrendaOcasionJpaRepository;

public class PrendaOcasionRepositoryImpl implements PrendaOcasionRepository {

    private final PrendaOcasionJpaRepository prendaOcasionJpaRepository;

    public PrendaOcasionRepositoryImpl(PrendaOcasionJpaRepository prendaOcasionJpaRepository){
        this.prendaOcasionJpaRepository = prendaOcasionJpaRepository;
    }

    @Override
    public void deleteAllPrendaOcasionByGarment(String garmentCode) {
        this.prendaOcasionJpaRepository.deleteAllByGarmentCode(garmentCode);
    }
}
