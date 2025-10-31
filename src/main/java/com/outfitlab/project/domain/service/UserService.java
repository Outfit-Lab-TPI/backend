package com.outfitlab.project.domain.service;

import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.domain.exceptions.UserNotFound;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public UserEntity findUserById(int id) throws UserNotFound {
        if(id == 1){
            return new UserEntity(1, "Julian");
        }
        throw new UserNotFound("No encontramos usuarios!");
    }
}
