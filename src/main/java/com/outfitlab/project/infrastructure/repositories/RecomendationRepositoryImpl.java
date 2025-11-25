package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRecomendationRepository;
import com.outfitlab.project.domain.model.GarmentRecomendationModel;
import com.outfitlab.project.infrastructure.model.GarmentRecomendationEntity;
import com.outfitlab.project.infrastructure.model.PrendaEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.GarmentJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.RecomendationJpaRepository;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class RecomendationRepositoryImpl implements GarmentRecomendationRepository {

    private final RecomendationJpaRepository recomendationJpaRepository;
    private final GarmentJpaRepository garmentJpaRepository;

    public RecomendationRepositoryImpl(RecomendationJpaRepository recomendationJpaRepository, GarmentJpaRepository garmentJpaRepository) {
        this.recomendationJpaRepository = recomendationJpaRepository;
        this.garmentJpaRepository = garmentJpaRepository;
    }

    @Override
    public List<GarmentRecomendationModel> findRecomendationsByGarmentCode(String garmentCode) throws GarmentNotFoundException {
        return getGarmentRecomendationEntities(getPrendaEntity(garmentCode)).stream()
                .map(GarmentRecomendationEntity::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRecomendationsByGarmentCode(String garmentCode) {
        this.recomendationJpaRepository.deleteAllByGarmentCode(garmentCode);
    }

    private List<GarmentRecomendationEntity> getGarmentRecomendationEntities(PrendaEntity garment) {
        List<GarmentRecomendationEntity> recomendations;

        if (garment.getTipo().equalsIgnoreCase("SUPERIOR"))
            recomendations = this.recomendationJpaRepository.findByTopGarment(garment); // si es superior, bsco combinaciones buscando por superior
        else
            recomendations = this.recomendationJpaRepository.findByBottomGarment(garment); // y si es inferior, al revéz, busco combinaciones por inferior

        return recomendations;
    }

    @NotNull
    private PrendaEntity getPrendaEntity(String garmentCode) throws GarmentNotFoundException {
        PrendaEntity garment = this.garmentJpaRepository.findByGarmentCode(garmentCode);

        if (garment == null) throw new GarmentNotFoundException("No encontramos la prenda con el código: " + garmentCode);
        return garment;
    }

}
