package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.OcasionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OcacionJpaRepository extends JpaRepository<OcasionEntity, Long> {
    Optional<OcasionEntity> findByNombre(String nombre);
    Optional<OcasionEntity> findOcasionEntityByNombre(String nombre);

}
