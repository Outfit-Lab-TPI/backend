package com.outfitlab.project.domain.useCases.subscription;

import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CreateMercadoPagoPreference {

    public CreateMercadoPagoPreference() {}

    public String execute(String planId, String userEmail, BigDecimal price, String currency) throws MPException, MPApiException {

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
                .email(userEmail)
                .build();

        PreferenceRequest request = PreferenceRequest.builder()
                .items(items)
                .payer(payer)
                .externalReference(planId)
                .build();

        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(request);

        return preference.getInitPoint();
    }
}