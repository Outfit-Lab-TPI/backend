package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.ColorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ColorJpaRepository extends JpaRepository<ColorEntity, Long> {
    Optional<ColorEntity> findByNombre(String nombre);
    Optional<ColorEntity> findColorEntityByNombre(String colorNombre);

}
