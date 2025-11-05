package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.FavoriteLimitExceededException;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.model.UserFavoriteCombinationModel;
import com.outfitlab.project.domain.useCases.favorites.AddFavoriteCombination;
import com.outfitlab.project.domain.useCases.favorites.GetUserFavorites;
import com.outfitlab.project.domain.useCases.favorites.RemoveFavoriteCombination;
import com.outfitlab.project.presentation.dto.AddFavoriteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller para gestión de combinaciones favoritas.
 * 
 * Endpoints:
 * - POST /api/users/{userId}/favorites - Agregar favorito
 * - GET /api/users/{userId}/favorites - Listar favoritos
 * - DELETE /api/users/{userId}/favorites/{favoriteId} - Eliminar favorito
 */
@RestController
@RequestMapping("/api/users/{userId}/favorites")
public class FavoritesController {

    private static final Logger logger = LoggerFactory.getLogger(FavoritesController.class);

    private final AddFavoriteCombination addFavorite;
    private final GetUserFavorites getUserFavorites;
    private final RemoveFavoriteCombination removeFavorite;

    public FavoritesController(AddFavoriteCombination addFavorite,
                                GetUserFavorites getUserFavorites,
                                RemoveFavoriteCombination removeFavorite) {
        this.addFavorite = addFavorite;
        this.getUserFavorites = getUserFavorites;
        this.removeFavorite = removeFavorite;
    }

    /**
     * Agregar combinación a favoritos.
     * 
     * @param userId ID del usuario
     * @param request DTO con garmentRecomendationId
     * @return Favorito creado o error 403 si alcanzó el límite
     */
    @PostMapping
    public ResponseEntity<?> addFavorite(
            @PathVariable Long userId,
            @RequestBody AddFavoriteRequest request) {
        
        try {
            logger.info("POST /api/users/{}/favorites - Request: {}", userId, request.getGarmentRecomendationId());
            
            UserFavoriteCombinationModel favorite = addFavorite.execute(
                userId, 
                request.getGarmentRecomendationId()
            );
            
            return ResponseEntity.ok(favorite);
            
        } catch (FavoriteLimitExceededException e) {
            logger.warn("Límite de favoritos alcanzado para user ID: {}", userId);
            return ResponseEntity.status(403).body(Map.of(
                "error", "LIMIT_EXCEEDED",
                "message", e.getMessage()
            ));
            
        } catch (Exception e) {
            logger.error("Error al agregar favorito", e);
            return ResponseEntity.status(500).body(Map.of(
                "error", "INTERNAL_ERROR",
                "message", "Error al agregar favorito: " + e.getMessage()
            ));
        }
    }

    /**
     * Obtener todos los favoritos de un usuario.
     * 
     * @param userId ID del usuario
     * @return Lista de favoritos activos
     */
    @GetMapping
    public ResponseEntity<?> getFavorites(@PathVariable Long userId) {
        try {
            logger.info("GET /api/users/{}/favorites", userId);
            
            List<UserFavoriteCombinationModel> favorites = getUserFavorites.execute(userId);
            
            return ResponseEntity.ok(favorites);
            
        } catch (Exception e) {
            logger.error("Error al obtener favoritos", e);
            return ResponseEntity.status(500).body(Map.of(
                "error", "INTERNAL_ERROR",
                "message", "Error al obtener favoritos: " + e.getMessage()
            ));
        }
    }

    /**
     * Eliminar un favorito (soft delete).
     * 
     * @param userId ID del usuario (no se usa, solo para RESTful)
     * @param favoriteId ID del favorito a eliminar
     * @return 204 No Content si se eliminó exitosamente
     */
    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<?> removeFavorite(
            @PathVariable Long userId,
            @PathVariable Long favoriteId) {
        
        try {
            logger.info("DELETE /api/users/{}/favorites/{}", userId, favoriteId);
            
            removeFavorite.execute(favoriteId);
            
            return ResponseEntity.noContent().build();
            
        } catch (Exception e) {
            logger.error("Error al eliminar favorito", e);
            return ResponseEntity.status(500).body(Map.of(
                "error", "INTERNAL_ERROR",
                "message", "Error al eliminar favorito: " + e.getMessage()
            ));
        }
    }
}
