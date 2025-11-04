package com.outfitlab.project.infrastructure.adapters;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import com.outfitlab.project.domain.exception.PaymentFailedException;
import com.outfitlab.project.domain.interfaces.repositories.PaymentGatewayRepository;
import com.outfitlab.project.domain.model.PaymentPreferenceModel;
import com.outfitlab.project.domain.model.PaymentStatusModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * Adaptador que implementa PaymentGatewayRepository usando MercadoPago SDK.
 * Esta clase pertenece a la capa de infraestructura y puede usar dependencias externas.
 */
@Component
public class MercadoPagoGatewayImpl implements PaymentGatewayRepository {

    private final String accessToken;
    private final String successUrl;
    private final String failureUrl;
    private final String pendingUrl;

    public MercadoPagoGatewayImpl(
            @Value("${mercadopago.access.token}") String accessToken,
            @Value("${mercadopago.notification.success-url:http://localhost:8080/success}") String successUrl,
            @Value("${mercadopago.notification.failure-url:http://localhost:8080/failure}") String failureUrl,
            @Value("${mercadopago.notification.pending-url:http://localhost:8080/pending}") String pendingUrl
    ) {
        this.accessToken = accessToken;
        this.successUrl = successUrl;
        this.failureUrl = failureUrl;
        this.pendingUrl = pendingUrl;
        
        // Configurar el token de acceso de MercadoPago
        MercadoPagoConfig.setAccessToken(this.accessToken);
    }

    @Override
    public PaymentPreferenceModel createPaymentPreference(
            String planCode,
            String planName,
            BigDecimal price,
            String userEmail
    ) {
        try {
            PreferenceClient client = new PreferenceClient();

            // Crear item de la preferencia
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id(planCode)
                    .title(planName)
                    .quantity(1)
                    .unitPrice(price)
                    .build();

            // Configurar URLs de retorno
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success(successUrl)
                    .failure(failureUrl)
                    .pending(pendingUrl)
                    .build();

            // Crear preferencia de pago
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(List.of(itemRequest))
                    .backUrls(backUrls)
                    .autoReturn("approved")
                    .externalReference(planCode + "_" + userEmail)
                    .build();

            Preference preference = client.create(preferenceRequest);

            // Mapear a modelo del dominio
            return new PaymentPreferenceModel(
                    preference.getInitPoint(),
                    preference.getId(),
                    preference.getSandboxInitPoint()
            );

        } catch (MPException | MPApiException e) {
            throw new PaymentFailedException("Error al crear preferencia de pago en MercadoPago: " + e.getMessage(), e);
        }
    }

    @Override
    public PaymentStatusModel getPaymentStatus(String paymentId) {
        try {
            PaymentClient client = new PaymentClient();
            Payment payment = client.get(Long.parseLong(paymentId));

            // Mapear a modelo del dominio
            PaymentStatusModel statusModel = new PaymentStatusModel();
            statusModel.setPaymentId(payment.getId().toString());
            statusModel.setStatus(payment.getStatus());
            statusModel.setStatusDetail(payment.getStatusDetail());
            statusModel.setTransactionAmount(payment.getTransactionAmount());
            statusModel.setPayerEmail(payment.getPayer() != null ? payment.getPayer().getEmail() : null);
            statusModel.setExternalReference(payment.getExternalReference());
            
            // Convertir OffsetDateTime a LocalDateTime
            if (payment.getDateApproved() != null) {
                statusModel.setDateApproved(convertToLocalDateTime(payment.getDateApproved()));
            }
            if (payment.getDateCreated() != null) {
                statusModel.setDateCreated(convertToLocalDateTime(payment.getDateCreated()));
            }

            return statusModel;

        } catch (MPException | MPApiException e) {
            throw new PaymentFailedException("Error al obtener estado del pago desde MercadoPago: " + e.getMessage(), e);
        } catch (NumberFormatException e) {
            throw new PaymentFailedException("ID de pago inválido: " + paymentId, e);
        }
    }

    private LocalDateTime convertToLocalDateTime(OffsetDateTime offsetDateTime) {
        return offsetDateTime.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
    }
}
