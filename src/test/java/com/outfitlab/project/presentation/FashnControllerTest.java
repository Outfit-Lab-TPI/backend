package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.useCases.fashn.CombinePrendas;
import com.outfitlab.project.domain.useCases.subscription.CheckUserPlanLimit;
import com.outfitlab.project.domain.useCases.subscription.IncrementUsageCounter;
import com.outfitlab.project.presentation.dto.CombineRequest;
import com.outfitlab.project.presentation.dto.GeneratedResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FashnControllerTest {

    private CombinePrendas combinePrendas;
    private CheckUserPlanLimit checkUserPlanLimit;
    private IncrementUsageCounter incrementUsageCounter;
    private RestTemplate restTemplate;
    private FashnController fashnController;
    private UserDetails mockUser;

    @BeforeEach
    void setUp() {
        combinePrendas = mock(CombinePrendas.class);
        checkUserPlanLimit = mock(CheckUserPlanLimit.class);
        incrementUsageCounter = mock(IncrementUsageCounter.class);
        restTemplate = mock(RestTemplate.class);
        fashnController = new FashnController(combinePrendas, checkUserPlanLimit, incrementUsageCounter, restTemplate);
        mockUser = mock(UserDetails.class);
    }

    @Test
    void givenValidRequestWhenCombineThenReturnsOk() throws Exception {
        CombineRequest request = givenValidCombineRequest();
        givenCombineReturns("result_url");

        ResponseEntity<GeneratedResponse> response = whenExecuteCombine(request);

        thenShouldReturnOk(response, "result_url");
        thenVerifyCombineCalledOnce();
    }

    @Test
    void givenPredictionFailedWhenCombineThenReturnsFailed() throws Exception {
        CombineRequest request = givenRequestWithTop();
        givenCombineThrowsPredictionFailed("Prediction failed");

        ResponseEntity<GeneratedResponse> response = whenExecuteCombine(request);

        thenShouldReturnFailed(response, "Prediction failed");
    }

    @Test
    void givenPredictionTimeoutWhenCombineThenReturnsTimeout() throws Exception {
        CombineRequest request = givenRequestWithBottom();
        givenCombineThrowsPredictionTimeout("Prediction timeout");

        ResponseEntity<GeneratedResponse> response = whenExecuteCombine(request);

        thenShouldReturnTimeout(response, "Prediction timeout");
    }

    @Test
    void givenFashnApiExceptionWhenCombineThenReturnsApiError() throws Exception {
        CombineRequest request = givenRequestWithTop();
        givenCombineThrowsApiError("Fashn API error");

        ResponseEntity<GeneratedResponse> response = whenExecuteCombine(request);

        thenShouldReturnApiError(response, "Fashn API error");
    }

    private CombineRequest givenValidCombineRequest() {
        CombineRequest request = new CombineRequest();
        request.setTop("top_url");
        request.setBottom("bottom_url");
        request.setAvatarType("fullbody");
        return request;
    }

    private CombineRequest givenRequestWithTop() {
        CombineRequest request = new CombineRequest();
        request.setTop("top_url");
        request.setAvatarType("fullbody");
        return request;
    }

    private CombineRequest givenRequestWithBottom() {
        CombineRequest request = new CombineRequest();
        request.setBottom("bottom_url");
        request.setAvatarType("fullbody");
        return request;
    }

    private void givenCombineReturns(String url) throws Exception {
        when(combinePrendas.execute(any(), any(UserDetails.class)))
                .thenReturn(url);
    }

    private void givenCombineThrowsPredictionFailed(String message) throws Exception {
        when(combinePrendas.execute(any(), any(UserDetails.class)))
                .thenThrow(new PredictionFailedException(message));
    }

    private void givenCombineThrowsPredictionTimeout(String message) throws Exception {
        when(combinePrendas.execute(any(), any(UserDetails.class)))
                .thenThrow(new PredictionTimeoutException(message));
    }

    private void givenCombineThrowsApiError(String message) throws Exception {
        when(combinePrendas.execute(any(), any(UserDetails.class)))
                .thenThrow(new FashnApiException(message));
    }

    private ResponseEntity<GeneratedResponse> whenExecuteCombine(CombineRequest request) {
        return fashnController.combine(request, mockUser);
    }

    private void thenShouldReturnOk(ResponseEntity<GeneratedResponse> response, String expectedUrl) {
        assertEquals(200, response.getStatusCode().value());
        assertEquals("OK", response.getBody().getStatus());
        assertEquals(expectedUrl, response.getBody().getImageUrl());
    }

    private void thenVerifyCombineCalledOnce() throws PlanLimitExceededException, SubscriptionNotFoundException {
        verify(combinePrendas, times(1)).execute(any(), any(UserDetails.class));
    }

    private void thenShouldReturnFailed(ResponseEntity<GeneratedResponse> response, String message) {
        assertEquals(502, response.getStatusCode().value());
        assertEquals("FAILED", response.getBody().getStatus());
        assertEquals(message, response.getBody().getErrorMessage());
    }

    private void thenShouldReturnTimeout(ResponseEntity<GeneratedResponse> response, String message) {
        assertEquals(504, response.getStatusCode().value());
        assertEquals("TIMEOUT", response.getBody().getStatus());
        assertEquals(message, response.getBody().getErrorMessage());
    }

    private void thenShouldReturnApiError(ResponseEntity<GeneratedResponse> response, String message) {
        assertEquals(502, response.getStatusCode().value());
        assertEquals("ERROR", response.getBody().getStatus());
        assertEquals(message, response.getBody().getErrorMessage());
    }
}
