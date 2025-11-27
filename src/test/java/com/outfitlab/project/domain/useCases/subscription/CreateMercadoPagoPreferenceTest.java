package com.outfitlab.project.domain.useCases.subscription;

import com.mercadopago.exceptions.MPApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CreateMercadoPagoPreferenceTest {

    private CreateMercadoPagoPreference createPreferenceUseCase;

    private static final String PLAN_ID = "premium-demo-1";
    private static final String USER_EMAIL = "test@example.com";
    private static final BigDecimal VALID_PRICE = new BigDecimal("25000.00");
    private static final String CURRENCY = "ARS";
    private static final String WEBHOOK_URL = "http://localhost:8080";
    private static final String FRONTEND_URL = "http://localhost:5173";

    @BeforeEach
    void setUp() {
        createPreferenceUseCase = new CreateMercadoPagoPreference(WEBHOOK_URL, FRONTEND_URL);
    }


    @Test
    public void shouldThrowMPApiExceptionWhenPriceIsNull() {
        BigDecimal invalidPrice = null;

        //when y then
        thenExecutionThrowsMPApiException(PLAN_ID, USER_EMAIL, invalidPrice, CURRENCY);
    }

    @Test
    public void shouldThrowMPApiExceptionWhenPriceIsZeroOrNegative() {
        BigDecimal zeroPrice = BigDecimal.ZERO;
        BigDecimal negativePrice = new BigDecimal("-1.00");

        thenExecutionThrowsMPApiException(PLAN_ID, USER_EMAIL, zeroPrice, CURRENCY);
        thenExecutionThrowsMPApiException(PLAN_ID, USER_EMAIL, negativePrice, CURRENCY);
    }

    @Test
    public void shouldThrowMPExceptionOrMPApiExceptionWhenUserEmailIsNull() {
        String invalidEmail = null;

        assertThrows(MPApiException.class,
                () -> createPreferenceUseCase.execute(PLAN_ID, invalidEmail, VALID_PRICE, CURRENCY),
                "Se esperaba una MPApiException cuando el email del usuario es nulo.");
    }


    //privado
    private void thenExecutionThrowsMPApiException(String planId, String email, BigDecimal price, String currency) {
        assertThrows(MPApiException.class,
                () -> createPreferenceUseCase.execute(planId, email, price, currency),
                "Se esperaba una MPApiException.");
    }
}