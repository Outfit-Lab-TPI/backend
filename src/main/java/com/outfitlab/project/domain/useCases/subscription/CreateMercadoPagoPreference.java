package com.outfitlab.project.domain.useCases.subscription;

import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CreateMercadoPagoPreference {

        private final String webhookBaseUrl;
        private final String frontendBaseUrl;

        public CreateMercadoPagoPreference(String webhookBaseUrl, String frontendBaseUrl) {
                this.webhookBaseUrl = webhookBaseUrl;
                this.frontendBaseUrl = frontendBaseUrl;
        }

        public String execute(String planId, String userEmail, BigDecimal price, String currency)
                        throws MPException, MPApiException {

                try {
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

                        PreferencePayerRequest payer = PreferencePayerRequest.builder()
                                        .email(userEmail) // dejo user email para poder pagar con cualquier cuenta
                                        .build();

                        // URLs de redirect configurables (localhost para testing, producción para
                        // deploy)
                        String redirectUrl = frontendBaseUrl + "/suscripcion";
                        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                                        .success(redirectUrl)
                                        .failure(redirectUrl)
                                        .pending(redirectUrl)
                                        .build();

                        PreferenceRequest request = PreferenceRequest.builder()
                                        .items(items)
                                        .payer(payer)
                                        .externalReference(userEmail) // CAMBIO: Almacenar userEmail en vez de planId
                                        .backUrls(backUrls)
                                        .notificationUrl(webhookBaseUrl + "/api/mp/webhooks") // ← NUEVO: Webhook URL
                                        .autoReturn("approved")
                                        .build();

                        PreferenceClient client = new PreferenceClient();
                        Preference preference = client.create(request);

                        System.out.println(
                                        "✅ Preferencia creada con webhook URL: " + webhookBaseUrl + "/api/mp/webhooks");
                        System.out.println("✅ Redirect URL: " + redirectUrl);

                        return preference.getInitPoint();
                } catch (MPApiException e) {
                        System.err.println("❌ Error de API de Mercado Pago:");
                        System.err.println("Status Code: " + e.getStatusCode());
                        System.err.println("Response Body: " + e.getApiResponse().getContent());
                        e.printStackTrace();
                        throw e;
                } catch (MPException e) {
                        System.err.println("❌ Error general de Mercado Pago:");
                        e.printStackTrace();
                        throw e;
                } catch (Exception e) {
                        System.err.println("❌ Error inesperado al crear preferencia:");
                        e.printStackTrace();
                        throw new MPException("Error inesperado: " + e.getMessage());
                }
        }
}