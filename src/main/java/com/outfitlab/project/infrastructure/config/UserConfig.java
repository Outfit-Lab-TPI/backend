package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.useCases.user.DesactivateUser;
import com.outfitlab.project.domain.useCases.user.GetAllUsers;
import com.outfitlab.project.domain.useCases.user.RegisterUser;
import com.outfitlab.project.infrastructure.repositories.UserRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RegisterUser registerUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new RegisterUser(userRepository, passwordEncoder);
    }
}
