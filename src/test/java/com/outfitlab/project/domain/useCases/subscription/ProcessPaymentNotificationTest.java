package com.outfitlab.project.domain.useCases.subscription;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.net.MPResponse;
import com.mercadopago.resources.payment.Payment;
import com.outfitlab.project.domain.exceptions.SubscriptionNotFoundException;
import com.outfitlab.project.domain.interfaces.gateways.MercadoPagoPaymentGateway;
import com.outfitlab.project.domain.interfaces.repositories.SubscriptionRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserSubscriptionRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.SubscriptionModel;
import com.outfitlab.project.domain.model.UserSubscriptionModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProcessPaymentNotificationTest {

    private UserRepository userRepository = mock(UserRepository.class);
    private MercadoPagoPaymentGateway paymentGateway = mock(MercadoPagoPaymentGateway.class);
    private UserSubscriptionRepository userSubscriptionRepository = mock(UserSubscriptionRepository.class);
    private SubscriptionRepository subscriptionRepository = mock(SubscriptionRepository.class);

    private ProcessPaymentNotification processNotificationUseCase;

    private final Long PAYMENT_ID = 12345L;
    private final String EXTERNAL_REFERENCE = "user-id-5678";
    private final String PLAN_CODE = "premium";
    private final String PAYMENT_STATUS_APPROVED = "approved";
    private final String PAYMENT_STATUS_REJECTED = "rejected";

    @BeforeEach
    void setUp() {
        processNotificationUseCase = new ProcessPaymentNotification(
                userRepository,
                paymentGateway,
                userSubscriptionRepository,
                subscriptionRepository
        );
    }


    @Test
    public void shouldActivateUserPremiumWhenPaymentIsApproved() throws MPException, MPApiException, SubscriptionNotFoundException {
        givenApprovedPaymentDetails(PAYMENT_ID, EXTERNAL_REFERENCE, "Plan Premium");
        givenSubscriptionModelsExist(PLAN_CODE);

        whenExecuteProcessNotification(PAYMENT_ID);

        thenPaymentDetailsAreFetched(PAYMENT_ID);
    }

    @Test
    public void shouldNotUpdateSubscriptionWhenPaymentIsRejected() throws MPException, MPApiException, SubscriptionNotFoundException {
        givenRejectedPaymentDetails(PAYMENT_ID, EXTERNAL_REFERENCE);

        whenExecuteProcessNotification(PAYMENT_ID);

        thenPaymentDetailsAreFetched(PAYMENT_ID);
        thenUpdateWasNeverCalled();
    }

    @Test
    public void shouldPropagateMPApiExceptionWhenFetchingPaymentDetailsFails() throws MPException, MPApiException {
        givenPaymentGatewayThrowsMPApiException(PAYMENT_ID);

        assertThrows(MPApiException.class, () -> whenExecuteProcessNotification(PAYMENT_ID));

        thenPaymentDetailsAreFetched(PAYMENT_ID);
        thenUpdateWasNeverCalled();
    }


    //privadoss
    private void givenApprovedPaymentDetails(Long paymentId, String externalReference, String planDescription) throws MPException, MPApiException {
        Payment mockPayment = mock(Payment.class);

        when(mockPayment.getStatus()).thenReturn(PAYMENT_STATUS_APPROVED);
        when(mockPayment.getExternalReference()).thenReturn(externalReference);
        when(mockPayment.getDescription()).thenReturn(planDescription);

        when(paymentGateway.getPaymentDetails(paymentId)).thenReturn(mockPayment);
    }

    private void givenRejectedPaymentDetails(Long paymentId, String externalReference) throws MPException, MPApiException {
        Payment mockPayment = mock(Payment.class);
        when(mockPayment.getStatus()).thenReturn(PAYMENT_STATUS_REJECTED);
        when(mockPayment.getExternalReference()).thenReturn(externalReference);

        when(paymentGateway.getPaymentDetails(paymentId)).thenReturn(mockPayment);
    }

    private void givenSubscriptionModelsExist(String planCode) throws SubscriptionNotFoundException {
        SubscriptionModel mockPlan = mock(SubscriptionModel.class);
        when(mockPlan.getPlanCode()).thenReturn(planCode);
        when(mockPlan.getFeature2()).thenReturn("Unlimited");

        when(subscriptionRepository.getByPlanCode(anyString())).thenReturn(mockPlan);

        UserSubscriptionModel mockUserSub = mock(UserSubscriptionModel.class);
        when(userSubscriptionRepository.findByUserEmail(anyString())).thenReturn(mockUserSub);
    }

    private void givenPaymentGatewayThrowsMPApiException(Long paymentId) throws MPException, MPApiException {
        MPResponse mockResponse = mock(MPResponse.class);
        doThrow(new MPApiException("Error de API simulado", mockResponse))
                .when(paymentGateway).getPaymentDetails(paymentId);
    }


    private void whenExecuteProcessNotification(Long paymentId) throws MPException, MPApiException, SubscriptionNotFoundException {
        processNotificationUseCase.execute(paymentId);
    }


    private void thenPaymentDetailsAreFetched(Long paymentId) throws MPException, MPApiException {
        verify(paymentGateway, times(1)).getPaymentDetails(paymentId);
    }

    private void thenUpdateWasNeverCalled() {
        verify(userSubscriptionRepository, never()).update(any());
    }
}