package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.model.UserModel;

import java.util.List;

public interface UserRepository {
    UserModel findUserByEmail(String userEmail) throws UserNotFoundException;

    UserModel saveUser(UserModel userModel);

    List<UserModel> findAll();

    void desactivateUser(String email);

    void activateUser(String email);

    void convertToAdmin(String email);

    void convertToUser(String email);

    void updateBrandUser(String userEmail, String brandCode);
}
