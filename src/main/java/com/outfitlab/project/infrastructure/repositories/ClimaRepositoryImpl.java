package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.exceptions.ClimaNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.ClimaRepository;
import com.outfitlab.project.domain.model.ClimaModel;
import com.outfitlab.project.infrastructure.model.ClimaEntity;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.ClimaJpaRepository;

import java.util.List;

public class ClimaRepositoryImpl implements ClimaRepository {

    private final ClimaJpaRepository climaJpaRepository;

    public ClimaRepositoryImpl(ClimaJpaRepository climaJpaRepository) {
        this.climaJpaRepository = climaJpaRepository;
    }

    @Override
    public List<ClimaModel> findAllClimas() {
        List<ClimaEntity> climas = this.climaJpaRepository.findAll();
        if (climas.isEmpty()) throw new ClimaNotFoundException("No encontramos climas.");
        return climas.stream().map(ClimaEntity::convertEntityToModel)
                .toList();
    }
}
