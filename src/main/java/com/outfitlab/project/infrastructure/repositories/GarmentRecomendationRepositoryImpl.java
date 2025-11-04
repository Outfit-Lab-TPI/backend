package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRecomendationRepository;
import com.outfitlab.project.domain.model.GarmentRecomendationModel;
import com.outfitlab.project.infrastructure.repositories.interfaces.GarmentRecomendationJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GarmentRecomendationRepositoryImpl implements GarmentRecomendationRepository {

    private final GarmentRecomendationJpaRepository jpaRepository;
    private final GarmentRecomendationMapper mapper;

    public GarmentRecomendationRepositoryImpl(GarmentRecomendationJpaRepository jpaRepository, GarmentRecomendationMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<GarmentRecomendationModel> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<GarmentRecomendationModel> findRecomendationsByGarmentCode(String garmentCode) throws GarmentNotFoundException {
        // This method needs to be implemented based on how recommendations are stored and retrieved.
        // For now, returning an empty list or throwing an exception.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
