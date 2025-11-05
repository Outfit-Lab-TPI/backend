package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.domain.model.enums.SubscriptionStatus;
import com.outfitlab.project.infrastructure.model.UserSubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para UserSubscriptionEntity.
 * Extiende JpaRepository para obtener métodos CRUD automáticos.
 */
public interface UserSubscriptionJpaRepository extends JpaRepository<UserSubscriptionEntity, Long> {
    
    /**
     * Busca la suscripción activa de un usuario.
     */
    Optional<UserSubscriptionEntity> findByUserIdAndStatus(Long userId, SubscriptionStatus status);
    
    /**
     * Busca todas las suscripciones que han vencido.
     * (endDate < now AND status = PREMIUM_ACTIVE)
     */
    @Query("SELECT s FROM UserSubscriptionEntity s WHERE s.endDate < :now AND s.status = :status")
    List<UserSubscriptionEntity> findExpiredSubscriptions(LocalDateTime now, SubscriptionStatus status);
}
