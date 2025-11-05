package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.repositories.*;
import com.outfitlab.project.domain.useCases.subscription.*;
import com.outfitlab.project.infrastructure.adapters.MercadoPagoGatewayImpl;
import com.outfitlab.project.infrastructure.repositories.*;
import com.outfitlab.project.infrastructure.repositories.interfaces.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubscriptionConfig {

    @Bean
    public PaymentGatewayRepository paymentGatewayRepository(
            @Value("${mercadopago.access.token}") String accessToken,
            @Value("${mercadopago.urls.back}") String backUrl
    ) {
        return new MercadoPagoGatewayImpl(accessToken, backUrl);
    }

    @Bean
    public SubscriptionPlanRepository subscriptionPlanRepository(
            SubscriptionPlanJpaRepository jpaRepository
    ) {
        return new SubscriptionPlanRepositoryImpl(jpaRepository);
    }

    @Bean
    public UserSubscriptionRepository userSubscriptionRepository(
            UserSubscriptionJpaRepository jpaRepository,
            UserJpaRepository userJpaRepository,
            SubscriptionPlanJpaRepository planJpaRepository
    ) {
        return new UserSubscriptionRepositoryImpl(jpaRepository, userJpaRepository, planJpaRepository);
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

    @Bean
    public GetSubscriptionStatus getSubscriptionStatus(
            UserRepository userRepository,
            UserFavoriteCombinationRepository favoriteCombinationRepository
    ) {
        return new GetSubscriptionStatus(
                userRepository,
                favoriteCombinationRepository
        );
    }
}
