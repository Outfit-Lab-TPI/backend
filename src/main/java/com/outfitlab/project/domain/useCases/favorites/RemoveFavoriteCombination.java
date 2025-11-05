package com.outfitlab.project.domain.useCases.favorites;

import com.outfitlab.project.domain.interfaces.repositories.UserFavoriteCombinationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Caso de uso: Eliminar combinación favorita (soft delete).
 * 
 * FLUJO:
 * 1. Marcar favorito como inactivo (isActive = false)
 */
public class RemoveFavoriteCombination {

    private static final Logger logger = LoggerFactory.getLogger(RemoveFavoriteCombination.class);

    private final UserFavoriteCombinationRepository favoriteRepository;

    public RemoveFavoriteCombination(UserFavoriteCombinationRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    /**
     * Ejecuta el caso de uso.
     * 
     * @param favoriteId ID del favorito a eliminar
     */
    public void execute(Long favoriteId) {
        logger.info("Eliminando favorito. Favorite ID: {}", favoriteId);

        favoriteRepository.deleteById(favoriteId);

        logger.info("Favorito eliminado exitosamente. Favorite ID: {}", favoriteId);
    }
}
