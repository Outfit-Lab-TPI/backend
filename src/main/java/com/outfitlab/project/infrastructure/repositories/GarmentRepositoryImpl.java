package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.model.dto.GarmentDTO;
import com.outfitlab.project.domain.model.dto.PageDTO;
import com.outfitlab.project.infrastructure.model.MarcaEntity;
import com.outfitlab.project.infrastructure.model.PrendaEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.BrandJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.GarmentJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

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
    public void createGarment(String name, String type, String color, String event, String brandCode, String imageUrl, String garmentCode) {
        MarcaEntity brandEntity = this.brandJpaRepository.findByCodigoMarca(brandCode);
        if (brandEntity == null) throw new GarmentNotFoundException("No encontramos la brand: " + brandCode);
        this.garmentJpaRepository.save(new PrendaEntity(name, brandEntity, type, imageUrl, garmentCode, color, event));
    }

    @Override
    public void deleteGarment(String garmentCode) {
        this.garmentJpaRepository.deleteByGarmentCode(garmentCode);
    }
}
