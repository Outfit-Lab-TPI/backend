package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.TripoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripoJpaRepository extends JpaRepository<TripoEntity, Long> {

    TripoEntity findByTaskId(String taskId);
}
