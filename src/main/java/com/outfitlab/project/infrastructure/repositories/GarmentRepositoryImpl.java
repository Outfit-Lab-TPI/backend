package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.model.dto.GarmentDTO;
import com.outfitlab.project.domain.model.dto.GarmentPageDTO;
import com.outfitlab.project.infrastructure.model.PrendaEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.GarmentJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class GarmentRepositoryImpl implements GarmentRepository {

    private final int PAGE_SIZE = 2;
    private final GarmentJpaRepository garmentJpaRepository;

    public GarmentRepositoryImpl(GarmentJpaRepository garmentJpaRepository) {
        this.garmentJpaRepository = garmentJpaRepository;
    }

    @Override
    public PrendaModel findByBrandCode(String brandCode, int page) {
        return PrendaEntity.convertToModel(this.garmentJpaRepository.findByGarmentCode(brandCode));
    }

    @Override
    public List<PrendaModel> findAllByBrandCode(String brandCode, int page) {
        return PrendaEntity.convertToListModel(this.garmentJpaRepository.findAllByGarmentCode(brandCode, PageRequest.of(page, PAGE_SIZE))
                .stream()
                .toList());
    }

    @Override
    public GarmentPageDTO findByBrandCodeAndTipo(String brandCode, String tipo, int page) {
        Page<PrendaEntity> pageResult = garmentJpaRepository.findByMarca_CodigoMarcaAndTipo(
                brandCode, tipo.toLowerCase(), PageRequest.of(page, PAGE_SIZE));

        Page<PrendaModel> modelPage = pageResult.map(PrendaEntity::convertToModel);

        List<GarmentDTO> dtoList = modelPage.stream()
                .map(GarmentDTO::convertModelToDTO)
                .toList();

        return new GarmentPageDTO(
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
}
