package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.useCases.user.*;
import com.outfitlab.project.infrastructure.config.security.jwt.JwtService;
import com.outfitlab.project.infrastructure.repositories.UserRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.interfaces.TokenRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserConfig {

    @Bean
    public UserRepository userRepository(UserJpaRepository userJpaRepository) {
        return new UserRepositoryImpl(userJpaRepository);
    }

    @Bean
    public GetAllUsers getAllUsers(UserRepository userRepository) {
        return new GetAllUsers(userRepository);
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

    @Bean
    public RegisterUser registerUser(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                                     TokenRepository tokenRepository, JwtService jwtService, UserJpaRepository userJpaRepository) {
        return new RegisterUser(userRepository, passwordEncoder, authenticationManager, tokenRepository, jwtService, userJpaRepository);
    }
}
