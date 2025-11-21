package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.model.ClimaModel;
import com.outfitlab.project.domain.model.OcasionModel;
import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.model.dto.GarmentDTO;
import com.outfitlab.project.domain.model.dto.PageDTO;
import com.outfitlab.project.infrastructure.model.ClimaEntity;
import com.outfitlab.project.infrastructure.model.MarcaEntity;
import com.outfitlab.project.infrastructure.model.OcasionEntity;
import com.outfitlab.project.infrastructure.model.PrendaEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.BrandJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.GarmentJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GarmentRepositoryImpl implements GarmentRepository {

    private final int PAGE_SIZE = 10;
    private final GarmentJpaRepository garmentJpaRepository;
    private final BrandJpaRepository brandJpaRepository;

    public GarmentRepositoryImpl(GarmentJpaRepository garmentJpaRepository, BrandJpaRepository brandJpaRepository) {
        this.garmentJpaRepository = garmentJpaRepository;
        this.brandJpaRepository = brandJpaRepository;
    }

    @Override
    public PageDTO findByBrandCodeAndTipo(String brandCode, String tipo, int page) {
        Page<PrendaEntity> pageResult = garmentJpaRepository.findByMarca_CodigoMarcaAndTipo(
                brandCode, tipo.toLowerCase(), PageRequest.of(page, PAGE_SIZE));

        Page<PrendaModel> modelPage = pageResult.map(PrendaEntity::convertToModel);

        List<GarmentDTO> dtoList = modelPage.stream()
                .map(GarmentDTO::convertModelToDTO)
                .toList();

        return new PageDTO(
                dtoList,
                modelPage.getNumber(),
                modelPage.getSize(),
                modelPage.getTotalElements(),
                modelPage.getTotalPages(),
                modelPage.isLast()
        );
    }

    @Override
    public Page<PrendaModel> getGarmentsByType(String type, int page) {
        return this.garmentJpaRepository.findByTipo(type, PageRequest.of(page, PAGE_SIZE))
                .map(PrendaEntity::convertToModel);
    }

    @Override
    public PrendaModel findByGarmentCode(String garmentCode) throws GarmentNotFoundException {
        PrendaEntity entity = this.garmentJpaRepository.findByGarmentCode(garmentCode);
        if (entity == null) throw new GarmentNotFoundException("No encontramos la prenda con el código: " + garmentCode);
        return PrendaEntity.convertToModel(entity);
    }

    @Override
    public void createGarment(
            String name,
            String type,
            String color,
            String brandCode,
            String imageUrl,
            String garmentCode,
            String climaNombre,
            List<String> ocasionesNombres
    ) {
        MarcaEntity brandEntity = this.brandJpaRepository.findByCodigoMarca(brandCode);
        if (brandEntity == null) throw new GarmentNotFoundException("No encontramos la brand: " + brandCode);
        ClimaEntity climaEntity = garmentJpaRepository.findClimaEntityByNombre(climaNombre)
                .orElseThrow(() -> new IllegalArgumentException("Clima no válido: " + climaNombre));

        Set<OcasionEntity> ocasionesEntities = ocasionesNombres.stream()
                .map(nombre -> garmentJpaRepository.findOcasionEntityByNombre(nombre)
                        .orElseThrow(() -> new IllegalArgumentException("Ocasión no válida: " + nombre)))
                .collect(Collectors.toSet());
        this.garmentJpaRepository.save(new PrendaEntity(
                name,
                brandEntity,
                type,
                imageUrl,
                garmentCode,
                color,
                climaEntity,
                ocasionesEntities
        ));
    }

    @Override
    public void deleteGarment(String garmentCode) {
        this.garmentJpaRepository.deleteByGarmentCode(garmentCode);
    }

    @Override
    public void updateGarment(String name, String type, String color, String event, String garmentCode, String imageUrl, String newGarmentCode) {
        PrendaEntity garmentEntity = this.garmentJpaRepository.findByGarmentCode(garmentCode);
        if (garmentEntity == null) throw new GarmentNotFoundException("No encontramos la prenda: " + garmentCode);

        garmentEntity.setNombre(name);
        garmentEntity.setTipo(type);
        garmentEntity.setColor(color);
        garmentEntity.setGarmentCode(newGarmentCode);

        if (!imageUrl.isEmpty()) garmentEntity.setImagenUrl(imageUrl); // la voy a actualizar solo si vino algo, si vino empty es pq no le actualizaron la img

        garmentJpaRepository.save(garmentEntity);
    }

    @Override
    public List<PrendaModel> findByClimaAndOcasion(String climaNombre, String ocasionNombre) {

        List<PrendaEntity> entities = garmentJpaRepository.findByClimaAdecuado_NombreAndOcasiones_Nombre(
                climaNombre,
                ocasionNombre
        );

        return PrendaEntity.convertToListModel(entities);
    }

    @Override
    public List<ClimaModel> findAllClimas() {
        return garmentJpaRepository.findAllClimaEntities().stream()
                .map(entity -> new ClimaModel(entity.getId(), entity.getNombre()))
                .collect(Collectors.toList());
    }

    @Override
    public List<OcasionModel> findAllOcasiones() {
        // CORRECCIÓN: Usar el método de JPA unificado y mapear
        return garmentJpaRepository.findAllOcasionEntities().stream()
                .map(entity -> new OcasionModel(entity.getId(), entity.getNombre()))
                .collect(Collectors.toList());
    }
}
