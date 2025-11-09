package com.outfitlab.project.infrastructure.config;

import com.mercadopago.MercadoPagoConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

@Configuration
public class MercadoPagoSdkConfig {

    private final String accessToken;

    public MercadoPagoSdkConfig(@Value("${mercadopago.access.token}") String accessToken) {
        this.accessToken = accessToken;
    }

    @PostConstruct
    public void initializeMercadoPagoSdk() {
        if (accessToken == null || accessToken.trim().isEmpty() || accessToken.equals("${mercadopago.access.token}")) {
            throw new IllegalArgumentException("ERROR: El Access Token de Mercado Pago no está configurado. Revisa la variable de entorno.");
        }

        MercadoPagoConfig.setAccessToken(accessToken);

        System.out.println("✅ Mercado Pago SDK de Java inicializado globalmente.");
    }
}