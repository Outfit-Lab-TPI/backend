package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.model.dto.RegisterDTO;
import com.outfitlab.project.domain.exceptions.UserAlreadyExistsException;
import com.outfitlab.project.domain.interfaces.gateways.GmailGateway;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.domain.model.Role;
import com.outfitlab.project.infrastructure.config.security.jwt.JwtService;
import com.outfitlab.project.infrastructure.config.security.jwt.Token;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.TokenRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.UUID;

public class RegisterUser {

    private final UserRepository userRepository;
    private final UserJpaRepository userJpaRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final GmailGateway gmailGateway;
    private final String baseUrl;

    public RegisterUser(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authManager,
            TokenRepository tokenRepository, JwtService jwtService, UserJpaRepository userJpaRepository,
            GmailGateway gmailGateway, String baseUrl) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
        this.userJpaRepository = userJpaRepository;
        this.gmailGateway = gmailGateway;
        this.baseUrl = baseUrl;
    }

    public UserModel execute(RegisterDTO request) throws UserAlreadyExistsException {

        checkIfUserExists(request.getEmail());

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        String verificationToken = UUID.randomUUID().toString();

        String verificationLink = baseUrl + "/api/users/verify?token=" + verificationToken;

        UserEntity userEntity = new UserEntity(
                request.getName(),
                request.getLastName(),
                request.getEmail(),
                null, null, null,
                hashedPassword);
        userEntity.setRole(Role.USER);
        userEntity.setVerified(false);
        userEntity.setVerificationToken(verificationToken);

        var savedUser = userJpaRepository.save(userEntity);

        String emailBody = "<h1>¡Bienvenido a Outfit Lab!</h1>"
                + "<p>Haz click en el enlace para verificar tu cuenta:</p>"
                + "<a href=\"" + verificationLink + "\">Verificar cuenta </a>";

        gmailGateway.sendEmail(request.getEmail(), "Verificación de cuenta de Outfit Lab", emailBody);

        UserModel newUserModel = new UserModel(
                request.getEmail(),
                request.getName(),
                request.getLastName(),
                hashedPassword,
                verificationToken);

        var accessToken = jwtService.generateToken(userEntity);
        var refreshToken = jwtService.generateRefreshToken(userEntity);
        saveUserToken(savedUser, accessToken);

        return newUserModel;
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

    private void checkIfUserExists(String email) throws UserAlreadyExistsException {
        var userSaved = userJpaRepository.getByEmail(email);
        if (userSaved.isPresent()) {
            throw new UserAlreadyExistsException("El email ya está registrado.");
        }
    }
}
