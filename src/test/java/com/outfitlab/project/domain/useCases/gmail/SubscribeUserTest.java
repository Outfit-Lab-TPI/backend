package com.outfitlab.project.domain.useCases.gmail;

import com.outfitlab.project.domain.interfaces.gateways.GmailGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SubscribeUserTest {

    private GmailGateway gmailGateway = mock(GmailGateway.class);
    private SubscribeUser subscribeUser;

    private final String VALID_EMAIL = "nuevo.usuario@test.com";
    private final String EXPECTED_SUBJECT = "¡Bienvenido a OutfitLab! 🥳";

    @BeforeEach
    void setUp() {
        subscribeUser = new SubscribeUser(gmailGateway);
    }


    @Test
    public void shouldSendWelcomeEmailToValidUser() {
        whenExecuteSubscribeUser(VALID_EMAIL);

        thenWelcomeEmailWasSent(VALID_EMAIL);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenEmailIsNull() {
        String nullEmail = null;

        assertThrows(IllegalArgumentException.class,
                () -> whenExecuteSubscribeUser(nullEmail),
                "Debe fallar si el email es nulo.");

        thenEmailWasNeverSent();
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenEmailIsEmpty() {
        String emptyEmail = "";

        assertThrows(IllegalArgumentException.class,
                () -> whenExecuteSubscribeUser(emptyEmail),
                "Debe fallar si el email está vacío.");

        thenEmailWasNeverSent();
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenEmailIsBlank() {
        String blankEmail = "   ";

        assertThrows(IllegalArgumentException.class,
                () -> whenExecuteSubscribeUser(blankEmail),
                "Debe fallar si el email contiene solo espacios.");

        thenEmailWasNeverSent();
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenEmailDoesNotContainAtSymbol() {
        String invalidEmail = "usuario.test.com";

        assertThrows(IllegalArgumentException.class,
                () -> whenExecuteSubscribeUser(invalidEmail),
                "Debe fallar si el email no contiene '@'.");

        thenEmailWasNeverSent();
    }

    @Test
    public void shouldSendEmailWhenEmailIsOnlyAtSymbolAsValidationAllowsIt() {
        String email = "@";

        whenExecuteSubscribeUser(email);

        thenWelcomeEmailWasSent(email);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenGmailGatewayFails() {
        givenGmailGatewayThrowsRuntimeException();

        assertThrows(RuntimeException.class,
                () -> whenExecuteSubscribeUser(VALID_EMAIL),
                "Debe propagar la excepción de Runtime si el gateway falla.");

        thenEmailWasSent(VALID_EMAIL, 1);
    }


    //privaadoss
    private void givenGmailGatewayThrowsRuntimeException() {
        doThrow(new RuntimeException("Simulated network failure"))
                .when(gmailGateway).sendEmail(anyString(), anyString(), anyString());
    }

    private void whenExecuteSubscribeUser(String email) {
        subscribeUser.execute(email);
    }

    private void thenWelcomeEmailWasSent(String expectedEmail) {
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);

        verify(gmailGateway, times(1)).sendEmail(emailCaptor.capture(), subjectCaptor.capture(), bodyCaptor.capture());

        assertEquals(expectedEmail, emailCaptor.getValue(), "El email del destinatario es incorrecto.");
        assertEquals(EXPECTED_SUBJECT, subjectCaptor.getValue(), "El asunto del correo es incorrecto.");

        String actualBody = bodyCaptor.getValue();
        assertNotNull(actualBody, "El cuerpo del correo no debe ser nulo.");
        assertTrue(actualBody.contains("¡Gracias por suscribirte a OutfitLab!"), "El cuerpo debe contener el mensaje de bienvenida.");
        assertTrue(actualBody.contains("<html>"), "El cuerpo debe contener formato HTML.");
    }

    private void thenEmailWasSent(String expectedEmail, int times) {
        verify(gmailGateway, times(times)).sendEmail(eq(expectedEmail), anyString(), anyString());
    }

    private void thenEmailWasNeverSent() {
        verify(gmailGateway, never()).sendEmail(anyString(), anyString(), anyString());
    }
}