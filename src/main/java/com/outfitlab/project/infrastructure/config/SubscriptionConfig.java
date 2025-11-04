package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.repositories.*;
import com.outfitlab.project.domain.useCases.favorites.AddFavoriteCombination;
import com.outfitlab.project.domain.useCases.favorites.GetUserFavorites;
import com.outfitlab.project.domain.useCases.favorites.RemoveFavoriteCombination;
import com.outfitlab.project.domain.useCases.subscription.CreateSubscriptionPaymentPreference;
import com.outfitlab.project.domain.useCases.subscription.GetAvailableSubscriptionPlans;
import com.outfitlab.project.domain.useCases.subscription.ProcessSubscriptionPayment;
import com.outfitlab.project.infrastructure.repositories.impl.SubscriptionPlanRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.impl.FavoriteCombinationRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.impl.UserRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.impl.UserSubscriptionRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.interfaces.SubscriptionPlanJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserFavoriteCombinationJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserSubscriptionJpaRepository;
import com.outfitlab.project.infrastructure.repositories.GarmentRecomendationMapper;
import com.outfitlab.project.infrastructure.repositories.GarmentRecomendationRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.interfaces.GarmentRecomendationJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Spring para la inyección manual de dependencias.
 * Define todos los beans de repositorios y casos de uso relacionados con suscripciones y favoritos.
 */
@Configuration
public class SubscriptionConfig {

    // ========== REPOSITORY IMPLEMENTATIONS ==========

    @Bean
    public SubscriptionPlanRepository subscriptionPlanRepository(SubscriptionPlanJpaRepository jpaRepository) {
        return new SubscriptionPlanRepositoryImpl(jpaRepository);
    }

    @Bean
    public UserFavoriteCombinationRepository userFavoriteCombinationRepository(
            UserFavoriteCombinationJpaRepository jpaRepository
    ) {
        return new FavoriteCombinationRepositoryImpl(jpaRepository);
    }

    @Bean
    public GarmentRecomendationRepository garmentRecomendationRepository(
            GarmentRecomendationJpaRepository jpaRepository,
            GarmentRecomendationMapper mapper
    ) {
        return new GarmentRecomendationRepositoryImpl(jpaRepository, mapper);
    }

    // ========== SUBSCRIPTION USE CASES ==========

    @Bean
    public GetAvailableSubscriptionPlans getAvailableSubscriptionPlans(
            SubscriptionPlanRepository subscriptionPlanRepository
    ) {
        return new GetAvailableSubscriptionPlans(subscriptionPlanRepository);
    }

    @Bean
    public CreateSubscriptionPaymentPreference createSubscriptionPaymentPreference(
            SubscriptionPlanRepository subscriptionPlanRepository,
            UserRepository userRepository,
            PaymentGatewayRepository paymentGatewayRepository
    ) {
        return new CreateSubscriptionPaymentPreference(
                subscriptionPlanRepository,
                userRepository,
                paymentGatewayRepository
        );
    }

    @Bean
    public ProcessSubscriptionPayment processSubscriptionPayment(
            UserRepository userRepository,
            UserSubscriptionRepository userSubscriptionRepository,
            SubscriptionPlanRepository subscriptionPlanRepository,
            PaymentGatewayRepository paymentGatewayRepository
    ) {
        return new ProcessSubscriptionPayment(
                userRepository,
                userSubscriptionRepository,
                subscriptionPlanRepository,
                paymentGatewayRepository
        );
    }

    // ========== FAVORITES USE CASES ==========

    @Bean
    public AddFavoriteCombination addFavoriteCombination(
            UserFavoriteCombinationRepository favoriteCombinationRepository,
            UserRepository userRepository,
            GarmentRecomendationRepository garmentRecomendationRepository
    ) {
        return new AddFavoriteCombination(
                favoriteCombinationRepository,
                userRepository,
                garmentRecomendationRepository
        );
    }

    @Bean
    public GetUserFavorites getUserFavorites(UserFavoriteCombinationRepository favoriteCombinationRepository) {
        return new GetUserFavorites(favoriteCombinationRepository);
    }

    @Bean
    public RemoveFavoriteCombination removeFavoriteCombination(
            UserFavoriteCombinationRepository favoriteCombinationRepository
    ) {
        return new RemoveFavoriteCombination(favoriteCombinationRepository);
    }
}