package com.outfitlab.project.domain.service;

import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.domain.exceptions.UserNotFound;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public UserModel findUserById(int id) throws UserNotFound {
        if(id == 1){
            return new UserModel("Julian", "Schmuker", "julian@gmail.com", "Mr.", "Gabriel", 21.0);
        }
        throw new UserNotFound("No encontramos usuarios!");
    }
}
