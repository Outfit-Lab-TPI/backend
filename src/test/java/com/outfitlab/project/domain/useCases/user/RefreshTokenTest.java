package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.infrastructure.config.security.AuthResponse;
import com.outfitlab.project.infrastructure.config.security.jwt.JwtService;
import com.outfitlab.project.infrastructure.repositories.interfaces.TokenRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import com.outfitlab.project.infrastructure.model.UserEntity;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RefreshTokenTest {

    private JwtService jwtService = mock(JwtService.class);
    private UserJpaRepository userJpaRepository = mock(UserJpaRepository.class);
    private TokenRepository tokenRepository = mock(TokenRepository.class);
    private RefreshToken refreshTokenUseCase;

    private final String VALID_REFRESH_TOKEN = "valid.refresh.token";
    private final String INVALID_REFRESH_TOKEN = "expired.or.invalid.token";
    private final String NEW_ACCESS_TOKEN = "new.access.token";
    private final String NEW_REFRESH_TOKEN = "new.refresh.token";
    private final String FIXED_EMAIL = "user@test.com";
    private final Long USER_ID = 1L;
    private UserEntity mockUserEntity;

    @BeforeEach
    void setUp() {
        refreshTokenUseCase = new RefreshToken(jwtService, userJpaRepository, tokenRepository);
        mockUserEntity = mock(UserEntity.class);
        when(mockUserEntity.getEmail()).thenReturn(FIXED_EMAIL);
        when(mockUserEntity.getId()).thenReturn(USER_ID);
    }


    @Test
    public void shouldReturnNewTokensWhenRefreshTokenIsValid() throws Exception {
        when(jwtService.isTokenExpired(VALID_REFRESH_TOKEN)).thenReturn(false);
        when(jwtService.extractUsername(VALID_REFRESH_TOKEN)).thenReturn(FIXED_EMAIL);
        when(userJpaRepository.getByEmail(FIXED_EMAIL)).thenReturn(Optional.of(mockUserEntity));
        when(jwtService.isTokenValid(VALID_REFRESH_TOKEN, mockUserEntity)).thenReturn(true);

        givenTokensAreGenerated();
        givenRepositoryHandlesTokenRevocation(mockUserEntity, 0);

        ResponseEntity<AuthResponse> response = whenExecuteRefresh(VALID_REFRESH_TOKEN);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(NEW_ACCESS_TOKEN, response.getBody().getAccess_token());

        thenTokenRevocationWasCalled(USER_ID, 1);
        thenNewAccessTokenWasSaved(mockUserEntity, NEW_ACCESS_TOKEN, 1);
    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenRefreshTokenIsNull() {
        String nullToken = null;

        assertThrows(UserNotFoundException.class,
                () -> whenExecuteRefresh(nullToken),
                "Debe fallar si el token es nulo.");

        thenJwtServiceWasNeverCalled();
        thenTokenRevocationWasNeverCalled();
    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenRefreshTokenIsBlank() {
        String blankToken = "   ";

        assertThrows(UserNotFoundException.class,
                () -> whenExecuteRefresh(blankToken),
                "Debe fallar si el token está en blanco.");

        thenJwtServiceWasNeverCalled();
        thenTokenRevocationWasNeverCalled();
    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenTokenIsExpiredByCheck() {
        when(jwtService.isTokenExpired(INVALID_REFRESH_TOKEN)).thenReturn(true);

        assertThrows(UserNotFoundException.class,
                () -> whenExecuteRefresh(INVALID_REFRESH_TOKEN),
                "Debe fallar si isTokenExpired() retorna true.");

        thenTokenExtractionWasNeverCalled();
        thenTokenRevocationWasNeverCalled();
    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenUserExtractedFromTokenDoesNotExist() {
        when(jwtService.isTokenExpired(INVALID_REFRESH_TOKEN)).thenReturn(false);
        when(jwtService.extractUsername(INVALID_REFRESH_TOKEN)).thenReturn(FIXED_EMAIL);
        when(userJpaRepository.getByEmail(FIXED_EMAIL)).thenReturn(Optional.empty()); // Fallo de búsqueda

        assertThrows(UserNotFoundException.class,
                () -> whenExecuteRefresh(INVALID_REFRESH_TOKEN),
                "Debe fallar si userJpaRepository no encuentra el usuario.");

        thenUserExtractionWasCalled(1);
        thenTokenRevocationWasNeverCalled();
    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenTokenIsNotValidForUser() {
        when(jwtService.isTokenExpired(INVALID_REFRESH_TOKEN)).thenReturn(false);
        when(jwtService.extractUsername(INVALID_REFRESH_TOKEN)).thenReturn(FIXED_EMAIL);
        when(userJpaRepository.getByEmail(FIXED_EMAIL)).thenReturn(Optional.of(mockUserEntity));
        when(jwtService.isTokenValid(INVALID_REFRESH_TOKEN, mockUserEntity)).thenReturn(false); // Fallo de validez

        assertThrows(UserNotFoundException.class,
                () -> whenExecuteRefresh(INVALID_REFRESH_TOKEN),
                "Debe fallar si isTokenValid() retorna false.");

        thenTokenRevocationWasNeverCalled();
    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenExpiredJwtExceptionIsThrown() {
        doThrow(ExpiredJwtException.class).when(jwtService).isTokenExpired(INVALID_REFRESH_TOKEN);

        assertThrows(UserNotFoundException.class,
                () -> whenExecuteRefresh(INVALID_REFRESH_TOKEN));
    }

    @Test
    public void shouldSkipRevocationWhenNoExistingTokensAreFound() throws Exception {
        when(jwtService.isTokenExpired(VALID_REFRESH_TOKEN)).thenReturn(false);
        when(jwtService.extractUsername(VALID_REFRESH_TOKEN)).thenReturn(FIXED_EMAIL);
        when(userJpaRepository.getByEmail(FIXED_EMAIL)).thenReturn(Optional.of(mockUserEntity));
        when(jwtService.isTokenValid(VALID_REFRESH_TOKEN, mockUserEntity)).thenReturn(true);

        givenTokensAreGenerated();

        when(tokenRepository.allValidTokensByUser(USER_ID)).thenReturn(Collections.emptyList());
        whenExecuteRefresh(VALID_REFRESH_TOKEN);

        verify(tokenRepository, never()).saveAll(any());
        thenNewAccessTokenWasSaved(mockUserEntity, NEW_ACCESS_TOKEN, 1);
    }


    //privados
    private void givenRepositoryHandlesTokenRevocation(UserEntity user, int tokenCount) {
        if (tokenCount == 0) {
            when(tokenRepository.allValidTokensByUser(user.getId())).thenReturn(Collections.emptyList());
        }
    }

    private void givenUserExists(String email, UserEntity user) {
        when(userJpaRepository.getByEmail(email)).thenReturn(Optional.of(user));
    }

    private void givenUserDoesNotExist(String email) {
        when(userJpaRepository.getByEmail(email)).thenReturn(Optional.empty());
    }

    private void givenTokensAreGenerated() {
        when(jwtService.generateToken(any(UserEntity.class))).thenReturn(NEW_ACCESS_TOKEN);
        when(jwtService.generateRefreshToken(any(UserEntity.class))).thenReturn(NEW_REFRESH_TOKEN);
    }

    private ResponseEntity<AuthResponse> whenExecuteRefresh(String refreshToken) {
        return refreshTokenUseCase.execute(refreshToken);
    }

    private void thenTokenRevocationWasCalled(Long userId, int times) {
        verify(tokenRepository, times(times)).allValidTokensByUser(userId);
    }

    private void thenTokenExtractionWasNeverCalled() {
        verify(jwtService, never()).extractUsername(anyString());
    }

    private void thenUserExtractionWasCalled(int times) {
        verify(jwtService, times(times)).extractUsername(anyString());
        verify(userJpaRepository, times(times)).getByEmail(FIXED_EMAIL);
    }

    private void thenNewAccessTokenWasSaved(UserEntity user, String token, int times) {
        verify(tokenRepository, times(times)).save(any());
    }

    private void thenTokenRevocationWasNeverCalled() {
        verify(tokenRepository, never()).allValidTokensByUser(anyLong());
    }

    private void thenJwtServiceWasNeverCalled() {
        verify(jwtService, never()).isTokenExpired(anyString());
        verify(jwtService, never()).extractUsername(anyString());
    }
}