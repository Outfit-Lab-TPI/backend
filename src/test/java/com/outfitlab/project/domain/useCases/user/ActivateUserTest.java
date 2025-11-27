package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ActivateUserTest {

    private UserRepository userRepository = mock(UserRepository.class);
    private ActivateUser activateUser;

    private final String VALID_EMAIL = "test@user.com";
    private final String EMPTY_EMAIL = "";
    private final String NULL_EMAIL = null;
    private final String SUCCESS_MESSAGE = "Usuario activado con éxito.";

    @BeforeEach
    void setUp() {
        activateUser = new ActivateUser(userRepository);
    }


    @Test
    public void shouldCallRepositoryToActivateUserAndReturnSuccessMessage() {
        String result = whenExecuteActivateUser(VALID_EMAIL);

        assertEquals(SUCCESS_MESSAGE, result, "Debe retornar el mensaje de éxito.");
        thenRepositoryActivateWasCalled(VALID_EMAIL, 1);
    }

    @Test
    public void shouldCallRepositoryWithEmptyStringWhenEmailIsEmpty() {
        String result = whenExecuteActivateUser(EMPTY_EMAIL);

        assertEquals(SUCCESS_MESSAGE, result, "Debe retornar el mensaje de éxito.");
        thenRepositoryActivateWasCalled(EMPTY_EMAIL, 1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenEmailIsNull() {
        givenRepositoryThrowsRuntimeException(NULL_EMAIL);

        assertThrows(RuntimeException.class,
                () -> whenExecuteActivateUser(NULL_EMAIL),
                "Se espera que el RuntimeException se propague al delegar el email nulo.");

        thenRepositoryActivateWasCalled(NULL_EMAIL, 1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenRepositoryFails() {
        givenRepositoryThrowsRuntimeException(VALID_EMAIL);

        assertThrows(RuntimeException.class,
                () -> whenExecuteActivateUser(VALID_EMAIL),
                "Se espera que el RuntimeException se propague si el repositorio falla.");

        thenRepositoryActivateWasCalled(VALID_EMAIL, 1);
    }


    //privadoss
    private void givenRepositoryThrowsRuntimeException(String email) {
        doThrow(new RuntimeException("Simulated DB error"))
                .when(userRepository).activateUser(email);
    }

    private String whenExecuteActivateUser(String email) {
        return activateUser.execute(email);
    }

    private void thenRepositoryActivateWasCalled(String expectedEmail, int times) {
        verify(userRepository, times(times)).activateUser(expectedEmail);
    }
}