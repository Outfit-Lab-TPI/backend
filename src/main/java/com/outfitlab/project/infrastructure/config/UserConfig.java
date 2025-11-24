package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.gateways.GmailGateway;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.useCases.user.*;
import com.outfitlab.project.infrastructure.config.security.jwt.JwtService;
import com.outfitlab.project.infrastructure.repositories.UserRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.interfaces.BrandJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.TokenRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserConfig {

    @Value("${app.base-url}")
    private String baseUrl;

    @Bean
    public UserRepository userRepository(UserJpaRepository userJpaRepository, BrandJpaRepository brandJpaRepository) {
        return new UserRepositoryImpl(userJpaRepository, brandJpaRepository);
    }

    // ← Use cases de develop
    @Bean
    public GetUserByEmail getUserByEmail(UserRepository userRepository) {
        return new GetUserByEmail(userRepository);
    }

    @Bean
    public UpdateUser updateUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new UpdateUser(userRepository, passwordEncoder);
    }

    @Bean
    public GetAllUsers getAllUsers(UserRepository userRepository) {
        return new GetAllUsers(userRepository);
    }

    @Bean
    public UpdateBrandUser updateBrandUser(UserRepository userRepository) {
        return new UpdateBrandUser(userRepository);
    }

    @Bean
    public DesactivateUser desactivateUser(UserRepository userRepository) {
        return new DesactivateUser(userRepository);
    }

    @Bean
    public ConvertToAdmin convertToAdmin(UserRepository userRepository) {
        return new ConvertToAdmin(userRepository);
    }

    @Bean
    public ConvertToUser convertToUser(UserRepository userRepository) {
        return new ConvertToUser(userRepository);
    }

    @Bean
    public ActivateUser activateUser(UserRepository userRepository) {
        return new ActivateUser(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ← RegisterUser con baseUrl de refactor
    @Bean
    public RegisterUser registerUser(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            TokenRepository tokenRepository, JwtService jwtService, UserJpaRepository userJpaRepository,
            GmailGateway gmailGateway) {
        return new RegisterUser(userRepository, passwordEncoder, authenticationManager, tokenRepository, jwtService,
                userJpaRepository, gmailGateway, baseUrl);
    }

    @Bean
    LoginUser loginUser(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            TokenRepository tokenRepository, JwtService jwtService, UserJpaRepository userJpaRepository) {
        return new LoginUser(userRepository, passwordEncoder, authenticationManager, tokenRepository, jwtService,
                userJpaRepository);
    }

    @Bean
    UserProfile userProfile(UserJpaRepository userJpaRepository) {
        return new UserProfile(userJpaRepository);
    }

    @Bean
    public VerifyEmail verifyEmail(UserRepository userRepository, UserJpaRepository userJpaRepository) {
        return new VerifyEmail(userRepository, userJpaRepository);
    }

    @Bean
    public RefreshToken refreshToken(JwtService jwtService, UserJpaRepository userJpaRepository,
            TokenRepository tokenRepository) {
        return new RefreshToken(jwtService, userJpaRepository, tokenRepository);
    }
}
