package com.outfitlab.project.domain.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import com.mercadopago.exceptions.MPException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SubscriptionService {

    /**
     * Constructor para inicializar el SDK con el Access Token.
     * Se ejecuta al iniciar Spring Boot.
     */
    public SubscriptionService(@Value("${mercadopago.access.token}") String accessToken) {
        if (accessToken == null || accessToken.trim().isEmpty() || accessToken.equals("${mercadopago.access.token}")) {
            throw new IllegalArgumentException("ERROR: El Access Token de Mercado Pago no está configurado. Revisa la variable de entorno MP_ACCESS_TOKEN.");
        }
        MercadoPagoConfig.setAccessToken(accessToken);
        System.out.println("Mercado Pago SDK de Java inicializado (desde SubscriptionService).");
    }

    /**
     * Crea una Preferencia de Pago Único (para la demo).
     */
    public String createMercadoPagoPreference(String planId, String userEmail, BigDecimal price, String currency) throws MPException, MPApiException {

        // 1. Definir el ítem
        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .id(planId)
                .title("Demo Premium Outfit Lab")
                .description("Acceso único a funciones premium")
                .quantity(1)
                .unitPrice(price)
                .currencyId(currency)
                .build();

        List<PreferenceItemRequest> items = new ArrayList<>();
        items.add(itemRequest);

        // 2. Definir el pagador (con el email de prueba)
        PreferencePayerRequest payer = PreferencePayerRequest.builder()
                .email(userEmail)
                .build();

        // 3. Crear la solicitud de Preferencia
        PreferenceRequest request = PreferenceRequest.builder()
                .items(items)
                .payer(payer)
                // --- SE ELIMINAN backUrls y autoReturn ---
                // Esto evita el error 500 (MPApiException) al validar localhost.
                // Mercado Pago usará las URLs configuradas en el dashboard
                // o mostrará un botón simple de "Volver al sitio".
                .externalReference(planId)
                .build();

        // 4. Ejecutar la API
        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(request);

        // 5. Devolver la URL de pago
        return preference.getInitPoint();
    }

    /**
     * Procesa el Webhook para un Pago Único.
     */
    public void processPaymentNotification(Long paymentId) throws MPException, MPApiException {
        PaymentClient client = new PaymentClient();
        Payment payment = client.get(paymentId);

        String status = payment.getStatus();
        String externalReference = payment.getExternalReference();

        System.out.printf("Procesando Webhook de PAGO. ID Pago MP: %s, ID Plan Interno: %s, Status: %s%n",
                paymentId, externalReference, status);

        if ("approved".equals(status)) {
            System.out.println("Pago AUTHORIZED. Activando Premium (Demo) para: " + externalReference);
            // TODO: Aquí iría tu lógica de base de datos para activar el servicio
        } else if ("rejected".equals(status) || "cancelled".equals(status)) {
            System.out.println("Pago REJECTED/CANCELLED para: " + externalReference);
        }
    }
}