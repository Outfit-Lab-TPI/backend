package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.infrastructure.repositories.UserRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Bean
    public UserRepository userRepository(UserJpaRepository userJpaRepository) {
        return new UserRepositoryImpl(userJpaRepository);
    }
}
