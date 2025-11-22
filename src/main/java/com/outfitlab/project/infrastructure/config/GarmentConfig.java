package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.port.GeminiClient;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.useCases.garment.GetGarmentRecomendationByText;
import com.outfitlab.project.infrastructure.repositories.GeminiClientImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.outfitlab.project.domain.interfaces.repositories.BrandRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserGarmentFavoriteRepository;
import com.outfitlab.project.domain.useCases.garment.*;
import com.outfitlab.project.infrastructure.repositories.GarmentRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.UserGarmentFavoriteRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.interfaces.BrandJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.GarmentJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserGarmentFavoriteJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;

@Configuration
public class GarmentConfig {

    @Bean
    public GarmentRepository garmentRepository(GarmentJpaRepository jpaRepository, BrandJpaRepository brandJpaRepository) {return new GarmentRepositoryImpl(jpaRepository, brandJpaRepository);}

    @Bean
    public UserGarmentFavoriteRepository userGarmentFavoriteRepository(UserGarmentFavoriteJpaRepository jpaRepository,
                                                                       UserJpaRepository userJpaRepository,
                                                                       GarmentJpaRepository garmentJpaRepository) {
        return new UserGarmentFavoriteRepositoryImpl(jpaRepository, userJpaRepository, garmentJpaRepository);
    }

    @Bean
    public AddGarmentToFavorite addGarmentToFavourite(UserGarmentFavoriteRepository userGarmentFavoriteRepository){
        return new AddGarmentToFavorite(userGarmentFavoriteRepository);
    }

    @Bean
    public GetGarmentByCode getGarmentByCode(GarmentRepository garmentRepository){
        return new GetGarmentByCode(garmentRepository);
    }

    @Bean
    public UpdateGarment updateGarment(GarmentRepository garmentRepository, BrandRepository brandRepository){
        return new UpdateGarment(garmentRepository, brandRepository);
    }

    @Bean
    public DeleteGarment deleteGarment(GarmentRepository garmentRepository, BrandRepository brandRepository) {
        return new DeleteGarment(garmentRepository, brandRepository);
    }

    @Bean
    public CreateGarment createGarment(GarmentRepository garmentRepository){
        return new CreateGarment(garmentRepository);
    }

    @Bean
    public DeleteGarmentFromFavorite deleteGarmentFromFavorite(UserGarmentFavoriteRepository userGarmentFavoriteRepository){
        return new DeleteGarmentFromFavorite(userGarmentFavoriteRepository);
    }

    @Bean
    public GetGarmentsFavoritesForUserByEmail getGarmentsFavoritesForUserByEmail(UserGarmentFavoriteRepository userGarmentFavoriteRepository){
        return new GetGarmentsFavoritesForUserByEmail(userGarmentFavoriteRepository);
    }

    @Bean
    public GetGarmentsByType getTopGarments(GarmentRepository garmentRepository) {
        return new GetGarmentsByType(garmentRepository);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Bean
    public String geminiApiKey(@Value("${gemini.api.key}") String apiKey) {
        return apiKey;
    }

    @Bean
    public GeminiClient geminiClient(ObjectMapper objectMapper, String geminiApiKey) {
        return new GeminiClientImpl(objectMapper, geminiApiKey);
    }

    @Bean
    public GetGarmentRecomendationByText getGarmentRecomendationByText(
            GeminiClient geminiClient,
            GarmentRepository garmentRepository) {

        return new GetGarmentRecomendationByText(geminiClient, garmentRepository);
    }
}
