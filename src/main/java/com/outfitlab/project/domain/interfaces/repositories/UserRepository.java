package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.model.UserModel;

public interface UserRepository {
    // Método legacy existente
    UserModel findUserByEmail(String userEmail) throws UserNotFoundException;
    
    // Nuevos métodos para suscripciones
    UserModel findByEmail(String email) throws UserNotFoundException;
    UserModel findById(Long id);
    UserModel save(UserModel user);
}
