package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.repositories.GarmentRecomendationRepository;
import com.outfitlab.project.domain.useCases.garment.GetGarmentRecomendation;
import com.outfitlab.project.infrastructure.repositories.RecomendationRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.GarmentJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.RecomendationJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecomendationConfig {

    @Bean
    public GarmentRecomendationRepository RecomendationRepository(RecomendationJpaRepository recomendationJpaRepository, GarmentJpaRepository garmentJpaRepository) {
        return new RecomendationRepository(recomendationJpaRepository, garmentJpaRepository);
    }

    @Bean
    public GetGarmentRecomendation getGarmentRecomendation(GarmentRecomendationRepository garmentRecomendationRepository){
        return new GetGarmentRecomendation(garmentRecomendationRepository);
    }
}
