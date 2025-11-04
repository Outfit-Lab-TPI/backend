package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.interfaces.repositories.BrandRepository;
import com.outfitlab.project.domain.model.BrandModel;
import com.outfitlab.project.infrastructure.model.MarcaEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.BrandJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Collectors;

public class BrandRepositoryImpl implements BrandRepository {

    private final int PAGE_SIZE = 10;
    private final BrandJpaRepository jpaMarcaRepository;

    public BrandRepositoryImpl(BrandJpaRepository marcaRepository) {
        this.jpaMarcaRepository = marcaRepository;
    }

    @Override
    public BrandModel findByBrandCode(String brandCode) {
        return MarcaEntity.convertToModelWithoutPrendas(this.jpaMarcaRepository.findByCodigoMarca(brandCode));
    }

    @Override
    public Page<BrandModel> getAllBrands(int page) {
        return this.jpaMarcaRepository.findAll(PageRequest.of(page, PAGE_SIZE))
                .map(MarcaEntity::convertToModelWithoutPrendas);
    }
}
