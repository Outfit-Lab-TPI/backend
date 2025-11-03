package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.repositories.BrandRepository;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.service.MarcaService;
import com.outfitlab.project.domain.useCases.marca.GetAllBrands;
import com.outfitlab.project.domain.useCases.marca.GetBrandAndGarmentsByBrandCode;
import com.outfitlab.project.infrastructure.repositories.BrandRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.GarmentRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.interfaces.BrandJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.GarmentJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MarcaConfig {

    @Bean
    public BrandRepository marcaRepository(BrandJpaRepository jpaRepo) {
        return new BrandRepositoryImpl(jpaRepo);
    }

    @Bean
    public GarmentRepository garmentRepository(GarmentJpaRepository jpaRepo) {return new GarmentRepositoryImpl(jpaRepo);}

    @Bean
    public GetBrandAndGarmentsByBrandCode getMarcaByCodigoMarca(BrandRepository marcaRepository, GarmentRepository garmentRepository) {
        return new GetBrandAndGarmentsByBrandCode(marcaRepository, garmentRepository);
    }

    @Bean
    public GetAllBrands getAllMarcas(BrandRepository marcaRepository) {
        return new GetAllBrands(marcaRepository);
    }

    @Bean
    public MarcaService marcaService(GetAllBrands getAllMarcas, GetBrandAndGarmentsByBrandCode getMarcaByCodigoMarca) {
        return new MarcaService(getAllMarcas, getMarcaByCodigoMarca);
    }
}
