package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.exceptions.PasswordException;
import com.outfitlab.project.domain.exceptions.PasswordIsNotTheSame;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UpdateUserTest {

    private UserRepository userRepository = mock(UserRepository.class);
    private PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private UpdateUser updateUser;

    private final String OLD_EMAIL = "old@user.com";
    private final String NEW_EMAIL = "new@user.com";
    private final String NAME = "Ailin";
    private final String LAST_NAME = "Vara";
    private final String RAW_PASS = "contraseña123";
    private final String DIFFERENT_PASS = "contraseña-diferente";
    private final String HASHED_PASS = "contraseña-hasheada_xyz";
    private final String IMAGE_URL = "http://image.com/new.jpg";
    private UserModel mockUpdatedModel;
    private UserModel mockExistingUserModel;

    @BeforeEach
    void setUp() throws UserNotFoundException {
        updateUser = new UpdateUser(userRepository, passwordEncoder);
        mockUpdatedModel = mock(UserModel.class);
        mockExistingUserModel = mock(UserModel.class);
        when(userRepository.findUserByEmail(OLD_EMAIL)).thenReturn(mockExistingUserModel);
        doThrow(UserNotFoundException.class).when(userRepository).findUserByEmail(NEW_EMAIL);
    }


    @Test
    public void shouldUpdateUserDataWithoutChangingEmailOrPassword() throws Exception {
        givenRepositoryReturnsUpdatedModel(OLD_EMAIL, mockUpdatedModel);

        UserModel result = whenExecuteUpdate(OLD_EMAIL, NAME, LAST_NAME, OLD_EMAIL, null, null, IMAGE_URL);

        thenResultMatchesExpectedModel(result, mockUpdatedModel);
        thenNewEmailCheckWasSkipped(NEW_EMAIL, 0);
        thenRepositoryUpdateWasCalled(OLD_EMAIL, NAME, LAST_NAME, OLD_EMAIL, "", IMAGE_URL, 1);
        thenPasswordEncoderWasNeverCalled();
    }

    @Test
    public void shouldUpdateAllFieldsIncludingEmailAndHashPassword() throws Exception {
        givenRepositoryReturnsUpdatedModel(OLD_EMAIL, mockUpdatedModel);
        givenPasswordEncoderReturnsHashedPassword(RAW_PASS, HASHED_PASS);

        UserModel result = whenExecuteUpdate(OLD_EMAIL, NAME, LAST_NAME, NEW_EMAIL, RAW_PASS, RAW_PASS, IMAGE_URL);

        thenResultMatchesExpectedModel(result, mockUpdatedModel);
        thenNewEmailCheckWasPerformed(NEW_EMAIL, 1);
        thenPasswordEncoderWasCalled(RAW_PASS, 1);
        thenRepositoryUpdateWasCalled(OLD_EMAIL, NAME, LAST_NAME, NEW_EMAIL, HASHED_PASS, IMAGE_URL, 1);
    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenOldUserDoesNotExist() throws UserNotFoundException {
        doThrow(UserNotFoundException.class).when(userRepository).findUserByEmail(OLD_EMAIL);

        assertThrows(UserNotFoundException.class,
                () -> whenExecuteUpdate(OLD_EMAIL, NAME, LAST_NAME, OLD_EMAIL, null, null, IMAGE_URL),
                "Debe fallar si el usuario original no se encuentra.");

        thenUserExistenceCheckWasCalled(OLD_EMAIL, 1);
        thenRepositoryUpdateWasNeverCalled();
    }

    @Test
    public void shouldThrowPasswordExceptionWhenOnlyPasswordIsSent() {
        assertThrows(PasswordException.class,
                () -> whenExecuteUpdate(OLD_EMAIL, NAME, LAST_NAME, OLD_EMAIL, RAW_PASS, null, IMAGE_URL),
                "Debe fallar si falta la confirmación.");

        thenRepositoryUpdateWasNeverCalled();
    }

    @Test
    public void shouldThrowPasswordExceptionWhenOnlyConfirmPasswordIsSent() {
        assertThrows(PasswordException.class,
                () -> whenExecuteUpdate(OLD_EMAIL, NAME, LAST_NAME, OLD_EMAIL, null, RAW_PASS, IMAGE_URL),
                "Debe fallar si falta la contraseña.");

        thenRepositoryUpdateWasNeverCalled();
    }

    @Test
    public void shouldThrowPasswordIsNotTheSameExceptionWhenPasswordsDoNotMatch() {
        assertThrows(PasswordIsNotTheSame.class,
                () -> whenExecuteUpdate(OLD_EMAIL, NAME, LAST_NAME, OLD_EMAIL, RAW_PASS, DIFFERENT_PASS, IMAGE_URL),
                "Debe fallar si las contraseñas son diferentes.");

        thenRepositoryUpdateWasNeverCalled();
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenRepositoryUpdateFails() throws Exception {
        givenRepositoryThrowsRuntimeException(OLD_EMAIL);

        assertThrows(RuntimeException.class,
                () -> whenExecuteUpdate(OLD_EMAIL, NAME, LAST_NAME, OLD_EMAIL, null, null, IMAGE_URL),
                "Se espera que el RuntimeException se propague si el repositorio falla.");

        thenUserExistenceCheckWasCalled(OLD_EMAIL, 1);
        thenRepositoryUpdateWasCalled(OLD_EMAIL, NAME, LAST_NAME, OLD_EMAIL, "", IMAGE_URL, 1);
    }


    //privadoss
    private void givenRepositoryReturnsUpdatedModel(String oldEmail, UserModel updatedModel) {
        when(userRepository.updateUser(eq(oldEmail), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(updatedModel);
    }

    private void givenRepositoryThrowsRuntimeException(String oldEmail) {
        doThrow(new RuntimeException("Simulated DB error"))
                .when(userRepository).updateUser(eq(oldEmail), anyString(), anyString(), anyString(), anyString(), anyString());
    }

    private void givenPasswordEncoderReturnsHashedPassword(String rawPass, String hashedPass) {
        when(passwordEncoder.encode(rawPass)).thenReturn(hashedPass);
    }

    private UserModel whenExecuteUpdate(String oldEmail, String name, String lastname, String newEmail, String password, String confirmPassword, String newImageUrl) {
        return updateUser.execute(oldEmail, name, lastname, newEmail, password, confirmPassword, newImageUrl);
    }

    private void thenResultMatchesExpectedModel(UserModel actual, UserModel expected) {
        assertNotNull(actual, "El modelo de usuario devuelto no debe ser nulo.");
        assertEquals(expected, actual, "El modelo devuelto debe coincidir con el modelo simulado.");
    }

    private void thenNewEmailCheckWasPerformed(String newEmail, int times) {
        verify(userRepository, times(times)).findUserByEmail(newEmail);
    }

    private void thenNewEmailCheckWasSkipped(String newEmail, int times) {
        verify(userRepository, times(times)).findUserByEmail(newEmail);
    }

    private void thenUserExistenceCheckWasCalled(String email, int times) {
        verify(userRepository, times(times)).findUserByEmail(email);
    }

    private void thenPasswordEncoderWasCalled(String rawPass, int times) {
        verify(passwordEncoder, times(times)).encode(rawPass);
    }

    private void thenPasswordEncoderWasNeverCalled() {
        verify(passwordEncoder, never()).encode(anyString());
    }

    private void thenRepositoryUpdateWasCalled(String oldEmail, String name, String lastname, String newEmail, String hashedPassword, String newImageUrl, int times) {
        verify(userRepository, times(times)).updateUser(oldEmail, name, lastname, newEmail, hashedPassword, newImageUrl);
    }

    private void thenRepositoryUpdateWasNeverCalled() {
        verify(userRepository, never()).updateUser(anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
    }
}