package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.CombinationAttemptEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CombinationAttemptJpaRepository extends JpaRepository<CombinationAttemptEntity, Long> {
}