package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.interfaces.repositories.GarmentRecomendationRepository;
import com.outfitlab.project.domain.model.GarmentRecomendationModel;
import com.outfitlab.project.infrastructure.model.GarmentRecomendationEntity;
import com.outfitlab.project.infrastructure.model.PrendaEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.GarmentJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.RecomendationJpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public class RecomendationRepository implements GarmentRecomendationRepository {

    private final RecomendationJpaRepository recomendationJpaRepository;
    private final GarmentJpaRepository garmentJpaRepository;

    public RecomendationRepository(RecomendationJpaRepository recomendationJpaRepository, GarmentJpaRepository garmentJpaRepository) {
        this.recomendationJpaRepository = recomendationJpaRepository;
        this.garmentJpaRepository = garmentJpaRepository;
    }

    @Override
    public List<GarmentRecomendationModel> findRecomendationsByGarmentCode(String garmentCode) {
        PrendaEntity garment = this.garmentJpaRepository.findByGarmentCode(garmentCode);

        List<GarmentRecomendationEntity> recomendations;

        if (garment.getTipo().equalsIgnoreCase("SUPERIOR")) {
            recomendations = this.recomendationJpaRepository.findByTopGarment(garment.getId()); // si es superior, bsco combinaciones buscando por superior
        } else{
            recomendations = this.recomendationJpaRepository.findByBottomGarment(garment.getId()); // y si es inferior, al revéz, busco combinaciones por inferior
        }

        return recomendations.stream()
                .map(GarmentRecomendationEntity::convertToModel)
                .collect(Collectors.toList());
    }

}
