package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.repositories.GarmentRecomendationRepository;
import com.outfitlab.project.domain.useCases.garment.GetGarmentRecomendation;
import com.outfitlab.project.infrastructure.repositories.GarmentRecomendationRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.interfaces.GarmentRecomendationJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RecomendationConfig {

    @Bean
    @Primary
    public GarmentRecomendationRepository garmentRecomendationRepository(
                GarmentRecomendationJpaRepository garmentRecomendationJpaRepository) {
            return new GarmentRecomendationRepositoryImpl(garmentRecomendationJpaRepository);
        }

    @Bean
    public GetGarmentRecomendation getGarmentRecomendation(GarmentRecomendationRepository garmentRecomendationRepository){
        return new GetGarmentRecomendation(garmentRecomendationRepository);
    }
}
