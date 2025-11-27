package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetUserByEmailTest {

    private UserRepository userRepository = mock(UserRepository.class);
    private GetUserByEmail getUserByEmail;

    private final String VALID_EMAIL = "user@found.com";
    private final String NOT_FOUND_EMAIL = "user@notfound.com";
    private final String NULL_EMAIL = null;
    private final String EMPTY_EMAIL = "";
    private UserModel mockUserModel;

    @BeforeEach
    void setUp() {
        mockUserModel = mock(UserModel.class);
        when(mockUserModel.getEmail()).thenReturn(VALID_EMAIL);
        getUserByEmail = new GetUserByEmail(userRepository);
    }


    @Test
    public void shouldReturnUserModelWhenUserIsFound() throws UserNotFoundException {
        givenRepositoryFindsUser(VALID_EMAIL, mockUserModel);

        UserModel result = whenExecuteGetUser(VALID_EMAIL);

        thenResultMatchesExpectedModel(result, mockUserModel, VALID_EMAIL);
        thenRepositoryFindUserByEmailWasCalled(VALID_EMAIL, 1);
    }

    @Test
    public void shouldPropagateUserNotFoundExceptionWhenUserIsNotFound() throws UserNotFoundException {
        givenRepositoryThrowsUserNotFound(NOT_FOUND_EMAIL);

        assertThrows(UserNotFoundException.class,
                () -> whenExecuteGetUser(NOT_FOUND_EMAIL),
                "Se espera que UserNotFoundException se propague.");

        thenRepositoryFindUserByEmailWasCalled(NOT_FOUND_EMAIL, 1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenEmailIsNull() throws UserNotFoundException {
        givenRepositoryThrowsRuntimeException(NULL_EMAIL);

        assertThrows(RuntimeException.class,
                () -> whenExecuteGetUser(NULL_EMAIL),
                "Se espera que el RuntimeException se propague al delegar email nulo.");

        thenRepositoryFindUserByEmailWasCalled(NULL_EMAIL, 1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenEmailIsEmpty() throws UserNotFoundException {
        givenRepositoryThrowsRuntimeException(EMPTY_EMAIL);

        assertThrows(RuntimeException.class,
                () -> whenExecuteGetUser(EMPTY_EMAIL),
                "Se espera que el RuntimeException se propague al delegar email vacío.");

        thenRepositoryFindUserByEmailWasCalled(EMPTY_EMAIL, 1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenRepositoryFails() throws UserNotFoundException {
        givenRepositoryThrowsRuntimeException(VALID_EMAIL);

        assertThrows(RuntimeException.class,
                () -> whenExecuteGetUser(VALID_EMAIL),
                "Se espera que el RuntimeException se propague si el repositorio falla.");

        thenRepositoryFindUserByEmailWasCalled(VALID_EMAIL, 1);
    }


    //privados
    private void givenRepositoryFindsUser(String email, UserModel model) throws UserNotFoundException {
        when(userRepository.findUserByEmail(email)).thenReturn(model);
    }

    private void givenRepositoryThrowsUserNotFound(String email) throws UserNotFoundException {
        doThrow(new UserNotFoundException("Usuario no encontrado")).when(userRepository).findUserByEmail(email);
    }

    private void givenRepositoryThrowsRuntimeException(String email) throws UserNotFoundException {
        doThrow(new RuntimeException("Simulated DB error")).when(userRepository).findUserByEmail(email);
    }

    private UserModel whenExecuteGetUser(String email) throws UserNotFoundException {
        return getUserByEmail.execute(email);
    }

    private void thenResultMatchesExpectedModel(UserModel actual, UserModel expected, String expectedEmail) {
        assertNotNull(actual, "El modelo de usuario devuelto no debe ser nulo.");
        assertEquals(expected, actual, "El modelo devuelto debe coincidir con el modelo simulado.");
        assertEquals(expectedEmail, actual.getEmail(), "El email del usuario debe coincidir.");
    }

    private void thenRepositoryFindUserByEmailWasCalled(String email, int times) throws UserNotFoundException {
        verify(userRepository, times(times)).findUserByEmail(email);
    }
}