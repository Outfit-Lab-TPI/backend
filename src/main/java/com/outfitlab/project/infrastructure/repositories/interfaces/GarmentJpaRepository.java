package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.PrendaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GarmentJpaRepository extends JpaRepository<PrendaEntity, Long> {
    PrendaEntity findByGarmentCode(String garmentCode);
    List<PrendaEntity> findAllByGarmentCode(String garmentCode, Pageable pageable);

    Page<PrendaEntity> findByMarca_CodigoMarcaAndTipo(String brandCode, String tipo, Pageable pageable);

    Page<PrendaEntity> findByTipo(String type, Pageable pageable);
}
