package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.repositories.*;
import com.outfitlab.project.domain.useCases.garment.GetGarmentRecomendation;
import com.outfitlab.project.domain.useCases.recomendations.*;
import com.outfitlab.project.infrastructure.repositories.*;
import com.outfitlab.project.infrastructure.repositories.interfaces.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecomendationConfig {

    @Bean
    public GarmentRecomendationRepository RecomendationRepository(RecomendationJpaRepository recomendationJpaRepository, GarmentJpaRepository garmentJpaRepository) {
        return new RecomendationRepositoryImpl(recomendationJpaRepository, garmentJpaRepository);
    }

    @Bean
    public GetGarmentRecomendation getGarmentRecomendation(GarmentRecomendationRepository garmentRecomendationRepository){
        return new GetGarmentRecomendation(garmentRecomendationRepository);
    }

    @Bean
    public DeleteGarmentRecomentationsRelatedToGarment deleteGarmentRecomentationsRelatedToGarment(GarmentRecomendationRepository garmentRecomendationRepository){
        return new DeleteGarmentRecomentationsRelatedToGarment(garmentRecomendationRepository);
    }

    @Bean
    public PrendaOcacionRepository prendaOcacionRepository(PrendaOcacionJpaRepository prendaOcacionJpaRepository){
        return new PrendaOcacionRepositoryImpl(prendaOcacionJpaRepository);
    }

    @Bean
    public CreateSugerenciasByGarmentsCode createSugerenciasByGarmentsCode(GarmentRecomendationRepository garmentRecomendationRepository){
        return new CreateSugerenciasByGarmentsCode(garmentRecomendationRepository);
    }

    @Bean
    public DeleteAllPrendaOcacionRelatedToGarment deleteAllPrendaOcacionRelatedToGarment(PrendaOcacionRepository prendaOcacionRepository){
        return new DeleteAllPrendaOcacionRelatedToGarment(prendaOcacionRepository);
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
