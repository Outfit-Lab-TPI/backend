package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.exceptions.NullFieldsException;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.dto.LoginDTO;
import com.outfitlab.project.infrastructure.config.security.AuthResponse;
import com.outfitlab.project.infrastructure.config.security.jwt.JwtService;
import com.outfitlab.project.infrastructure.config.security.jwt.Token;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.TokenRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class LoginUser {
    private final UserRepository userRepository;
    private final UserJpaRepository userJpaRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;

    public LoginUser(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authManager,
                        TokenRepository tokenRepository, JwtService jwtService, UserJpaRepository userJpaRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.tokenRepository = tokenRepository;
        this.jwtService =jwtService;
        this.userJpaRepository = userJpaRepository;
    }
    public ResponseEntity<AuthResponse> execute(LoginDTO loginDTO){
        if(loginDTO.getEmail().isBlank() || loginDTO.getPassword().isBlank()){
            throw new NullFieldsException("Debe completar la totalidad de los campos para autenticarse.");
        }
        try{
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(),
                            loginDTO.getPassword()
                    )
            );
        } catch (AuthenticationException | UserNotFoundException ex) {
            throw new UserNotFoundException("Email o contraseña incorrecta. Vuelva a intentarlo.");
        }

        UserEntity userEntity = userJpaRepository.findByEmail(loginDTO.getEmail());
        if (!userEntity.isVerified()){
            throw new UserNotFoundException("La cuenta no ha sido verificada. Revisa tu correo electrónico.");
        }

        var user = userJpaRepository.getByEmail(loginDTO.getEmail())
                .orElseThrow();
        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);

        return new ResponseEntity<>(
                AuthResponse.builder()
                        .access_token(accessToken)
                        .refresh_token(refreshToken)
                        .build(),
                HttpStatus.OK
        );
    }

    private void saveUserToken(UserEntity user, String token){
        var saveToken = Token.builder()
                .token(token)
                .user(user)
                .revoked(false)
                .expired(false)
                .build();

        tokenRepository.save(saveToken);
    }

    private void revokeAllUserTokens(UserEntity user){
        var validTokens = tokenRepository.allValidTokensByUser(user.getId());

        if(validTokens.isEmpty()){
            return;
        }

        validTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });

        tokenRepository.saveAll(validTokens);
    }
}
