package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.FashnApiException;
import com.outfitlab.project.domain.exceptions.PredictionFailedException;
import com.outfitlab.project.domain.exceptions.PredictionTimeoutException;
import com.outfitlab.project.domain.useCases.fashn.CombinePrendas;
import com.outfitlab.project.domain.useCases.subscription.CheckUserPlanLimit;
import com.outfitlab.project.domain.useCases.subscription.IncrementUsageCounter;
import com.outfitlab.project.presentation.dto.CombineRequest;
import com.outfitlab.project.presentation.dto.GeneratedResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FashnControllerTest {

    private CombinePrendas combinePrendas;
    private CheckUserPlanLimit checkUserPlanLimit;
    private IncrementUsageCounter incrementUsageCounter;
    private RestTemplate restTemplate;
    private FashnController fashnController;
    private SecurityContext securityContext;
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        combinePrendas = mock(CombinePrendas.class);
        checkUserPlanLimit = mock(CheckUserPlanLimit.class);
        incrementUsageCounter = mock(IncrementUsageCounter.class);
        restTemplate = mock(RestTemplate.class);

        securityContext = mock(SecurityContext.class);
        authentication = mock(Authentication.class);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");

        fashnController = new FashnController(combinePrendas, checkUserPlanLimit, incrementUsageCounter, restTemplate);
    }

    @Test
    public void givenValidRequestWhenCombineThenReturnsOk() throws Exception {
        CombineRequest request = new CombineRequest();
        request.setTop("top_url");
        request.setBottom("bottom_url");
        request.setAvatarType("fullbody");

        when(combinePrendas.execute(any(), anyString())).thenReturn("result_url");

        ResponseEntity<GeneratedResponse> response = fashnController.combine(request);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("OK", response.getBody().getStatus());
        assertEquals("result_url", response.getBody().getImageUrl());

        verify(combinePrendas, times(1)).execute(any(), anyString());
    }

    @Test
    public void givenPredictionFailedExceptionWhenCombineThenReturnsFailed() throws Exception {
        CombineRequest request = new CombineRequest();
        request.setTop("top_url");
        request.setAvatarType("fullbody");

        when(combinePrendas.execute(any(), anyString())).thenThrow(new PredictionFailedException("Prediction failed"));

        ResponseEntity<GeneratedResponse> response = fashnController.combine(request);

        assertEquals(502, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("FAILED", response.getBody().getStatus());
        assertEquals("Prediction failed", response.getBody().getErrorMessage());
    }

    @Test
    public void givenPredictionTimeoutExceptionWhenCombineThenReturnsTimeout() throws Exception {
        CombineRequest request = new CombineRequest();
        request.setBottom("bottom_url");
        request.setAvatarType("fullbody");

        when(combinePrendas.execute(any(), anyString()))
                .thenThrow(new PredictionTimeoutException("Prediction timeout"));

        ResponseEntity<GeneratedResponse> response = fashnController.combine(request);

        assertEquals(504, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("TIMEOUT", response.getBody().getStatus());
        assertEquals("Prediction timeout", response.getBody().getErrorMessage());
    }

    @Test
    public void givenFashnApiExceptionWhenCombineThenReturnsError() throws Exception {
        CombineRequest request = new CombineRequest();
        request.setTop("top_url");
        request.setAvatarType("fullbody");

        when(combinePrendas.execute(any(), anyString())).thenThrow(new FashnApiException("Fashn API error"));

        ResponseEntity<GeneratedResponse> response = fashnController.combine(request);

        assertEquals(502, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("ERROR", response.getBody().getStatus());
        assertEquals("Fashn API error", response.getBody().getErrorMessage());
    }
}
