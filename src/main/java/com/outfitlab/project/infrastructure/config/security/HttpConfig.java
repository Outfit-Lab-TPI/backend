package com.outfitlab.project.infrastructure.config.security;

import lombok.RequiredArgsConstructor;
import okhttp3.internal.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class HttpConfig{

    private final AuthenticationProvider authenticationProvider;
    private final SecurityAuthFilter securityAuthFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        return http.cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                /*.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/**").permitAll() // modificable cuanddo tengamos implementada autenticacion via jwt
                        .anyRequest()
                        .authenticated()
                )*/
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/api/users/register", "/api/users/login/**", "/api/users/verify/**", "/login")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(securityAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
