package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.model.UserFavoriteCombinationModel;

import java.util.List;

/**
 * Puerto (interface) para repositorio de combinaciones favoritas de usuario.
 * Define las operaciones de persistencia para UserFavoriteCombinationModel.
 */
public interface UserFavoriteCombinationRepository {
    
    /**
     * Guarda una nueva combinación favorita.
     * 
     * @param model Modelo de favorito a guardar
     * @return Modelo guardado con ID asignado
     */
    UserFavoriteCombinationModel save(UserFavoriteCombinationModel model);
    
    /**
     * Obtiene todas las combinaciones favoritas activas de un usuario.
     * 
     * @param userId ID del usuario
     * @return Lista de favoritos activos (isActive = true)
     */
    List<UserFavoriteCombinationModel> findActiveByUserId(Long userId);
    
    /**
     * Cuenta cuántos favoritos activos tiene un usuario.
     * Usado para validar límites según su plan de suscripción.
     * 
     * @param userId ID del usuario
     * @return Cantidad de favoritos activos
     */
    int countActiveByUserId(Long userId);
    
    /**
     * Elimina (soft delete) una combinación favorita.
     * Marca isActive = false en lugar de borrar el registro.
     * 
     * @param id ID del favorito a eliminar
     */
    void deleteById(Long id);
}
