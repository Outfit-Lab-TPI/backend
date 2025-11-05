package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.interfaces.repositories.UserSubscriptionRepository;
import com.outfitlab.project.domain.model.UserSubscriptionModel;
import com.outfitlab.project.domain.model.enums.SubscriptionStatus;
import com.outfitlab.project.infrastructure.model.SubscriptionPlanEntity;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.model.UserSubscriptionEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.SubscriptionPlanJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserSubscriptionJpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del puerto UserSubscriptionRepository.
 * Adaptador que usa Spring Data JPA para persistencia.
 */
public class UserSubscriptionRepositoryImpl implements UserSubscriptionRepository {

    private final UserSubscriptionJpaRepository jpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final SubscriptionPlanJpaRepository planJpaRepository;

    public UserSubscriptionRepositoryImpl(UserSubscriptionJpaRepository jpaRepository,
                                           UserJpaRepository userJpaRepository,
                                           SubscriptionPlanJpaRepository planJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.planJpaRepository = planJpaRepository;
    }

    @Override
    public UserSubscriptionModel save(UserSubscriptionModel model) {
        // Obtener entidades relacionadas
        UserEntity userEntity = userJpaRepository.findById(model.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + model.getUserId()));
        
        SubscriptionPlanEntity planEntity = planJpaRepository.findById(model.getPlanId())
                .orElseThrow(() -> new RuntimeException("Plan no encontrado: " + model.getPlanId()));

        // Crear o actualizar entidad
        UserSubscriptionEntity entity;
        if (model.getId() != null) {
            entity = jpaRepository.findById(model.getId())
                    .orElse(new UserSubscriptionEntity());
        } else {
            entity = new UserSubscriptionEntity();
        }

        // Mapear campos usando setters
        entity.setUser(userEntity);
        entity.setPlan(planEntity);
        entity.setMercadoPagoPaymentId(model.getMercadoPagoPaymentId());
        entity.setStatus(model.getStatus());
        entity.setStartDate(model.getStartDate());
        entity.setEndDate(model.getEndDate());
        entity.setAutoRenew(model.getAutoRenew());

        // Guardar y convertir a modelo
        UserSubscriptionEntity saved = jpaRepository.save(entity);
        return UserSubscriptionEntity.toModel(saved);
    }

    @Override
    public UserSubscriptionModel findActiveByUserId(Long userId) {
        return jpaRepository.findByUserIdAndStatus(userId, SubscriptionStatus.PREMIUM_ACTIVE)
                .map(UserSubscriptionEntity::toModel)
                .orElse(null);
    }

    @Override
    public List<UserSubscriptionModel> findExpiredSubscriptions() {
        return jpaRepository.findExpiredSubscriptions(LocalDateTime.now(), SubscriptionStatus.PREMIUM_ACTIVE)
                .stream()
                .map(UserSubscriptionEntity::toModel)
                .collect(Collectors.toList());
    }
}
