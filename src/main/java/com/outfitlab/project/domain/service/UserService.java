package com.outfitlab.project.domain.service;

import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    public UserModel findUserById(int id) throws UserNotFoundException {
        if(id == 1){
            return new UserModel("Julian", "Schmuker", "julian@gmail.com", "Mr.", "Gabriel", 21, "123456", LocalDateTime.now(), LocalDateTime.now());
        }
        throw new UserNotFoundException("No encontramos usuarios!");
    }
}
