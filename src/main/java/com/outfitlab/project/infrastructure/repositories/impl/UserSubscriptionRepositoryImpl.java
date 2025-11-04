package com.outfitlab.project.infrastructure.repositories.impl;

import com.outfitlab.project.domain.interfaces.repositories.UserSubscriptionRepository;
import com.outfitlab.project.domain.model.SubscriptionPlanModel;
import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.domain.model.UserSubscriptionModel;
import com.outfitlab.project.infrastructure.model.SubscriptionPlanEntity;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.model.UserSubscriptionEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserSubscriptionJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserSubscriptionRepositoryImpl implements UserSubscriptionRepository {

    private final UserSubscriptionJpaRepository jpaRepository;

    public UserSubscriptionRepositoryImpl(UserSubscriptionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public UserSubscriptionModel save(UserSubscriptionModel userSubscriptionModel) {
        UserSubscriptionEntity entity = toEntity(userSubscriptionModel);
        UserSubscriptionEntity savedEntity = jpaRepository.save(entity);
        return toModel(savedEntity);
    }

    private UserSubscriptionModel toModel(UserSubscriptionEntity entity) {
        UserModel userModel = new UserModel(
                entity.getUser().getId(),
                entity.getUser().getSatulation(),
                entity.getUser().getName(),
                entity.getUser().getSecondName(),
                entity.getUser().getLastName(),
                entity.getUser().getYears(),
                entity.getUser().getEmail(),
                entity.getUser().getSubscriptionStatus(),
                entity.getUser().getSubscriptionExpiresAt()
        );

        SubscriptionPlanModel planModel = new SubscriptionPlanModel();
        planModel.setId(entity.getPlan().getId());
        planModel.setPlanCode(entity.getPlan().getPlanCode());
        planModel.setName(entity.getPlan().getName());
        planModel.setPrice(entity.getPlan().getPrice());
        planModel.setMaxFavorites(entity.getPlan().getMaxFavorites());
        planModel.setDurationDays(entity.getPlan().getDurationDays());
        planModel.setIsActive(entity.getPlan().getIsActive());

        UserSubscriptionModel model = new UserSubscriptionModel();
        model.setId(entity.getId());
        model.setUser(userModel);
        model.setPlan(planModel);
        model.setMercadoPagoPaymentId(entity.getMercadoPagoPaymentId());
        model.setStatus(entity.getStatus());
        model.setStartDate(entity.getStartDate());
        model.setEndDate(entity.getEndDate());
        model.setAutoRenew(entity.getAutoRenew());
        
        return model;
    }

    private UserSubscriptionEntity toEntity(UserSubscriptionModel model) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(model.getUser().getId());
        // Set other user properties if necessary for saving

        SubscriptionPlanEntity planEntity = new SubscriptionPlanEntity();
        planEntity.setId(model.getPlan().getId());
        // Set other plan properties if necessary for saving

        UserSubscriptionEntity entity = new UserSubscriptionEntity();
        entity.setId(model.getId());
        entity.setUser(userEntity);
        entity.setPlan(planEntity);
        entity.setMercadoPagoPaymentId(model.getMercadoPagoPaymentId());
        entity.setStatus(model.getStatus());
        entity.setStartDate(model.getStartDate());
        entity.setEndDate(model.getEndDate());
        entity.setAutoRenew(model.getAutoRenew());
        return entity;
    }
}
