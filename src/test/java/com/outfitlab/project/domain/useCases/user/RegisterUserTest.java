package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.exceptions.UserAlreadyExistsException;
import com.outfitlab.project.domain.interfaces.gateways.GmailGateway;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.dto.RegisterDTO;
import com.outfitlab.project.infrastructure.config.security.jwt.JwtService;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.TokenRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RegisterUserTest {

    private UserRepository userRepository = mock(UserRepository.class);
    private UserJpaRepository userJpaRepository = mock(UserJpaRepository.class);
    private TokenRepository tokenRepository = mock(TokenRepository.class);
    private JwtService jwtService = mock(JwtService.class);
    private PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private AuthenticationManager authManager = mock(AuthenticationManager.class);
    private GmailGateway gmailGateway = mock(GmailGateway.class);
    private RegisterUser registerUser;

    private static final String BASE_URL = "http://localhost:8080";
    private static final String FIXED_EMAIL = "new.user@test.com";
    private static final String RAW_PASSWORD = "contraseña123";
    private static final String HASHED_PASSWORD = "contraseña-hasheada";
    private static final String ACCESS_TOKEN = "access.token.jwt";
    private static final String REFRESH_TOKEN = "refresh.token.jwt";
    private static final String NAME = "Ailin";
    private static final String LAST_NAME = "Vara";


    private RegisterDTO validRequest;
    private UserEntity mockSavedUserEntity;

    @BeforeEach
    void setUp() {
        registerUser = new RegisterUser(
                userRepository, passwordEncoder, authManager, tokenRepository, jwtService, userJpaRepository,
                gmailGateway, BASE_URL
        );
        validRequest = mock(RegisterDTO.class);
        when(validRequest.getName()).thenReturn(NAME);
        when(validRequest.getLastName()).thenReturn(LAST_NAME);
        when(validRequest.getEmail()).thenReturn(FIXED_EMAIL);
        when(validRequest.getPassword()).thenReturn(RAW_PASSWORD);
        mockSavedUserEntity = mock(UserEntity.class);
        when(mockSavedUserEntity.getEmail()).thenReturn(FIXED_EMAIL);
        when(mockSavedUserEntity.getPassword()).thenReturn(HASHED_PASSWORD);
    }


    @Test
    public void shouldThrowUserAlreadyExistsExceptionWhenUserAlreadyExists() {
        givenUserAlreadyExists();

        assertThrows(UserAlreadyExistsException.class,
                () -> registerUser.execute(validRequest),
                "Debe fallar si el email ya está registrado.");

        thenCheckIfUserExistsWasCalled(1);
        thenPasswordEncoderWasNeverCalled();
        thenEmailWasNeverSent();
    }


    //privadoss
    private void givenUserAlreadyExists() {
        when(userJpaRepository.getByEmail(FIXED_EMAIL)).thenReturn(Optional.of(mockSavedUserEntity));
    }

    private void thenCheckIfUserExistsWasCalled(int times) {
        verify(userJpaRepository, times(times)).getByEmail(FIXED_EMAIL);
    }

    private void thenPasswordEncoderWasNeverCalled() {
        verify(passwordEncoder, never()).encode(anyString());
    }

    private void thenEmailWasNeverSent() {
        verify(gmailGateway, never()).sendEmail(anyString(), anyString(), anyString());
    }
}