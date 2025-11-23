package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.infrastructure.config.security.AuthResponse;
import com.outfitlab.project.infrastructure.config.security.jwt.JwtService;
import com.outfitlab.project.infrastructure.config.security.jwt.Token;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.TokenRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RefreshToken {
    private final JwtService jwtService;
    private final UserJpaRepository userJpaRepository;
    private final TokenRepository tokenRepository;

    public RefreshToken(JwtService jwtService, UserJpaRepository userJpaRepository, TokenRepository tokenRepository) {
        this.jwtService = jwtService;
        this.userJpaRepository = userJpaRepository;
        this.tokenRepository = tokenRepository;
    }

    public ResponseEntity<AuthResponse> execute(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new UserNotFoundException("Refresh token no proporcionado.");
        }

        // Verificar si el refresh token está expirado
        if (jwtService.isTokenExpired(refreshToken)) {
            throw new UserNotFoundException("El refresh token ha expirado. Por favor, inicie sesión nuevamente.");
        }

        // Extraer el email del usuario del refresh token
        String userEmail = jwtService.extractUsername(refreshToken);

        // Buscar el usuario
        var user = userJpaRepository.getByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado."));

        // Validar que el token sea válido para este usuario
        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new UserNotFoundException("Refresh token no válido.");
        }

        // Generar nuevos tokens
        var newAccessToken = jwtService.generateToken(user);
        var newRefreshToken = jwtService.generateRefreshToken(user);

        // Revocar todos los tokens anteriores del usuario
        revokeAllUserTokens(user);

        // Guardar el nuevo access token
        saveUserToken(user, newAccessToken);

        return new ResponseEntity<>(
                AuthResponse.builder()
                        .access_token(newAccessToken)
                        .refresh_token(newRefreshToken)
                        .user(UserEntity.convertEntityToModel(user))
                        .build(),
                HttpStatus.OK);
    }

    private void saveUserToken(UserEntity user, String token) {
        var saveToken = Token.builder()
                .token(token)
                .user(user)
                .revoked(false)
                .expired(false)
                .build();

        tokenRepository.save(saveToken);
    }

    private void revokeAllUserTokens(UserEntity user) {
        var validTokens = tokenRepository.allValidTokensByUser(user.getId());

        if (validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });

        tokenRepository.saveAll(validTokens);
    }
}
