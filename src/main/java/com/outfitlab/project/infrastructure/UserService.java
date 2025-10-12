package com.outfitlab.project.infrastructure;

import com.outfitlab.project.domain.models.UserModel;
import com.outfitlab.project.domain.exceptions.UserNotFound;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public UserModel findUserById(int id) throws UserNotFound {
        if(id == 1){
            return new UserModel(1, "Julian");
        }
        throw new UserNotFound("No encontramos usuarios!");
    }
}
