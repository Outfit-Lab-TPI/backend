package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.ColorEntity;
import com.outfitlab.project.infrastructure.model.PrendaEntity;
import com.outfitlab.project.infrastructure.model.ClimaEntity;
import com.outfitlab.project.infrastructure.model.OcasionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GarmentJpaRepository extends JpaRepository<PrendaEntity, Long> {
    PrendaEntity findByGarmentCode(String garmentCode);

    Page<PrendaEntity> findByMarca_CodigoMarcaAndTipo(String brandCode, String tipo, Pageable pageable);

    Page<PrendaEntity> findByTipo(String type, Pageable pageable);

    void deleteByGarmentCode(String garmentCode);

    List<PrendaEntity> findByClimaAdecuado_NombreAndOcasiones_Nombre(
            String climaNombre,
            String ocasionNombre
    );

    @Query("SELECT c FROM ClimaEntity c")
    List<ClimaEntity> findAllClimaEntities();

    @Query("SELECT o FROM OcasionEntity o")
    List<OcasionEntity> findAllOcasionEntities();

    @Query("SELECT c FROM ColorEntity c")
    List<ColorEntity> findAllColorEntities();

    Optional<ClimaEntity> findClimaEntityByNombre(String nombre);

    Optional<OcasionEntity> findOcasionEntityByNombre(String nombre);

    Optional<ColorEntity> findColorEntityByNombre(String colorNombre);
}
