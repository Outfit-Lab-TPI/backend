package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import com.outfitlab.project.presentation.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class UserProfile {
    private final UserJpaRepository userJpaRepository;
    public UserModel execute(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("No se encontro a un usuario autenticado.");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserEntity user = userJpaRepository.getByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado."));
        return UserEntity.convertEntityToModel(user);

    }
}
