package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserProfileTest {

    private UserJpaRepository userJpaRepository = mock(UserJpaRepository.class);
    private UserProfile userProfile;

    private final String USER_EMAIL = "test@profile.com";
    private UserModel mockUserModel;
    private UserEntity mockUserEntity;
    private Authentication mockAuthentication;
    private UserDetails mockUserDetails;

    @BeforeEach
    void setUp() {
        userProfile = new UserProfile(userJpaRepository);
        mockUserEntity = mock(UserEntity.class);
        when(mockUserEntity.getEmail()).thenReturn(USER_EMAIL);
        mockUserModel = mock(UserModel.class);
        when(mockUserModel.getEmail()).thenReturn(USER_EMAIL);
        mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn(USER_EMAIL);
        mockAuthentication = mock(Authentication.class);
    }


    @Test
    public void shouldReturnUserModelWhenUserIsAuthenticatedAndFound() throws Exception {
        givenRepositoryFindsUser(USER_EMAIL, mockUserEntity);

        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            givenSecurityContextSetup(mockedContext, true, mockUserDetails);

            UserModel result = userProfile.execute();

            assertNotNull(result, "El modelo de usuario no debe ser nulo.");
            assertEquals(USER_EMAIL, result.getEmail(), "El email del perfil devuelto debe coincidir.");
            thenRepositoryWasCalledOnce(USER_EMAIL);
        }
    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenAuthenticationIsNull() throws Exception {
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            givenSecurityContextSetup(mockedContext, false, null); // Authentication es null

            assertThrows(UserNotFoundException.class,
                    () -> userProfile.execute(),
                    "Debe fallar si no hay Authentication en el contexto.");

            thenRepositoryWasNeverCalled();
        }
    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenUserIsNotAuthenticated() throws Exception {
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            givenSecurityContextSetup(mockedContext, false, mockUserDetails); // No autenticado

            assertThrows(UserNotFoundException.class,
                    () -> userProfile.execute(),
                    "Debe fallar si el usuario no está marcado como autenticado.");

            thenRepositoryWasNeverCalled();
        }
    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenUserIsNotInRepository() throws Exception {
        givenRepositoryDoesNotFindUser(USER_EMAIL);

        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            givenSecurityContextSetup(mockedContext, true, mockUserDetails);

            assertThrows(UserNotFoundException.class,
                    () -> userProfile.execute(),
                    "Debe fallar si el repositorio devuelve Optional.empty().");

            thenRepositoryWasCalledOnce(USER_EMAIL);
        }
    }


    //privadossss
    private void givenSecurityContextSetup(MockedStatic<SecurityContextHolder> mockedContext, boolean isAuthenticated, UserDetails userDetails) {

        SecurityContext securityContext = mock(SecurityContext.class);

        if (userDetails != null) {
            when(mockAuthentication.isAuthenticated()).thenReturn(isAuthenticated);
            when(mockAuthentication.getPrincipal()).thenReturn(userDetails);
            when(securityContext.getAuthentication()).thenReturn(mockAuthentication);
        } else {
            when(securityContext.getAuthentication()).thenReturn(null);
        }

        mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContext);

        try (MockedStatic<UserEntity> mockedEntity = mockStatic(UserEntity.class)) {
            mockedEntity.when(() -> UserEntity.convertEntityToModel(mockUserEntity)).thenReturn(mockUserModel);
        }
    }

    private void givenRepositoryFindsUser(String email, UserEntity entity) {
        when(userJpaRepository.getByEmail(email)).thenReturn(Optional.of(entity));
    }

    private void givenRepositoryDoesNotFindUser(String email) {
        when(userJpaRepository.getByEmail(email)).thenReturn(Optional.empty());
    }

    private UserModel whenExecuteUserProfile() {
        return userProfile.execute();
    }

    private void thenRepositoryWasCalledOnce(String email) {
        verify(userJpaRepository, times(1)).getByEmail(email);
    }

    private void thenRepositoryWasNeverCalled() {
        verify(userJpaRepository, never()).getByEmail(anyString());
    }
}