package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.interfaces.repositories.CombinationRepository;
import com.outfitlab.project.domain.model.CombinationModel;
import com.outfitlab.project.infrastructure.model.CombinationEntity;
import com.outfitlab.project.infrastructure.model.MarcaEntity;
import com.outfitlab.project.infrastructure.model.PrendaEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.CombinationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CombinationRepositoryImpl implements CombinationRepository {

    private final CombinationJpaRepository jpaRepository;

    @Override
    public Optional<CombinationModel> findById(Long id) {
        return jpaRepository.findById(id)
                .map(this::entityToModel);
    }

    @Override
    public Optional<CombinationModel> findByPrendas(Long prendaSuperiorId, Long prendaInferiorId) {
        return jpaRepository
                .findByPrendas(prendaSuperiorId, prendaInferiorId)
                .map(this::entityToModel);
    }

    @Override
    public CombinationModel save(CombinationModel model) {
        CombinationEntity entity = modelToEntity(model);
        CombinationEntity saved = jpaRepository.save(entity);
        return entityToModel(saved);
    }

    @Override
    public void deleteAllByGarmentcode(String garmentCode) {
        this.jpaRepository.deleteAllByGarmentCode(garmentCode);
    }

    private CombinationModel entityToModel(CombinationEntity entity) {
        CombinationModel model = new CombinationModel(
                PrendaEntity.convertToModel(entity.getPrendaSuperior()),
               PrendaEntity.convertToModel(entity.getPrendaInferior())
        );

        model.setId(entity.getId());

        return model;
    }

    private CombinationEntity modelToEntity(CombinationModel model) {
        PrendaEntity sup = new PrendaEntity();
        sup.setId(model.getPrendaSuperior().getId());
        MarcaEntity marcaSup = model.getPrendaSuperior().getMarca() != null
                ? MarcaEntity.convertToEntityWithoutPrendas(model.getPrendaSuperior().getMarca())
                : null;
        sup.setMarca(marcaSup);

        PrendaEntity inf = new PrendaEntity();
        inf.setId(model.getPrendaInferior().getId());
        MarcaEntity marcaInf = model.getPrendaInferior().getMarca() != null
                ? MarcaEntity.convertToEntityWithoutPrendas(model.getPrendaInferior().getMarca())
                : null;
        inf.setMarca(marcaInf);

        CombinationEntity entity = new CombinationEntity(sup, inf);

        if (model.getId() != null) {
            entity.setId(model.getId());
        }

        return entity;
    }

}
