package com.outfitlab.project.domain.useCases.subscription;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class CreateMercadoPagoPreferenceTest {

    private CreateMercadoPagoPreference createPreferenceUseCase;
    private final String PLAN_ID = "premium-demo-1";
    private final String USER_EMAIL = "test@example.com";
    private final BigDecimal PRICE = new BigDecimal("100.00");
    private final String CURRENCY = "ARS";

    @BeforeEach
    void setUp() {
        createPreferenceUseCase = new CreateMercadoPagoPreference();
    }

    @Test
    public void givenNullPriceWhenExecuteThenThrowMPApiException() {

        assertThrows(MPApiException.class, () ->
                createPreferenceUseCase.execute(PLAN_ID, USER_EMAIL, null, CURRENCY)
        );
    }
}