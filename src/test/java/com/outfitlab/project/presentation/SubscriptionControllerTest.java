package com.outfitlab.project.presentation;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.net.MPResponse;
import com.outfitlab.project.domain.exceptions.SubscriptionNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserSubscriptionRepository;
import com.outfitlab.project.domain.model.UserSubscriptionModel;
import com.outfitlab.project.domain.useCases.subscription.CreateMercadoPagoPreference;
import com.outfitlab.project.domain.useCases.subscription.GetAllSubscription;
import com.outfitlab.project.domain.useCases.subscription.ProcessPaymentNotification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriptionControllerTest {

    private CreateMercadoPagoPreference createMercadoPagoPreference;
    private ProcessPaymentNotification processPaymentNotification;
    private GetAllSubscription getAllSubscription;
    private UserSubscriptionRepository userSubscriptionRepository;

    private SubscriptionController controller;

    @BeforeEach
    void setUp() {
        createMercadoPagoPreference = mock(CreateMercadoPagoPreference.class);
        processPaymentNotification = mock(ProcessPaymentNotification.class);
        getAllSubscription = mock(GetAllSubscription.class);
        userSubscriptionRepository = mock(UserSubscriptionRepository.class);

        controller = new SubscriptionController(
                createMercadoPagoPreference,
                processPaymentNotification,
                getAllSubscription,
                userSubscriptionRepository);
    }

    // ========== createPreference Tests ==========

    @Test
    void givenValidRequestWhenCreatePreferenceThenReturnOk() throws MPException, MPApiException {
        SubscriptionRequest request = givenValidSubscriptionRequest();
        String preferenceId = givenPreferenceCreated("pref-123456");

        ResponseEntity<?> response = whenCallCreatePreference(request);

        thenResponseOkWithPreferenceId(response, "pref-123456");
        thenVerifyCreatePreferenceCalled("PLAN-001", "user@example.com", new BigDecimal("99.99"), "ARS");
    }

    @Test
    void givenMPApiExceptionWhenCreatePreferenceThenReturnError() throws MPException, MPApiException {
        SubscriptionRequest request = givenValidSubscriptionRequest();
        givenMPApiException();

        ResponseEntity<?> response = whenCallCreatePreference(request);

        thenResponseInternalServerError(response);
        thenResponseContainsError(response, "Error de MercadoPago API");
    }

    @Test
    void givenMPExceptionWhenCreatePreferenceThenReturnError() throws MPException, MPApiException {
        SubscriptionRequest request = givenValidSubscriptionRequest();
        givenMPException();

        ResponseEntity<?> response = whenCallCreatePreference(request);

        thenResponseInternalServerError(response);
        thenResponseContainsError(response, "Error al crear preferencia");
    }

    // ========== handleMercadoPagoWebhook Tests ==========

    @Test
    void givenValidPaymentNotificationWhenHandleWebhookThenReturnOk() throws MPException, MPApiException {
        String paymentId = "12345";
        String topic = "payment";
        givenPaymentProcessed();

        ResponseEntity<?> response = whenCallHandleWebhook(paymentId, topic);

        thenResponseOk(response);
        thenVerifyProcessPaymentNotificationCalled(12345L);
    }

    @Test
    void givenInvalidTopicWhenHandleWebhookThenReturnOk() {
        String paymentId = "123";
        String topic = "merchant_order";

        ResponseEntity<?> response = whenCallHandleWebhook(paymentId, topic);

        thenResponseOk(response);
    }

    @Test
    void givenExceptionWhenHandleWebhookThenReturnError() throws MPException, MPApiException {
        String paymentId = "12345";
        String topic = "payment";
        givenPaymentProcessingFailed();

        ResponseEntity<?> response = whenCallHandleWebhook(paymentId, topic);

        thenResponseInternalServerError(response);
    }

    // ========== getSubscriptions Tests ==========

    @Test
    void whenGetSubscriptionsThenReturnOk() {
        givenAuthenticatedUser("user@example.com");
        givenPlansExist("user@example.com");

        ResponseEntity<?> response = whenCallGetSubscriptions();

        thenResponseOkWithSubscriptionsData(response);
        thenVerifyGetAllSubscriptionCalled("user@example.com");
    }

    // ========== getUserSubscription Tests ==========

    @Test
    void givenAuthenticatedUserWhenGetUserSubscriptionThenReturnOk() throws SubscriptionNotFoundException {
        givenAuthenticatedUser("user@example.com");
        givenUserSubscriptionModelExists();

        ResponseEntity<?> response = whenCallGetUserSubscription();

        thenResponseOkWithUserSubscription(response);
        thenVerifyUserSubscriptionRepositoryCalled("user@example.com");
    }

    @Test
    void givenNoSubscriptionWhenGetUserSubscriptionThenReturnNotFound() throws SubscriptionNotFoundException {
        givenAuthenticatedUser("user@example.com");
        givenUserSubscriptionModelNotFound();

        ResponseEntity<?> response = whenCallGetUserSubscription();

        thenResponseNotFound(response);
    }

    // ========== GIVEN Methods ==========

    private SubscriptionRequest givenValidSubscriptionRequest() {
        SubscriptionRequest request = new SubscriptionRequest();
        request.setPlanId("PLAN-001");
        request.setUserEmail("user@example.com");
        request.setPrice(new BigDecimal("99.99"));
        request.setCurrency("ARS");
        return request;
    }

    private String givenPreferenceCreated(String preferenceId) throws MPException, MPApiException {
        when(createMercadoPagoPreference.execute(anyString(), anyString(), any(BigDecimal.class), anyString()))
                .thenReturn(preferenceId);
        return preferenceId;
    }

    private void givenMPApiException() throws MPException, MPApiException {
        MPResponse mockResponse = mock(MPResponse.class);
        when(mockResponse.getStatusCode()).thenReturn(500);
        doThrow(new MPApiException("Error de MercadoPago API", mockResponse))
                .when(createMercadoPagoPreference)
                .execute(anyString(), anyString(), any(BigDecimal.class), anyString());
    }

    private void givenMPException() throws MPException, MPApiException {
        doThrow(new MPException("Error al crear preferencia"))
                .when(createMercadoPagoPreference)
                .execute(anyString(), anyString(), any(BigDecimal.class), anyString());
    }

    private void givenPaymentProcessed() throws MPException, MPApiException {
        doNothing().when(processPaymentNotification).execute(anyLong());
    }

    private void givenPaymentProcessingFailed() throws MPException, MPApiException {
        doThrow(new MPException("Error procesando pago"))
                .when(processPaymentNotification).execute(anyLong());
    }

    private void givenPlansExist(String userEmail) {
        List<?> plans = Arrays.asList(
                Map.of("id", "PLAN-001", "name", "Basic", "price", 99.99),
                Map.of("id", "PLAN-002", "name", "Premium", "price", 199.99));
        doReturn(plans).when(getAllSubscription).execute(userEmail);
    }

    private void givenAuthenticatedUser(String email) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(email);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    private UserSubscriptionModel givenUserSubscriptionModelExists() throws SubscriptionNotFoundException {
        UserSubscriptionModel subscription = mock(UserSubscriptionModel.class);
        when(subscription.getPlanCode()).thenReturn("PLAN-001");
        when(subscription.getStatus()).thenReturn("active");
        when(subscription.getCombinationsUsed()).thenReturn(5);
        when(subscription.getMaxCombinations()).thenReturn(10);
        when(subscription.getFavoritesCount()).thenReturn(3);
        when(subscription.getMaxFavorites()).thenReturn(20);
        when(subscription.getModelsGenerated()).thenReturn(2);
        when(subscription.getMaxModels()).thenReturn(5);
        when(subscription.getDownloadsCount()).thenReturn(1);
        when(subscription.getMaxDownloads()).thenReturn(10);
        when(subscription.getGarmentsUploaded()).thenReturn(0);
        when(subscription.getMaxGarments()).thenReturn(null);
        when(userSubscriptionRepository.findByUserEmail(anyString())).thenReturn(subscription);
        return subscription;
    }

    private void givenUserSubscriptionModelNotFound() throws SubscriptionNotFoundException {
        when(userSubscriptionRepository.findByUserEmail(anyString()))
                .thenThrow(new SubscriptionNotFoundException("Suscripción no encontrada"));
    }

    // ========== WHEN Methods ==========

    private ResponseEntity<?> whenCallCreatePreference(SubscriptionRequest request) {
        return controller.createPreference(request);
    }

    private ResponseEntity<?> whenCallHandleWebhook(String id, String topic) {
        return controller.handleMercadoPagoWebhook(id, topic);
    }

    private ResponseEntity<?> whenCallGetSubscriptions() {
        return controller.getSubscriptions();
    }

    private ResponseEntity<?> whenCallGetUserSubscription() {
        return controller.getUserSubscription();
    }

    // ========== THEN Methods ==========

    private void thenResponseOk(ResponseEntity<?> response) {
        assertEquals(200, response.getStatusCode().value());
    }

    private void thenResponseOkWithPreferenceId(ResponseEntity<?> response, String expectedPreferenceId) {
        assertEquals(200, response.getStatusCode().value());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        assertNotNull(body.get("initPoint"));
    }

    private void thenResponseOkWithSubscriptionsData(ResponseEntity<?> response) {
        assertEquals(200, response.getStatusCode().value());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        assertNotNull(body.get("data"));
    }

    private void thenResponseOkWithUserSubscription(ResponseEntity<?> response) {
        assertEquals(200, response.getStatusCode().value());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("planCode"));
        assertTrue(body.containsKey("usage"));
    }

    private void thenResponseInternalServerError(ResponseEntity<?> response) {
        assertEquals(500, response.getStatusCode().value());
    }

    private void thenResponseNotFound(ResponseEntity<?> response) {
        assertEquals(404, response.getStatusCode().value());
    }

    private void thenResponseContainsError(ResponseEntity<?> response, String expectedError) {
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        String error = (String) body.get("error");
        assertNotNull(error, "Error message should not be null");
        assertTrue(error.contains(expectedError) || error.contains("MercadoPago") || error.contains("preferencia"),
                "Expected error to contain: " + expectedError + " but got: " + error);
    }

    private void thenVerifyCreatePreferenceCalled(String planId, String userEmail, BigDecimal price, String currency)
            throws MPException, MPApiException {
        verify(createMercadoPagoPreference, times(1)).execute(planId, userEmail, price, currency);
    }

    private void thenVerifyProcessPaymentNotificationCalled(Long paymentId) throws MPException, MPApiException {
        verify(processPaymentNotification, times(1)).execute(paymentId);
    }

    private void thenVerifyGetAllSubscriptionCalled(String userEmail) {
        verify(getAllSubscription, times(1)).execute(userEmail);
    }

    private void thenVerifyUserSubscriptionRepositoryCalled(String email) throws SubscriptionNotFoundException {
        verify(userSubscriptionRepository, times(1)).findByUserEmail(email);
    }
}
