package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.interfaces.repositories.CombinationAttemptRepository;
import com.outfitlab.project.domain.model.*;
import com.outfitlab.project.infrastructure.model.ColorEntity;
import com.outfitlab.project.infrastructure.model.CombinationAttemptEntity;
import com.outfitlab.project.infrastructure.model.MarcaEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.CombinationAttemptJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.CombinationJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CombinationAttemptRepositoryImpl implements CombinationAttemptRepository {

    private final CombinationAttemptJpaRepository attemptJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final CombinationJpaRepository combinationJpaRepository;

    public CombinationAttemptRepositoryImpl(
            CombinationAttemptJpaRepository attemptJpaRepository,
            UserJpaRepository userJpaRepository,
            CombinationJpaRepository combinationJpaRepository
    ) {
        this.attemptJpaRepository = attemptJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.combinationJpaRepository = combinationJpaRepository;
    }

    @Override
    public Long save(CombinationAttemptModel model) {
        var userEntity = model.getUser() != null ?
                userJpaRepository.findById(model.getUser().getId())
                        .orElseThrow(() -> new RuntimeException("User not found")) : null;

        var combinationEntity = combinationJpaRepository.findById(model.getCombination().getId())
                .orElseThrow(() -> new RuntimeException("Combination not found"));

        var entity = new CombinationAttemptEntity(
                userEntity,
                combinationEntity,
                model.getImageUrl()
        );

        return attemptJpaRepository.save(entity).getId();
    }

    @Override
    public List<CombinationAttemptModel> findAllByPrenda(Long prendaId) {
        List<CombinationAttemptEntity> entities = attemptJpaRepository
                .findByCombination_PrendaSuperior_IdOrCombination_PrendaInferior_Id(prendaId, prendaId);

        return entities.stream().map(this::mapToModel).toList();
    }

    @Override
    public List<CombinationAttemptModel> findLastNDays(int days) {
        LocalDateTime fromDate = LocalDateTime.now().minusDays(days);
        List<CombinationAttemptEntity> entities = attemptJpaRepository.findByCreatedAtAfter(fromDate);
        return entities.stream().map(this::mapToModel).toList();
    }

    @Override
    public List<CombinationAttemptModel> findAll() {
        List<CombinationAttemptEntity> entities = attemptJpaRepository.findAll();
        return entities.stream().map(this::mapToModel).toList();
    }

    // --- Método privado para mapear entity -> model ---
    private CombinationAttemptModel mapToModel(CombinationAttemptEntity entity) {
        PrendaModel sup = new PrendaModel(
                entity.getCombination().getPrendaSuperior().getId(),
                entity.getCombination().getPrendaSuperior().getNombre(),
                entity.getCombination().getPrendaSuperior().getImagenUrl(),
                MarcaEntity.convertToModel(entity.getCombination().getPrendaSuperior().getMarca())
        );
        sup.setColor(ColorEntity.convertEntityToModel(entity.getCombination().getPrendaSuperior().getColor()));

        PrendaModel inf = new PrendaModel(
                entity.getCombination().getPrendaInferior().getId(),
                entity.getCombination().getPrendaInferior().getNombre(),
                entity.getCombination().getPrendaInferior().getImagenUrl(),
                MarcaEntity.convertToModel(entity.getCombination().getPrendaInferior().getMarca())
        );
        inf.setColor(ColorEntity.convertEntityToModel(entity.getCombination().getPrendaInferior().getColor()));

        CombinationModel combinationModel = new CombinationModel(sup, inf);
        combinationModel.setId(entity.getCombination().getId());

        UserModel userModel = entity.getUser() != null ?
                new UserModel(entity.getUser().getId(), entity.getUser().getEmail()) : null;

        return new CombinationAttemptModel(
                userModel,
                combinationModel,
                entity.getCreatedAt(),
                entity.getImageUrl()
        );
    }
}
