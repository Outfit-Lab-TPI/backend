package com.outfitlab.project.infrastructure.adapters;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.*;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import com.outfitlab.project.domain.exceptions.PaymentFailedException;
import com.outfitlab.project.domain.interfaces.repositories.PaymentGatewayRepository;
import com.outfitlab.project.domain.model.PaymentPreferenceModel;
import com.outfitlab.project.domain.model.PaymentStatusModel;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.Collections;

public class MercadoPagoGatewayImpl implements PaymentGatewayRepository {

    private final String backUrl;

    public MercadoPagoGatewayImpl(@Value("${mercadopago.access.token}") String accessToken,
                                   @Value("${mercadopago.urls.back}") String backUrl) {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalStateException("MP_ACCESS_TOKEN no está configurado");
        }
        MercadoPagoConfig.setAccessToken(accessToken);
        this.backUrl = backUrl;
    }

    @Override
    public PaymentPreferenceModel createPaymentPreference(String planCode, String planName, 
                                                          BigDecimal price, String userEmail) {
        try {
            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .id(planCode)
                    .title(planName)
                    .quantity(1)
                    .currencyId("ARS")
                    .unitPrice(price)
                    .build();

            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success(backUrl)
                    .failure(backUrl)
                    .pending(backUrl)
                    .build();

            PreferenceRequest request = PreferenceRequest.builder()
                    .items(Collections.singletonList(item))
                    .backUrls(backUrls)
                    // .autoReturn("approved")  // Comentado: requiere URLs públicas, no localhost
                    .externalReference(planCode + "_" + userEmail)
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(request);

            return new PaymentPreferenceModel(preference.getId(), preference.getInitPoint());

        } catch (MPApiException e) {
            System.err.println("MPApiException - Status: " + e.getStatusCode());
            System.err.println("MPApiException - Message: " + e.getMessage());
            if (e.getApiResponse() != null) {
                System.err.println("MPApiException - Response Content: " + e.getApiResponse().getContent());
            }
            throw new PaymentFailedException("MP API Error [" + e.getStatusCode() + "]: " + e.getMessage(), e);
        } catch (MPException e) {
            System.err.println("MPException - Message: " + e.getMessage());
            throw new PaymentFailedException("Error al crear preferencia de pago: " + e.getMessage(), e);
        }
    }

    @Override
    public PaymentStatusModel getPaymentStatus(String paymentId) {
        try {
            PaymentClient client = new PaymentClient();
            Payment payment = client.get(Long.parseLong(paymentId));

            return new PaymentStatusModel(
                    String.valueOf(payment.getId()),
                    payment.getStatus(),
                    payment.getStatusDetail(),
                    payment.getExternalReference(),
                    payment.getPayer().getEmail()
            );

        } catch (MPException | MPApiException e) {
            throw new PaymentFailedException("Error al obtener estado del pago: " + e.getMessage(), e);
        } catch (NumberFormatException e) {
            throw new PaymentFailedException("ID de pago inválido: " + paymentId, e);
        }
    }
}
