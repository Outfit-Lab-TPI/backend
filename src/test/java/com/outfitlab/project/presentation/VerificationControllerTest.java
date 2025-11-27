package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.useCases.user.VerifyEmail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VerificationControllerTest {

    private VerifyEmail verifyEmailUseCase;
    private VerificationController controller;

    @BeforeEach
    void setUp() {
        verifyEmailUseCase = mock(VerifyEmail.class);
        controller = new VerificationController(verifyEmailUseCase);
    }

    @Test
    void givenValidTokenWhenVerifyUserThenRedirectsToSuccess() throws Exception {

        String token = givenValidToken();

        ResponseEntity<Void> response = whenVerifyUser(token);

        thenRedirectsToSuccess(response);
        verify(verifyEmailUseCase, times(1)).execute(token);
    }

    @Test
    void givenInvalidTokenWhenVerifyUserThenRedirectsToUserNotFoundError() throws Exception {

        String token = givenInvalidToken();
        mockUserNotFoundException(token);

        ResponseEntity<Void> response = whenVerifyUser(token);

        thenRedirectsToUserNotFoundError(response);
        verify(verifyEmailUseCase, times(1)).execute(token);
    }

    @Test
    void givenUnexpectedExceptionWhenVerifyUserThenRedirectsToInternalError() throws Exception {

        String token = givenAnyToken();
        mockUnexpectedException(token);

        ResponseEntity<Void> response = whenVerifyUser(token);

        thenRedirectsToInternalError(response);
        verify(verifyEmailUseCase, times(1)).execute(token);
    }

    private String givenValidToken() {
        return "valid-token";
    }

    private String givenInvalidToken() {
        return "invalid-token";
    }

    private String givenAnyToken() {
        return "any-token";
    }

    private void mockUserNotFoundException(String token) throws Exception {
        doThrow(new UserNotFoundException("No existe")).when(verifyEmailUseCase).execute(token);
    }

    private void mockUnexpectedException(String token) throws Exception {
        doThrow(new RuntimeException("boom")).when(verifyEmailUseCase).execute(token);
    }

    private ResponseEntity<Void> whenVerifyUser(String token) throws Exception {
        return controller.verifyUser(token);
    }

    private void thenRedirectsToSuccess(ResponseEntity<Void> response) {
        assertEquals(302, response.getStatusCode().value());
        assertTrue(response.getHeaders().getLocation().toString().contains("?verification=success"));
    }

    private void thenRedirectsToUserNotFoundError(ResponseEntity<Void> response) {
        assertEquals(302, response.getStatusCode().value());

        String location = response.getHeaders().getLocation().toString();
        assertTrue(location.contains("?verification=error&message="));
        assertTrue(location.contains("Token+inv%C3%A1lido+o+expirado."));
    }

    private void thenRedirectsToInternalError(ResponseEntity<Void> response) {
        assertEquals(302, response.getStatusCode().value());

        String location = response.getHeaders().getLocation().toString();
        assertTrue(location.contains("?verification=error&message="));
        assertTrue(location.contains("Error+interno+del+servidor"));
    }
}
