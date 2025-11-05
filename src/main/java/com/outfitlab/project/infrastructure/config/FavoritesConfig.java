package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.repositories.GarmentRecomendationRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserFavoriteCombinationRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.useCases.favorites.AddFavoriteCombination;
import com.outfitlab.project.domain.useCases.favorites.GetUserFavorites;
import com.outfitlab.project.domain.useCases.favorites.RemoveFavoriteCombination;
import com.outfitlab.project.infrastructure.repositories.UserFavoriteCombinationRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.interfaces.GarmentRecomendationJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserFavoriteCombinationJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de beans para el módulo de favoritos.
 * Define todos los beans necesarios siguiendo arquitectura hexagonal.
 */
@Configuration
public class FavoritesConfig {

    // ========== ADAPTADORES (Infrastructure Layer) ==========

    /**
     * Repositorio de combinaciones favoritas.
     */
    @Bean
    public UserFavoriteCombinationRepository userFavoriteCombinationRepository(
            UserFavoriteCombinationJpaRepository jpaRepo,
            UserJpaRepository userJpaRepo,
            GarmentRecomendationJpaRepository garmentJpaRepo) {
        return new UserFavoriteCombinationRepositoryImpl(jpaRepo, userJpaRepo, garmentJpaRepo);
    }

    // ========== CASOS DE USO (Domain Layer) ==========

    /**
     * Caso de uso: Agregar favorito.
     */
    @Bean
    public AddFavoriteCombination addFavoriteCombination(
            UserRepository userRepo,
            UserFavoriteCombinationRepository favoriteRepo,
            GarmentRecomendationRepository recomendationRepo) {
        return new AddFavoriteCombination(userRepo, favoriteRepo, recomendationRepo);
    }

    /**
     * Caso de uso: Obtener favoritos de usuario.
     */
    @Bean
    public GetUserFavorites getUserFavorites(
            UserFavoriteCombinationRepository favoriteRepo) {
        return new GetUserFavorites(favoriteRepo);
    }

    /**
     * Caso de uso: Eliminar favorito.
     */
    @Bean
    public RemoveFavoriteCombination removeFavoriteCombination(
            UserFavoriteCombinationRepository favoriteRepo) {
        return new RemoveFavoriteCombination(favoriteRepo);
    }
}
