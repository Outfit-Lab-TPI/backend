package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.MarcaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IJpaMarcaRepository extends JpaRepository<MarcaEntity, Long> {
    MarcaEntity findByCodigoMarca(String codigoMarca);
}
