package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.useCases.garment.GetGarmentsByType;
import com.outfitlab.project.infrastructure.repositories.GarmentRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.interfaces.GarmentJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GarmentConfig {

    @Bean
    public GarmentRepository garmentRepository(GarmentJpaRepository jpaRepo) {return new GarmentRepositoryImpl(jpaRepo);}

    @Bean
    public GetGarmentsByType getTopGarments(GarmentRepository garmentRepository) {
        return new GetGarmentsByType(garmentRepository);
    }
}
