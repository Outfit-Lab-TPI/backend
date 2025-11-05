package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.repositories.UserCombinationFavoriteRepository;
import com.outfitlab.project.domain.useCases.combination.AddCombinationToFavourite;
import com.outfitlab.project.domain.useCases.combination.DeleteCombinationFromFavorite;
import com.outfitlab.project.domain.useCases.combination.GetCombinationFavoritesForUserByEmail;
import com.outfitlab.project.infrastructure.repositories.UserCombinationFavoriteRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserCombinationFavoriteJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CombinationConfig {

    @Bean
    public UserCombinationFavoriteRepository userCombinationFavoriteRepository(UserCombinationFavoriteJpaRepository jpaRepository,
                                                                           UserJpaRepository userJpaRepository) {
        return new UserCombinationFavoriteRepositoryImpl(jpaRepository, userJpaRepository);
    }

    @Bean
    public AddCombinationToFavourite addCombinationToFavourite(UserCombinationFavoriteRepository userCombinationFavoriteRepository){
        return new AddCombinationToFavourite(userCombinationFavoriteRepository);
    }

    @Bean
    public GetCombinationFavoritesForUserByEmail getCombinationFavoritesForUserByEmail(UserCombinationFavoriteRepository userCombinationFavoriteRepository){
        return new GetCombinationFavoritesForUserByEmail(userCombinationFavoriteRepository);
    }

    @Bean
    public DeleteCombinationFromFavorite deleteCombinationFromFavorite(UserCombinationFavoriteRepository userCombinationFavoriteRepository){
        return new DeleteCombinationFromFavorite(userCombinationFavoriteRepository);
    }
}
