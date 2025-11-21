package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.model.dto.RegisterDTO;
import com.outfitlab.project.domain.exceptions.UserAlreadyExistsException;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.domain.useCases.subscription.AssignFreePlanToUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

public class RegisterUser {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AssignFreePlanToUser assignFreePlanToUser;

    public RegisterUser(UserRepository userRepository, 
                       PasswordEncoder passwordEncoder,
                       AssignFreePlanToUser assignFreePlanToUser) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.assignFreePlanToUser = assignFreePlanToUser;
    }

    @Transactional
    public UserModel execute(RegisterDTO request) throws UserAlreadyExistsException {

        checkIfUserExists(request.getEmail());

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        UserModel newUserModel = new UserModel(
                request.getEmail(),
                request.getName(),
                request.getLastName(),
                hashedPassword
        );

        UserModel savedUser = userRepository.saveUser(newUserModel);
        
        // Asignar plan FREE automáticamente
        assignFreePlanToUser.execute(savedUser.getEmail());

        return savedUser;
    }

    private void checkIfUserExists(String email) throws UserAlreadyExistsException {
        try {
            userRepository.findUserByEmail(email);
            throw new UserAlreadyExistsException("El email ya está registrado.");
        } catch (UserNotFoundException e) {
        }
    }
}
