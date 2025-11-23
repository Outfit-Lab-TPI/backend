package com.outfitlab.project.infrastructure.config.security;

import com.outfitlab.project.infrastructure.config.security.jwt.JwtService;
import com.outfitlab.project.infrastructure.repositories.interfaces.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String userUsername;
        final String jwtToken;
        if (
                request.getServletPath().equals("/api/users/login")
                        || request.getServletPath().equals("/api/users/register")
                        || request.getServletPath().startsWith("/api/users/verify")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = jwtService.extractToken(request);
        if(jwtToken == null){
            filterChain.doFilter(request, response);
            return;
        }

        userUsername = jwtService.extractUsername(jwtToken);
        if(userUsername != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(userUsername);

            var isTokenValid = tokenRepository.findByToken(jwtToken)
                    .map(token -> !token.getExpired() && !token.getRevoked())
                    .orElse(false);

            if(isTokenValid && jwtService.isTokenValid(jwtToken, userDetails) && !jwtService.isTokenExpired(jwtToken)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
