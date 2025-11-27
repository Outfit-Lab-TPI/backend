package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.model.ClimaModel;
import com.outfitlab.project.domain.model.ColorModel;
import com.outfitlab.project.domain.model.OcasionModel;
import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.model.dto.GarmentDTO;
import com.outfitlab.project.domain.model.dto.PageDTO;
import com.outfitlab.project.infrastructure.model.*;
import com.outfitlab.project.infrastructure.repositories.interfaces.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GarmentRepositoryImpl implements GarmentRepository {

    private final int PAGE_SIZE = 10;
    private final GarmentJpaRepository garmentJpaRepository;
    private final BrandJpaRepository brandJpaRepository;
    private final ColorJpaRepository colorJpaRepository;
    private final ClimaJpaRepository climaJpaRepository;
    private final OcasionJpaRepository ocacionJpaRepository;

    public GarmentRepositoryImpl(GarmentJpaRepository garmentJpaRepository, BrandJpaRepository brandJpaRepository, ColorJpaRepository colorJpaRepository, ClimaJpaRepository climaJpaRepository, OcasionJpaRepository ocacionJpaRepository) {
        this.garmentJpaRepository = garmentJpaRepository;
        this.brandJpaRepository = brandJpaRepository;
        this.colorJpaRepository = colorJpaRepository;
        this.climaJpaRepository = climaJpaRepository;
        this.ocacionJpaRepository = ocacionJpaRepository;
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
        if (entity == null)
            throw new GarmentNotFoundException("No encontramos la prenda con el código: " + garmentCode);
        return PrendaEntity.convertToModel(entity);
    }

    @Override
    public void createGarment(
            String name,
            String garmentCode,
            String type,
            String colorNombre,
            String brandCode,
            String imageUrl,
            String climaNombre,
            List<String> ocasionesNombres,
            String genero
    ) {
        MarcaEntity brandEntity = this.brandJpaRepository.findByCodigoMarca(brandCode);

        if (brandEntity == null) throw new GarmentNotFoundException("No encontramos la brand: " + brandCode);

        ClimaEntity climaEntity = this.climaJpaRepository.findClimaEntityByNombre(climaNombre)
                .orElseThrow(() -> new IllegalArgumentException("Clima no válido: " + climaNombre));

        ColorEntity colorEntity = this.colorJpaRepository.findColorEntityByNombre(colorNombre)
                .orElseThrow(() -> new IllegalArgumentException("Color no válido: " + colorNombre));

        Set<OcasionEntity> ocasionesEntities = ocasionesNombres.stream()
                .map(nombre -> this.ocacionJpaRepository.findOcasionEntityByNombre(nombre)
                        .orElseThrow(() -> new IllegalArgumentException("Ocasión no válida: " + nombre)))
                .collect(Collectors.toSet());

        this.garmentJpaRepository.save(new PrendaEntity(
                name,
                brandEntity,
                type,
                imageUrl,
                garmentCode,
                colorEntity,
                climaEntity,
                ocasionesEntities,
                genero
        ));
    }

    @Override
    public void deleteGarment(String garmentCode) {
        this.garmentJpaRepository.deleteByGarmentCode(garmentCode);
    }

    @Override
    public void updateGarment(String name, String type, String colorNombre, String event, String garmentCode, String imageUrl, String newGarmentCode, String climaNombre, List<String> ocasionesNombres, String genero) {
        PrendaEntity garmentEntity = this.garmentJpaRepository.findByGarmentCode(garmentCode);
        if (garmentEntity == null) throw new GarmentNotFoundException("No encontramos la prenda: " + garmentCode);
        ColorEntity colorEntity = this.colorJpaRepository.findColorEntityByNombre(colorNombre)
                .orElseThrow(() -> new IllegalArgumentException("Color no válido: " + colorNombre));
        ClimaEntity climaEntity = this.climaJpaRepository.findClimaEntityByNombre(climaNombre)
                .orElseThrow(() -> new IllegalArgumentException("Clima no válido: " + climaNombre));
        Set<OcasionEntity> ocasionesEntities = ocasionesNombres.stream()
                .map(nombre -> this.ocacionJpaRepository.findOcasionEntityByNombre(nombre)
                        .orElseThrow(() -> new IllegalArgumentException("Ocasión no válida: " + nombre)))
                .collect(Collectors.toSet());


        garmentEntity.setNombre(name);
        garmentEntity.setTipo(type);
        garmentEntity.setColor(colorEntity);
        garmentEntity.setGarmentCode(newGarmentCode);
        garmentEntity.setOcasiones(ocasionesEntities);
        garmentEntity.setClimaAdecuado(climaEntity);
        garmentEntity.setGenero(genero.toLowerCase());

        if (!imageUrl.isEmpty())
            garmentEntity.setImagenUrl(imageUrl); // la voy a actualizar solo si vino algo, si vino empty es pq no le actualizaron la img

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
        return garmentJpaRepository.findAllOcasionEntities().stream()
                .map(entity -> new OcasionModel(entity.getId(), entity.getNombre()))
                .collect(Collectors.toList());
    }

    @Override
    public List<PrendaModel> findAll() {
        List<PrendaEntity> entities = garmentJpaRepository.findAll();
        return PrendaEntity.convertToListModel(entities);
    }

    @Override
    public List<ColorModel> findAllColores() {
        return garmentJpaRepository.findAllColorEntities().stream()
                .map(entity -> new ColorModel(entity.getId(), entity.getNombre(), entity.getValor()))
                .collect(Collectors.toList());
    }
}
