package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.infrastructure.model.TripoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ITripoRepository extends JpaRepository<TripoEntity, Long> {

    Optional<TripoEntity> findByTaskId(String taskId);

    // Métodos útiles para consultas futuras
    // List<TripoModel> findByStatus(TripoModel.ModelStatus status);
    // List<TripoModel> findByUserId(Long userId);
}
