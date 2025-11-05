package com.outfitlab.project.domain.useCases.subscription;

import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserFavoriteCombinationRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.domain.model.enums.SubscriptionStatus;
import com.outfitlab.project.presentation.dto.SubscriptionStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Caso de uso: Obtener estado de suscripción de un usuario.
 * 
 * FLUJO:
 * 1. Buscar usuario por ID
 * 2. Contar favoritos activos
 * 3. Calcular límite según status de suscripción
 * 4. Retornar DTO con información completa
 */
public class GetSubscriptionStatus {

    private static final Logger logger = LoggerFactory.getLogger(GetSubscriptionStatus.class);

    private final UserRepository userRepository;
    private final UserFavoriteCombinationRepository favoriteRepository;

    public GetSubscriptionStatus(UserRepository userRepository,
                                   UserFavoriteCombinationRepository favoriteRepository) {
        this.userRepository = userRepository;
        this.favoriteRepository = favoriteRepository;
    }

    /**
     * Ejecuta el caso de uso.
     * 
     * @param userId ID del usuario
     * @return DTO con estado de suscripción y límites de favoritos
     * @throws UserNotFoundException Si el usuario no existe
     */
    public SubscriptionStatusResponse execute(Long userId) throws UserNotFoundException {
        logger.info("Obteniendo estado de suscripción. User ID: {}", userId);

        // 1. Buscar usuario
        UserModel user = userRepository.findById(userId);
        if (user == null) {
            logger.error("Usuario no encontrado: {}", userId);
            throw new UserNotFoundException("Usuario no encontrado: " + userId);
        }

        // 2. Contar favoritos activos
        int currentFavorites = favoriteRepository.countActiveByUserId(userId);

        // 3. Calcular límite según status
        int maxFavorites = getMaxFavoritesForStatus(user.getSubscriptionStatus());

        // 4. Calcular restantes
        int remainingFavorites = Math.max(0, maxFavorites - currentFavorites);

        logger.info("Estado de suscripción obtenido. User ID: {}, Status: {}, Favoritos: {}/{}", 
            userId, user.getSubscriptionStatus(), currentFavorites, maxFavorites);

        return new SubscriptionStatusResponse(
            user.getSubscriptionStatus(),
            user.getSubscriptionExpiresAt(),
            currentFavorites,
            maxFavorites,
            remainingFavorites
        );
    }

    /**
     * Retorna el máximo de favoritos según el estado de suscripción.
     */
    private int getMaxFavoritesForStatus(SubscriptionStatus status) {
        if (status == null) {
            return 2; // Default: FREE
        }
        
        return switch (status) {
            case FREE, PREMIUM_EXPIRED -> 2;
            case PREMIUM_ACTIVE -> 20;
        };
    }
}
