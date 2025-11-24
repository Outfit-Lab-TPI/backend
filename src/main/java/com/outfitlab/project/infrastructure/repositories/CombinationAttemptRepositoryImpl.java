package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.interfaces.repositories.CombinationAttemptRepository;
import com.outfitlab.project.domain.model.CombinationAttemptModel;
import com.outfitlab.project.infrastructure.model.CombinationAttemptEntity;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.CombinationAttemptJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.CombinationJpaRepository;
import org.springframework.stereotype.Repository;

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

        UserEntity user = null;

        if (model.getUser() != null) {
            user = userJpaRepository.findById(model.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        var combination = combinationJpaRepository.findById(model.getCombination().getId())
                .orElseThrow(() -> new RuntimeException("Combination not found"));

        var entity = new CombinationAttemptEntity(
                user,
                combination,
                model.getImageUrl()
        );

        var saved = attemptJpaRepository.save(entity);
        return saved.getId();
    }
}
