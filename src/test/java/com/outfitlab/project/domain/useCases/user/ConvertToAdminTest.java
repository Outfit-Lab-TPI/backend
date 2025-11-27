package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ConvertToAdminTest {

    private UserRepository userRepository = mock(UserRepository.class);
    private ConvertToAdmin convertToAdmin;

    private final String VALID_EMAIL = "user.to.admin@test.com";
    private final String EMPTY_EMAIL = "";
    private final String NULL_EMAIL = null;
    private final String SUCCESS_MESSAGE = "El usuario se ha convertido a Administrador con éxito.";

    @BeforeEach
    void setUp() {
        convertToAdmin = new ConvertToAdmin(userRepository);
    }


    @Test
    public void shouldCallRepositoryToConvertUserToAdminAndReturnSuccessMessage() {
        String result = whenExecuteConvertToAdmin(VALID_EMAIL);

        assertEquals(SUCCESS_MESSAGE, result, "Debe retornar el mensaje de éxito.");
        thenRepositoryConvertWasCalled(VALID_EMAIL, 1);
    }

    @Test
    public void shouldCallRepositoryWithEmptyStringWhenEmailIsEmpty() {
        String result = whenExecuteConvertToAdmin(EMPTY_EMAIL);

        assertEquals(SUCCESS_MESSAGE, result, "Debe retornar el mensaje de éxito.");
        thenRepositoryConvertWasCalled(EMPTY_EMAIL, 1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenEmailIsNull() {
        givenRepositoryThrowsRuntimeException(NULL_EMAIL);

        assertThrows(RuntimeException.class,
                () -> whenExecuteConvertToAdmin(NULL_EMAIL),
                "Se espera que el RuntimeException se propague al delegar el email nulo.");

        thenRepositoryConvertWasCalled(NULL_EMAIL, 1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenRepositoryFails() {
        givenRepositoryThrowsRuntimeException(VALID_EMAIL);

        assertThrows(RuntimeException.class,
                () -> whenExecuteConvertToAdmin(VALID_EMAIL),
                "Se espera que el RuntimeException se propague si el repositorio falla.");

        thenRepositoryConvertWasCalled(VALID_EMAIL, 1);
    }


    //privados
    private void givenRepositoryThrowsRuntimeException(String email) {
        doThrow(new RuntimeException("Simulated DB error"))
                .when(userRepository).convertToAdmin(email);
    }

    private String whenExecuteConvertToAdmin(String email) {
        return convertToAdmin.execute(email);
    }

    private void thenRepositoryConvertWasCalled(String expectedEmail, int times) {
        verify(userRepository, times(times)).convertToAdmin(expectedEmail);
    }
}