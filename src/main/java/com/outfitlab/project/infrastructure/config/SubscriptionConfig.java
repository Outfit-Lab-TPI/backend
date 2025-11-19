package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.repositories.SubscriptionRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.useCases.subscription.CreateMercadoPagoPreference;
import com.outfitlab.project.domain.useCases.subscription.GetAllSubscription;
import com.outfitlab.project.domain.useCases.subscription.ProcessPaymentNotification;
import com.outfitlab.project.infrastructure.gateways.MercadoPagoPaymentGatewayImpl;
import com.outfitlab.project.infrastructure.repositories.SubscriptionRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.interfaces.SubscriptionJpaRepository;
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
    public GetAllSubscription getAllsubscription(SubscriptionRepository subscriptionRepository) {
        return new GetAllSubscription(subscriptionRepository);
    }

    @Bean
    public ProcessPaymentNotification processPaymentNotification(UserRepository userRepository, MercadoPagoPaymentGateway mercadoPagoPaymentGateway) {
        return new ProcessPaymentNotification(userRepository, mercadoPagoPaymentGateway);
    }

    @Bean
    public CreateMercadoPagoPreference createMercadoPagoPreference() {
        return new CreateMercadoPagoPreference();
    }
}