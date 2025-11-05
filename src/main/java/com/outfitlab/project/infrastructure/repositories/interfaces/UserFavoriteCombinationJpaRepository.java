package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.UserFavoriteCombinationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio JPA para UserFavoriteCombinationEntity.
 * Extiende JpaRepository para obtener métodos CRUD automáticos.
 */
public interface UserFavoriteCombinationJpaRepository extends JpaRepository<UserFavoriteCombinationEntity, Long> {
    
    /**
     * Busca todos los favoritos activos de un usuario.
     */
    List<UserFavoriteCombinationEntity> findByUserIdAndIsActiveTrue(Long userId);
    
    /**
     * Cuenta cuántos favoritos activos tiene un usuario.
     */
    int countByUserIdAndIsActiveTrue(Long userId);
}
