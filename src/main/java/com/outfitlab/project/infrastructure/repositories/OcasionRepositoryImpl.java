package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.exceptions.OcasionNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.OcacionRepository;
import com.outfitlab.project.domain.model.OcasionModel;
import com.outfitlab.project.infrastructure.model.OcasionEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.OcasionJpaRepository;

import java.util.List;

public class OcasionRepositoryImpl implements OcacionRepository {


    private final OcasionJpaRepository ocasionJpaRepository;

    public OcasionRepositoryImpl(OcasionJpaRepository ocasionJpaRepository) {
        this.ocasionJpaRepository = ocasionJpaRepository;
    }

    @Override
    public List<OcasionModel> findAllOcasiones() {
        List<OcasionEntity> ocaciones = this.ocasionJpaRepository.findAll();
        if (ocaciones.isEmpty()) throw new OcasionNotFoundException("No encontramos ocaciones.");
        return ocaciones.stream().map(OcasionEntity::convertEntityToModel)
                .toList();
    }
}
