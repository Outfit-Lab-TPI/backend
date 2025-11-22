package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.model.UserModel;
import jakarta.transaction.Transactional;

import java.util.List;

public interface UserRepository {
    UserModel findUserByEmail(String userEmail) throws UserNotFoundException;

    UserModel findUserByVerificationToken(String token) throws UserNotFoundException;

    UserModel saveUser(UserModel userModel);

    List<UserModel> findAll();

    void desactivateUser(String email);

    void activateUser(String email);

    void convertToAdmin(String email);

    void convertToUser(String email);

    void updateBrandUser(String userEmail, String brandCode);

    String getEmailUserRelatedToBrandByBrandCode(String brandCode);

    @Transactional
    void updateUser(String name, String lastname, String email, String password, String confirmPassword, String newImageUrl);
}
