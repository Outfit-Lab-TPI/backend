package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ConvertToUserTest {

    private UserRepository userRepository = mock(UserRepository.class);
    private ConvertToUser convertToUser;

    private final String VALID_EMAIL = "admin.to.user@test.com";
    private final String EMPTY_EMAIL = "";
    private final String NULL_EMAIL = null;
    private final String SUCCESS_MESSAGE = "El administrador se ha convertido a Usuario con éxito.";

    @BeforeEach
    void setUp() {
        convertToUser = new ConvertToUser(userRepository);
    }


    @Test
    public void shouldCallRepositoryToConvertAdminToUserAndReturnSuccessMessage() {
        String result = whenExecuteConvertToUser(VALID_EMAIL);

        assertEquals(SUCCESS_MESSAGE, result, "Debe retornar el mensaje de éxito.");
        thenRepositoryConvertWasCalled(VALID_EMAIL, 1);
    }

    @Test
    public void shouldCallRepositoryWithEmptyStringWhenEmailIsEmpty() {
        String result = whenExecuteConvertToUser(EMPTY_EMAIL);

        assertEquals(SUCCESS_MESSAGE, result, "Debe retornar el mensaje de éxito.");
        thenRepositoryConvertWasCalled(EMPTY_EMAIL, 1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenEmailIsNull() {
        givenRepositoryThrowsRuntimeException(NULL_EMAIL);

        assertThrows(RuntimeException.class,
                () -> whenExecuteConvertToUser(NULL_EMAIL),
                "Se espera que el RuntimeException se propague al delegar el email nulo.");

        thenRepositoryConvertWasCalled(NULL_EMAIL, 1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenRepositoryFails() {
        givenRepositoryThrowsRuntimeException(VALID_EMAIL);

        assertThrows(RuntimeException.class,
                () -> whenExecuteConvertToUser(VALID_EMAIL),
                "Se espera que el RuntimeException se propague si el repositorio falla.");

        thenRepositoryConvertWasCalled(VALID_EMAIL, 1);
    }


    //privadoss
    private void givenRepositoryThrowsRuntimeException(String email) {
        doThrow(new RuntimeException("Simulated DB error"))
                .when(userRepository).convertToUser(email);
    }

    private String whenExecuteConvertToUser(String email) {
        return convertToUser.execute(email);
    }

    private void thenRepositoryConvertWasCalled(String expectedEmail, int times) {
        verify(userRepository, times(times)).convertToUser(expectedEmail);
    }
}