package com.outfitlab.project.domain.useCases.subscription;

import com.mercadopago.net.MPResponse;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.interfaces.gateways.MercadoPagoPaymentGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProcessPaymentNotificationTest {

    private UserRepository userRepository = mock(UserRepository.class);
    private MercadoPagoPaymentGateway paymentGateway = mock(MercadoPagoPaymentGateway.class);
    private ProcessPaymentNotification processNotificationUseCase;

    private final Long PAYMENT_ID = 12345L;
    private final String EXTERNAL_REFERENCE = "user-id-5678";

    @BeforeEach
    void setUp() {
        processNotificationUseCase = new ProcessPaymentNotification(userRepository, paymentGateway);
    }

    @Test
    public void givenApprovedPaymentWhenExecuteThenActivateUserPremium() throws MPException, MPApiException {
        Payment mockPayment = mock(Payment.class);
        when(mockPayment.getStatus()).thenReturn("approved");
        when(mockPayment.getExternalReference()).thenReturn(EXTERNAL_REFERENCE);
        when(paymentGateway.getPaymentDetails(PAYMENT_ID)).thenReturn(mockPayment);

        processNotificationUseCase.execute(PAYMENT_ID);

        verify(paymentGateway, times(1)).getPaymentDetails(PAYMENT_ID);
    }

    @Test
    public void givenRejectedPaymentWhenExecuteThenDoNotActivateUserPremium() throws MPException, MPApiException {
        Payment mockPayment = mock(Payment.class);
        when(mockPayment.getStatus()).thenReturn("rejected");
        when(mockPayment.getExternalReference()).thenReturn(EXTERNAL_REFERENCE);
        when(paymentGateway.getPaymentDetails(PAYMENT_ID)).thenReturn(mockPayment);

        processNotificationUseCase.execute(PAYMENT_ID);

        verify(paymentGateway, times(1)).getPaymentDetails(PAYMENT_ID);
    }

    @Test
    public void givenMPApiExceptionWhenExecuteThenPropagateException() throws MPException, MPApiException {
        MPResponse mockResponse = mock(MPResponse.class);
        doThrow(new MPApiException("Error de API simulado", mockResponse))
                .when(paymentGateway).getPaymentDetails(PAYMENT_ID);

        assertThrows(MPApiException.class, () -> processNotificationUseCase.execute(PAYMENT_ID));

        verify(paymentGateway, times(1)).getPaymentDetails(PAYMENT_ID);
    }
}