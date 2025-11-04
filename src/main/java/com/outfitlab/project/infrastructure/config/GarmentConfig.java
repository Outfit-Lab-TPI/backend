package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserGarmentFavoriteRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.useCases.garment.AddGarmentToFavorite;
import com.outfitlab.project.domain.useCases.garment.GetGarmentsByType;
import com.outfitlab.project.infrastructure.repositories.GarmentRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.UserGarmentFavoriteRempositoryImpl;
import com.outfitlab.project.infrastructure.repositories.UserRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.interfaces.GarmentJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserGarmentFavoriteJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GarmentConfig {

    @Bean
    public GarmentRepository garmentRepository(GarmentJpaRepository jpaRepository) {return new GarmentRepositoryImpl(jpaRepository);}

    @Bean
    public UserGarmentFavoriteRepository userGarmentFavoriteRepository(UserGarmentFavoriteJpaRepository jpaRepository,
                                                                       UserJpaRepository userJpaRepository,
                                                                       GarmentJpaRepository garmentJpaRepository) {
        return new UserGarmentFavoriteRempositoryImpl(jpaRepository, userJpaRepository, garmentJpaRepository);
    }

    @Bean
    public AddGarmentToFavorite addGarmentToFavourite(UserGarmentFavoriteRepository userGarmentFavoriteRepository){
        return new AddGarmentToFavorite(userGarmentFavoriteRepository);
    }

    @Bean
    public GetGarmentsByType getTopGarments(GarmentRepository garmentRepository) {
        return new GetGarmentsByType(garmentRepository);
    }
}
