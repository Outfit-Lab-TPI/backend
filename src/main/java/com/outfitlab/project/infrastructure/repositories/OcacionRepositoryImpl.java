package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.exceptions.ClimaNotFoundException;
import com.outfitlab.project.domain.exceptions.OcasionNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.OcacionRepository;
import com.outfitlab.project.domain.model.OcasionModel;
import com.outfitlab.project.infrastructure.model.ClimaEntity;
import com.outfitlab.project.infrastructure.model.OcasionEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.OcacionJpaRepository;

import java.util.List;

public class OcacionRepositoryImpl implements OcacionRepository {

    private final OcacionJpaRepository ocacionJpaRepository;

    public OcacionRepositoryImpl(OcacionJpaRepository ocacionJpaRepository) {
        this.ocacionJpaRepository = ocacionJpaRepository;
    }

    @Override
    public List<OcasionModel> findAllOcasiones() {
        List<OcasionEntity> ocaciones = this.ocacionJpaRepository.findAll();
        if (ocaciones.isEmpty()) throw new OcasionNotFoundException("No encontramos ocaciones.");
        return ocaciones.stream().map(OcasionEntity::convertEntityToModel)
                .toList();
    }
}
