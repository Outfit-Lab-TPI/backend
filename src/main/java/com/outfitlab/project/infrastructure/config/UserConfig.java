package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.useCases.user.RegisterUser;
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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RegisterUser registerUser(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                                     TokenRepository tokenRepository, JwtService jwtService, UserJpaRepository userJpaRepository) {
        return new RegisterUser(userRepository, passwordEncoder, authenticationManager, tokenRepository, jwtService, userJpaRepository);
    }
}
