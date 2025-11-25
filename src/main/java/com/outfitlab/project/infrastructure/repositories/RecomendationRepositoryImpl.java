package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRecomendationRepository;
import com.outfitlab.project.domain.model.GarmentRecomendationModel;
import com.outfitlab.project.infrastructure.model.GarmentRecomendationEntity;
import com.outfitlab.project.infrastructure.model.PrendaEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.GarmentJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.RecomendationJpaRepository;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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

    @Override
    public void createSugerenciasByGarmentCode(String garmentCode, String type, List<String> sugerencias) {

        PrendaEntity prendaPrincipal = this.garmentJpaRepository.findByGarmentCode(garmentCode);
        if(prendaPrincipal == null) throw new GarmentNotFoundException("Prenda principal no encontrada");

        List<GarmentRecomendationEntity> sugerenciasToCeate = getGarmentRecomendationEntitiesToCreate(type, sugerencias, prendaPrincipal);
        this.recomendationJpaRepository.saveAll(sugerenciasToCeate);
    }

    private List<GarmentRecomendationEntity> getGarmentRecomendationEntitiesToCreate(String type, List<String> sugerencias, PrendaEntity prendaPrincipal) {
        List<GarmentRecomendationEntity> sugerenciasToCeate = new ArrayList<>();

        for (String sugeridaCode : sugerencias) {

            PrendaEntity prendaSugerida = this.getPrendaEntity(sugeridaCode);

            GarmentRecomendationEntity nueva = new GarmentRecomendationEntity();

            if (type.equalsIgnoreCase("inferior")) {
                nueva.setBottomGarment(prendaPrincipal);
                nueva.setTopGarment(prendaSugerida);
            } else if (type.equalsIgnoreCase("superior")){
                nueva.setTopGarment(prendaPrincipal);
                nueva.setBottomGarment(prendaSugerida);
            }else{
                throw new GarmentNotFoundException("Tipo de prenda inválido: " + type);
            }
            sugerenciasToCeate.add(nueva);
        }
        return sugerenciasToCeate;
    }

    private List<GarmentRecomendationEntity> getGarmentRecomendationEntities(PrendaEntity garment) {
        List<GarmentRecomendationEntity> recomendations;

        if (garment.getTipo().equalsIgnoreCase("SUPERIOR"))
            recomendations = this.recomendationJpaRepository.findByTopGarment(garment); // si es superior, bsco combinaciones buscando por superior
        else
            recomendations = this.recomendationJpaRepository.findByBottomGarment(garment); // y si es inferior, al revéz, busco combinaciones por inferior

        return recomendations;
    }

    private PrendaEntity getPrendaEntity(String garmentCode) throws GarmentNotFoundException {
        PrendaEntity garment = this.garmentJpaRepository.findByGarmentCode(garmentCode);

        if (garment == null) throw new GarmentNotFoundException("No encontramos la prenda con el código: " + garmentCode);
        return garment;
    }

}
