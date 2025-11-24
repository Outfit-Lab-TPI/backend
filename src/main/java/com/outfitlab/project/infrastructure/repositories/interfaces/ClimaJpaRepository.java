package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.ClimaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClimaJpaRepository extends JpaRepository<ClimaEntity, Long> {
    Optional<ClimaEntity> findByNombre(String nombre);
    Optional<ClimaEntity> findClimaEntityByNombre(String nombre);

}
