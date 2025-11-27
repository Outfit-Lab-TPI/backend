package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.exceptions.NullFieldsException;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.dto.LoginDTO;
import com.outfitlab.project.infrastructure.config.security.AuthResponse;
import com.outfitlab.project.infrastructure.config.security.jwt.JwtService;
import com.outfitlab.project.infrastructure.repositories.interfaces.TokenRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import com.outfitlab.project.infrastructure.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginUserTest {

    private UserRepository userRepository = mock(UserRepository.class);
    private UserJpaRepository userJpaRepository = mock(UserJpaRepository.class);
    private TokenRepository tokenRepository = mock(TokenRepository.class);
    private JwtService jwtService = mock(JwtService.class);
    private PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private AuthenticationManager authManager = mock(AuthenticationManager.class);
    private Authentication mockAuthentication = mock(Authentication.class);
    private LoginUser loginUser;

    private final String FIXED_EMAIL = "test@user.com";
    private final String FIXED_PASSWORD = "password123";
    private final String ACCESS_TOKEN = "access.token.jwt";
    private final String REFRESH_TOKEN = "refresh.token.jwt";
    private final Long USER_ID = 1L;
    private LoginDTO validLoginDTO;
    private UserEntity mockUserEntity;

    @BeforeEach
    void setUp() {
        loginUser = new LoginUser(userRepository, passwordEncoder, authManager, tokenRepository, jwtService, userJpaRepository);
        validLoginDTO = new LoginDTO(FIXED_EMAIL, FIXED_PASSWORD);
        mockUserEntity = mock(UserEntity.class);
        when(mockUserEntity.getEmail()).thenReturn(FIXED_EMAIL);
        when(mockUserEntity.isVerified()).thenReturn(true);
        when(mockUserEntity.getId()).thenReturn(USER_ID);
    }


    @Test
    public void shouldReturnAuthResponseWithTokensWhenLoginIsSuccessful() {
        givenAuthenticationSucceeds(mockUserEntity, true);
        givenTokensAreGenerated();
        givenRepositoryHandlesTokenRevocation(mockUserEntity, 0);

        ResponseEntity<AuthResponse> response = whenExecuteLogin(validLoginDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        assertEquals(ACCESS_TOKEN, response.getBody().getAccess_token());

        thenNewAccessTokenWasSaved(1);
        thenTokenRevocationWasCalled(USER_ID, 1);
    }

    @Test
    public void shouldThrowNullFieldsExceptionWhenEmailIsBlank() {
        LoginDTO blankEmailDTO = new LoginDTO("   ", FIXED_PASSWORD);

        assertThrows(NullFieldsException.class, () -> whenExecuteLogin(blankEmailDTO));
        thenAuthenticationWasNeverCalled();
    }

    @Test
    public void shouldThrowNullFieldsExceptionWhenPasswordIsBlank() {
        LoginDTO blankPasswordDTO = new LoginDTO(FIXED_EMAIL, " ");

        assertThrows(NullFieldsException.class, () -> whenExecuteLogin(blankPasswordDTO));
        thenAuthenticationWasNeverCalled();
    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenAuthenticationFails() {
        givenAuthenticationFails();

        assertThrows(UserNotFoundException.class, () -> whenExecuteLogin(validLoginDTO));
        thenAuthenticationWasCalled(validLoginDTO, 1);
        thenUserEntityLookupsWereNeverCalled();
    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenUserIsNotVerified() {
        givenAuthenticationSucceeds(mockUserEntity, false);

        assertThrows(UserNotFoundException.class, () -> whenExecuteLogin(validLoginDTO));
        thenAuthenticationWasCalled(validLoginDTO, 1);
        thenTokenRevocationWasNeverCalled();
    }

    @Test
    public void shouldCallRevocationProcessBeforeSavingNewToken() {
        givenAuthenticationSucceeds(mockUserEntity, true);
        givenTokensAreGenerated();

        when(tokenRepository.allValidTokensByUser(USER_ID)).thenReturn(Collections.emptyList());
        whenExecuteLogin(validLoginDTO);

        thenTokenRevocationWasCalled(USER_ID, 1);
    }


    @Test
    public void shouldSaveNewAccessTokenAfterRevocation() {
        givenAuthenticationSucceeds(mockUserEntity, true);
        givenTokensAreGenerated();
        givenRepositoryHandlesTokenRevocation(mockUserEntity, 0);

        whenExecuteLogin(validLoginDTO);

        thenNewAccessTokenWasSaved(1);
    }


    //privadosss
    private void givenAuthenticationSucceeds(UserEntity user, boolean isVerified) {
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuthentication);
        when(userJpaRepository.findByEmail(FIXED_EMAIL)).thenReturn(user);
        when(user.isVerified()).thenReturn(isVerified);
        when(userJpaRepository.getByEmail(FIXED_EMAIL)).thenReturn(Optional.of(user));
    }

    private void givenAuthenticationFails() {
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(mock(AuthenticationException.class));
    }

    private void givenTokensAreGenerated() {
        when(jwtService.generateToken(any(UserEntity.class))).thenReturn(ACCESS_TOKEN);
        when(jwtService.generateRefreshToken(any(UserEntity.class))).thenReturn(REFRESH_TOKEN);
    }

    private void givenRepositoryHandlesTokenRevocation(UserEntity user, int tokenCount) {
        if (tokenCount == 0) {
            when(tokenRepository.allValidTokensByUser(user.getId())).thenReturn(Collections.emptyList());
        }
    }

    private ResponseEntity<AuthResponse> whenExecuteLogin(LoginDTO loginDTO) {
        return loginUser.execute(loginDTO);
    }

    private void thenAuthenticationWasCalled(LoginDTO dto, int times) {
        verify(authManager, times(times)).authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
    }

    private void thenAuthenticationWasNeverCalled() {
        verify(authManager, never()).authenticate(any());
    }

    private void thenUserEntityLookupsWereNeverCalled() {
        verify(userJpaRepository, never()).findByEmail(anyString());
        verify(userJpaRepository, never()).getByEmail(anyString());
    }

    private void thenTokenRevocationWasCalled(Long userId, int times) {
        verify(tokenRepository, times(times)).allValidTokensByUser(userId);
    }

    private void thenTokenRevocationWasNeverCalled() {
        verify(tokenRepository, never()).allValidTokensByUser(anyLong());
    }

    private void thenNewAccessTokenWasSaved(int times) {
        verify(tokenRepository, times(times)).save(any());
    }
}