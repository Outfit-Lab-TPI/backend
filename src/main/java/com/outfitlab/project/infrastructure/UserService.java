package com.outfitlab.project.infrastructure;

import com.outfitlab.project.domain.entities.User;
import com.outfitlab.project.domain.exceptions.UserNotFound;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public User findUserById(int id) throws UserNotFound {
        if(id == 1){
            return new User(1, "Julian");
        }
        throw new UserNotFound("No encontramos usuarios!");
    }
}
