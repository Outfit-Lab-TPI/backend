package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.model.UserModel;

public interface UserRepository {
    UserModel findUserByEmail(String userEmail) throws UserNotFoundException;

    UserModel saveUser(UserModel userModel);
}
