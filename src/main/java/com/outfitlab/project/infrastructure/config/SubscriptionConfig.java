package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.repositories.SubscriptionRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserSubscriptionRepository;
import com.outfitlab.project.domain.useCases.subscription.*;
import com.outfitlab.project.infrastructure.gateways.MercadoPagoPaymentGatewayImpl;
import com.outfitlab.project.infrastructure.repositories.SubscriptionRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.UserSubscriptionRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.interfaces.SubscriptionJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserSubscriptionJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.outfitlab.project.domain.interfaces.gateways.MercadoPagoPaymentGateway;

@Configuration
public class SubscriptionConfig {

    @Bean
    public MercadoPagoPaymentGateway mercadoPagoPaymentGateway() {
        return new MercadoPagoPaymentGatewayImpl();
    }
    
    @Bean
    public SubscriptionRepository subscriptionRepository(SubscriptionJpaRepository subscriptionJpaRepository) {
        return new SubscriptionRepositoryImpl(subscriptionJpaRepository);
    }
    
    @Bean
    public UserSubscriptionRepository userSubscriptionRepository(UserSubscriptionJpaRepository jpaRepository,
                                                                 UserJpaRepository userJpaRepository,
                                                                 SubscriptionJpaRepository subscriptionJpaRepository) {
        return new UserSubscriptionRepositoryImpl(jpaRepository, userJpaRepository, subscriptionJpaRepository);
    }
    
    @Bean
    public GetAllSubscription getAllsubscription(SubscriptionRepository subscriptionRepository) {
        return new GetAllSubscription(subscriptionRepository);
    }

    @Bean
    public ProcessPaymentNotification processPaymentNotification(UserRepository userRepository, 
                                                                 MercadoPagoPaymentGateway mercadoPagoPaymentGateway,
                                                                 UserSubscriptionRepository userSubscriptionRepository,
                                                                 SubscriptionRepository subscriptionRepository) {
        return new ProcessPaymentNotification(userRepository, mercadoPagoPaymentGateway, 
                                             userSubscriptionRepository, subscriptionRepository);
    }

    @Bean
    public CreateMercadoPagoPreference createMercadoPagoPreference() {
        return new CreateMercadoPagoPreference();
    }
    
    @Bean
    public CheckUserPlanLimit checkUserPlanLimit(UserSubscriptionRepository userSubscriptionRepository) {
        return new CheckUserPlanLimit(userSubscriptionRepository);
    }
    
    @Bean
    public IncrementUsageCounter incrementUsageCounter(UserSubscriptionRepository userSubscriptionRepository) {
        return new IncrementUsageCounter(userSubscriptionRepository);
    }
    
    @Bean
    public AssignFreePlanToUser assignFreePlanToUser(UserSubscriptionRepository userSubscriptionRepository,
                                                     SubscriptionRepository subscriptionRepository) {
        return new AssignFreePlanToUser(userSubscriptionRepository, subscriptionRepository);
    }
}