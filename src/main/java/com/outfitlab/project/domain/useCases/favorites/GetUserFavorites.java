package com.outfitlab.project.domain.useCases.favorites;

import com.outfitlab.project.domain.interfaces.repositories.UserFavoriteCombinationRepository;
import com.outfitlab.project.domain.model.UserFavoriteCombinationModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Caso de uso: Obtener favoritos de un usuario.
 * 
 * FLUJO:
 * 1. Buscar todos los favoritos activos del usuario
 * 2. Retornar lista
 */
public class GetUserFavorites {

    private static final Logger logger = LoggerFactory.getLogger(GetUserFavorites.class);

    private final UserFavoriteCombinationRepository favoriteRepository;

    public GetUserFavorites(UserFavoriteCombinationRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    /**
     * Ejecuta el caso de uso.
     * 
     * @param userId ID del usuario
     * @return Lista de favoritos activos
     */
    public List<UserFavoriteCombinationModel> execute(Long userId) {
        logger.info("Obteniendo favoritos. User ID: {}", userId);

        List<UserFavoriteCombinationModel> favorites = favoriteRepository.findActiveByUserId(userId);

        logger.info("Favoritos obtenidos. User ID: {}, Total: {}", userId, favorites.size());

        return favorites;
    }
}
