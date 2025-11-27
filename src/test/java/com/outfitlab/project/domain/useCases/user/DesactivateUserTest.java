package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DesactivateUserTest {

    private UserRepository userRepository = mock(UserRepository.class);
    private DesactivateUser desactivateUser;

    private final String VALID_EMAIL = "user.to.desactivate@test.com";
    private final String EMPTY_EMAIL = "";
    private final String NULL_EMAIL = null;
    private final String SUCCESS_MESSAGE = "Usuario desactivado con éxito.";

    @BeforeEach
    void setUp() {
        desactivateUser = new DesactivateUser(userRepository);
    }


    @Test
    public void shouldCallRepositoryToDesactivateUserAndReturnSuccessMessage() {
        String result = whenExecuteDesactivateUser(VALID_EMAIL);

        assertEquals(SUCCESS_MESSAGE, result, "Debe retornar el mensaje de éxito.");
        thenRepositoryDesactivateWasCalled(VALID_EMAIL, 1);
    }

    @Test
    public void shouldCallRepositoryWithEmptyStringWhenEmailIsEmpty() {
        String result = whenExecuteDesactivateUser(EMPTY_EMAIL);

        assertEquals(SUCCESS_MESSAGE, result, "Debe retornar el mensaje de éxito.");
        thenRepositoryDesactivateWasCalled(EMPTY_EMAIL, 1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenEmailIsNull() {
        givenRepositoryThrowsRuntimeException(NULL_EMAIL);

        assertThrows(RuntimeException.class,
                () -> whenExecuteDesactivateUser(NULL_EMAIL),
                "Se espera que el RuntimeException se propague al delegar el email nulo.");

        thenRepositoryDesactivateWasCalled(NULL_EMAIL, 1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenRepositoryFails() {
        givenRepositoryThrowsRuntimeException(VALID_EMAIL);

        assertThrows(RuntimeException.class,
                () -> whenExecuteDesactivateUser(VALID_EMAIL),
                "Se espera que el RuntimeException se propague si el repositorio falla.");

        thenRepositoryDesactivateWasCalled(VALID_EMAIL, 1);
    }


    //privadoss
    private void givenRepositoryThrowsRuntimeException(String email) {
        doThrow(new RuntimeException("Simulated DB error"))
                .when(userRepository).desactivateUser(email);
    }

    private String whenExecuteDesactivateUser(String email) {
        return desactivateUser.execute(email);
    }

    private void thenRepositoryDesactivateWasCalled(String expectedEmail, int times) {
        verify(userRepository, times(times)).desactivateUser(expectedEmail);
    }
}