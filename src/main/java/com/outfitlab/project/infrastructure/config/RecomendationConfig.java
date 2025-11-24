package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.repositories.ClimaRepository;
import com.outfitlab.project.domain.interfaces.repositories.ColorRepository;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRecomendationRepository;
import com.outfitlab.project.domain.interfaces.repositories.OcacionRepository;
import com.outfitlab.project.domain.useCases.garment.GetGarmentRecomendation;
import com.outfitlab.project.domain.useCases.recomendations.GetAllClima;
import com.outfitlab.project.domain.useCases.recomendations.GetAllColors;
import com.outfitlab.project.domain.useCases.recomendations.GetAllOcacion;
import com.outfitlab.project.infrastructure.repositories.ClimaRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.ColorRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.OcacionRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.RecomendationRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.*;
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

    @Bean
    public ColorRepository colorRepository(ColorJpaRepository colorJpaRepository){
        return new ColorRepositoryImpl(colorJpaRepository);
    }

    @Bean
    public OcacionRepository ocacionRepository(OcacionJpaRepository ocacionJpaRepository){
        return new OcacionRepositoryImpl(ocacionJpaRepository);
    }

    @Bean
    public GetAllClima getAllClima(ClimaRepository climaRepository){
        return new GetAllClima(climaRepository);
    }

    @Bean
    public GetAllOcacion getAllOcacion(OcacionRepository ocacionRepository){
        return new GetAllOcacion(ocacionRepository);
    }

    @Bean
    public GetAllColors getAllColors(ColorRepository colorRepository){
        return new GetAllColors(colorRepository);
    }

    @Bean
    public ClimaRepository climaRepository(ClimaJpaRepository climaJpaRepository){
        return new ClimaRepositoryImpl(climaJpaRepository);
    }
}
