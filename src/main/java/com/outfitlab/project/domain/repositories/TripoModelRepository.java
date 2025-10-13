package com.outfitlab.project.domain.repositories;

import com.outfitlab.project.domain.entities.TripoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TripoModelRepository extends JpaRepository<TripoModel, Long> {

    Optional<TripoModel> findByTaskId(String taskId);

    // Métodos útiles para consultas futuras
    // List<TripoModel> findByStatus(TripoModel.ModelStatus status);
    // List<TripoModel> findByUserId(Long userId);
}
