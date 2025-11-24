package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.exceptions.SubscriptionNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserSubscriptionRepository;
import com.outfitlab.project.domain.model.UserSubscriptionModel;
import com.outfitlab.project.infrastructure.model.SubscriptionEntity;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.model.UserSubscriptionEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.SubscriptionJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserSubscriptionJpaRepository;
import org.springframework.transaction.annotation.Transactional;

public class UserSubscriptionRepositoryImpl implements UserSubscriptionRepository {
    private final UserSubscriptionJpaRepository jpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final SubscriptionJpaRepository subscriptionJpaRepository;

    public UserSubscriptionRepositoryImpl(UserSubscriptionJpaRepository jpaRepository,
            UserJpaRepository userJpaRepository,
            SubscriptionJpaRepository subscriptionJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.subscriptionJpaRepository = subscriptionJpaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserSubscriptionModel findByUserEmail(String userEmail) throws SubscriptionNotFoundException {
        return jpaRepository.findByUserEmail(userEmail)
                .map(UserSubscriptionEntity::convertToModel)
                .orElseThrow(() -> new SubscriptionNotFoundException(
                        "No se encontró suscripción para el usuario: " + userEmail));
    }

    @Override
    @Transactional
    public UserSubscriptionModel save(UserSubscriptionModel subscription) {
        UserEntity user = userJpaRepository.findByEmail(subscription.getUserEmail());
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado: " + subscription.getUserEmail());
        }

        SubscriptionEntity subscriptionEntity = subscriptionJpaRepository.findByPlanCode(subscription.getPlanCode())
                .orElseThrow(() -> new RuntimeException("Plan no encontrado: " + subscription.getPlanCode()));

        UserSubscriptionEntity entity = new UserSubscriptionEntity();
        entity.setUser(user);
        entity.setSubscription(subscriptionEntity);
        entity.setCombinationsUsed(subscription.getCombinationsUsed());
        entity.setFavoritesCount(subscription.getFavoritesCount());
        entity.setModelsGenerated(subscription.getModelsGenerated());
        entity.setMaxCombinations(subscription.getMaxCombinations());
        entity.setMaxFavorites(subscription.getMaxFavorites());
        entity.setMaxModels(subscription.getMaxModels());
        entity.setSubscriptionStart(subscription.getSubscriptionStart());
        entity.setSubscriptionEnd(subscription.getSubscriptionEnd());
        entity.setStatus(subscription.getStatus());

        UserSubscriptionEntity saved = jpaRepository.save(entity);
        return UserSubscriptionEntity.convertToModel(saved);
    }

    @Override
    @Transactional
    public UserSubscriptionModel update(UserSubscriptionModel subscription) {
        UserSubscriptionEntity entity = jpaRepository.findByUserEmail(subscription.getUserEmail())
                .orElseThrow(
                        () -> new RuntimeException("Suscripción no encontrada para: " + subscription.getUserEmail()));

        SubscriptionEntity subscriptionEntity = subscriptionJpaRepository.findByPlanCode(subscription.getPlanCode())
                .orElseThrow(() -> new RuntimeException("Plan no encontrado: " + subscription.getPlanCode()));

        entity.setSubscription(subscriptionEntity);
        entity.setCombinationsUsed(subscription.getCombinationsUsed());
        entity.setFavoritesCount(subscription.getFavoritesCount());
        entity.setModelsGenerated(subscription.getModelsGenerated());
        entity.setMaxCombinations(subscription.getMaxCombinations());
        entity.setMaxFavorites(subscription.getMaxFavorites());
        entity.setMaxModels(subscription.getMaxModels());
        entity.setSubscriptionStart(subscription.getSubscriptionStart());
        entity.setSubscriptionEnd(subscription.getSubscriptionEnd());
        entity.setStatus(subscription.getStatus());

        UserSubscriptionEntity updated = jpaRepository.save(entity);
        return UserSubscriptionEntity.convertToModel(updated);
    }

    @Override
    @Transactional
    public void incrementCounter(String userEmail, String counterType) {
        // Obtener el user_id desde el email
        UserEntity user = userJpaRepository.findByEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado: " + userEmail);
        }

        switch (counterType) {
            case "combinations":
                jpaRepository.incrementCombinationsByUserId(user.getId());
                break;
            case "favorites":
                jpaRepository.incrementFavoritesByUserId(user.getId());
                break;
            case "3d_models":
                jpaRepository.incrementModelsByUserId(user.getId());
                break;
        }
    }

    @Override
    @Transactional
    public void decrementCounter(String userEmail, String counterType) {
        if ("favorites".equals(counterType)) {
            // Obtener el user_id desde el email
            UserEntity user = userJpaRepository.findByEmail(userEmail);
            if (user == null) {
                throw new RuntimeException("Usuario no encontrado: " + userEmail);
            }
            jpaRepository.decrementFavoritesByUserId(user.getId());
        }
    }
}
