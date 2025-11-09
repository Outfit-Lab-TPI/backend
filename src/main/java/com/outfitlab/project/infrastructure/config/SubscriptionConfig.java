package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.useCases.mercadopago.CreateMercadoPagoPreference;
import com.outfitlab.project.domain.useCases.mercadopago.ProcessPaymentNotification;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubscriptionConfig {

    @Bean
    public ProcessPaymentNotification processPaymentNotification(UserRepository userRepository) {
        return new ProcessPaymentNotification(userRepository);
    }

    @Bean
    public CreateMercadoPagoPreference createMercadoPagoPreference() {
        return new CreateMercadoPagoPreference();
    }
}