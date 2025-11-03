package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.MarcaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandJpaRepository extends JpaRepository<MarcaEntity, Long> {
    MarcaEntity findByCodigoMarca(String codigoMarca);
}
