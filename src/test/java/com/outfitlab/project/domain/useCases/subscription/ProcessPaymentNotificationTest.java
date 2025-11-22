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
    private com.outfitlab.project.domain.interfaces.repositories.UserSubscriptionRepository userSubscriptionRepository = mock(com.outfitlab.project.domain.interfaces.repositories.UserSubscriptionRepository.class);
    private com.outfitlab.project.domain.interfaces.repositories.SubscriptionRepository subscriptionRepository = mock(com.outfitlab.project.domain.interfaces.repositories.SubscriptionRepository.class);
    private ProcessPaymentNotification processNotificationUseCase;

    private final Long PAYMENT_ID = 12345L;
    private final String EXTERNAL_REFERENCE = "user-id-5678";

    @BeforeEach
    void setUp() {
        processNotificationUseCase = new ProcessPaymentNotification(userRepository, paymentGateway, userSubscriptionRepository, subscriptionRepository);
    }

    @Test
    public void givenApprovedPaymentWhenExecuteThenActivateUserPremium() throws MPException, MPApiException, com.outfitlab.project.domain.exceptions.SubscriptionNotFoundException {
        Payment mockPayment = mock(Payment.class);
        when(mockPayment.getStatus()).thenReturn("approved");
        when(mockPayment.getExternalReference()).thenReturn(EXTERNAL_REFERENCE);
        // Mock description to contain plan info if needed, or just ensure it doesn't crash
        when(mockPayment.getDescription()).thenReturn("Plan Premium");
        
        when(paymentGateway.getPaymentDetails(PAYMENT_ID)).thenReturn(mockPayment);
        
        // Mock subscription behavior
        com.outfitlab.project.domain.model.SubscriptionModel mockPlan = new com.outfitlab.project.domain.model.SubscriptionModel();
        mockPlan.setPlanCode("premium");
        mockPlan.setFeature2("Unlimited"); // For limit parsing
        when(subscriptionRepository.getByPlanCode(anyString())).thenReturn(mockPlan);
        
        com.outfitlab.project.domain.model.UserSubscriptionModel mockUserSub = new com.outfitlab.project.domain.model.UserSubscriptionModel();
        when(userSubscriptionRepository.findByUserEmail(anyString())).thenReturn(mockUserSub);

        processNotificationUseCase.execute(PAYMENT_ID);

        verify(paymentGateway, times(1)).getPaymentDetails(PAYMENT_ID);
    }

    @Test
    public void givenRejectedPaymentWhenExecuteThenDoNotActivateUserPremium() throws MPException, MPApiException, com.outfitlab.project.domain.exceptions.SubscriptionNotFoundException {
        Payment mockPayment = mock(Payment.class);
        when(mockPayment.getStatus()).thenReturn("rejected");
        when(mockPayment.getExternalReference()).thenReturn(EXTERNAL_REFERENCE);
        when(paymentGateway.getPaymentDetails(PAYMENT_ID)).thenReturn(mockPayment);

        processNotificationUseCase.execute(PAYMENT_ID);

        verify(paymentGateway, times(1)).getPaymentDetails(PAYMENT_ID);
        verify(userSubscriptionRepository, never()).update(any());
    }

    @Test
    public void givenMPApiExceptionWhenExecuteThenPropagateException() throws MPException, MPApiException, com.outfitlab.project.domain.exceptions.SubscriptionNotFoundException {
        MPResponse mockResponse = mock(MPResponse.class);
        doThrow(new MPApiException("Error de API simulado", mockResponse))
                .when(paymentGateway).getPaymentDetails(PAYMENT_ID);

        assertThrows(MPApiException.class, () -> processNotificationUseCase.execute(PAYMENT_ID));

        verify(paymentGateway, times(1)).getPaymentDetails(PAYMENT_ID);
    }
}