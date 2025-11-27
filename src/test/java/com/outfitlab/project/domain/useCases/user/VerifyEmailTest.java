package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VerifyEmailTest {

    private UserRepository userRepository = mock(UserRepository.class);
    private UserJpaRepository userJpaRepository = mock(UserJpaRepository.class);
    private VerifyEmail verifyEmail;

    private final String VALID_TOKEN = "valid-verification-token";
    private final String NOT_FOUND_TOKEN = "invalid-token";
    private final String USER_EMAIL = "user@verified.com";
    private UserModel mockUserModel;
    private UserEntity mockUserEntity;

    @BeforeEach
    void setUp() throws UserNotFoundException {
        verifyEmail = new VerifyEmail(userRepository, userJpaRepository);
        mockUserModel = mock(UserModel.class);
        when(mockUserModel.getEmail()).thenReturn(USER_EMAIL);
        mockUserEntity = mock(UserEntity.class);
        when(mockUserEntity.getEmail()).thenReturn(USER_EMAIL);
        when(userRepository.findUserByVerificationToken(VALID_TOKEN)).thenReturn(mockUserModel);
        when(userJpaRepository.findByEmail(USER_EMAIL)).thenReturn(mockUserEntity);
    }


    @Test
    public void shouldSetUserVerifiedAndClearTokenWhenTokenIsValid() throws UserNotFoundException {
        whenExecuteVerifyEmail(VALID_TOKEN);

        thenUserWasFoundByToken(VALID_TOKEN, 1);
        thenUserEntityWasUpdatedAndSaved();
    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenTokenIsNotFound() throws UserNotFoundException {
        doThrow(UserNotFoundException.class).when(userRepository).findUserByVerificationToken(NOT_FOUND_TOKEN);

        assertThrows(UserNotFoundException.class,
                () -> whenExecuteVerifyEmail(NOT_FOUND_TOKEN),
                "Debe fallar si el token no encuentra un usuario.");

        thenUserWasFoundByToken(NOT_FOUND_TOKEN, 1);
        thenUserEntityWasNeverSaved();
    }

    @Test
    public void shouldPropagateRuntimeExceptionIfUserEntityCannotBeFoundByEmail() {
        when(userJpaRepository.findByEmail(USER_EMAIL)).thenReturn(null);

        assertThrows(RuntimeException.class,
                () -> whenExecuteVerifyEmail(VALID_TOKEN),
                "Debe propagar un fallo si UserEntity no se encuentra para el email.");

        thenUserWasFoundByToken(VALID_TOKEN, 1);
        thenRepositorySaveWasNeverCalled();
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenSavingUserEntityFails() throws UserNotFoundException {
        doThrow(new RuntimeException("DB Save Error")).when(userJpaRepository).save(mockUserEntity);

        assertThrows(RuntimeException.class,
                () -> whenExecuteVerifyEmail(VALID_TOKEN),
                "Debe propagar RuntimeException si la persistencia falla.");

        thenUserEntityWasUpdated(1);
    }


    //privadoss
    private void whenExecuteVerifyEmail(String token) throws UserNotFoundException {
        verifyEmail.execute(token);
    }

    private void thenUserWasFoundByToken(String token, int times) throws UserNotFoundException {
        verify(userRepository, times(times)).findUserByVerificationToken(token);
    }

    private void thenUserEntityWasUpdatedAndSaved() {
        verify(mockUserEntity, times(1)).setVerified(true);
        verify(mockUserEntity, times(1)).setVerificationToken(null);
        verify(userJpaRepository, times(1)).save(mockUserEntity);
    }

    private void thenUserEntityWasUpdated(int times) {
        verify(mockUserEntity, times(times)).setVerified(true);
        verify(mockUserEntity, times(times)).setVerificationToken(null);
    }

    private void thenUserEntityWasNeverSaved() {
        verify(userJpaRepository, never()).save(any(UserEntity.class));
    }

    private void thenRepositorySaveWasNeverCalled() {
        verify(userJpaRepository, never()).save(any(UserEntity.class));
    }
}