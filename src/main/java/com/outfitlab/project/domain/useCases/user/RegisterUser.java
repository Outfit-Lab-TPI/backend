package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.model.dto.RegisterDTO;
import com.outfitlab.project.domain.exceptions.UserAlreadyExistsException;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.UserModel;
import org.springframework.security.crypto.password.PasswordEncoder;

public class RegisterUser {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserModel execute(RegisterDTO request) throws UserAlreadyExistsException {

        checkIfUserExists(request.getEmail());

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        UserModel newUserModel = new UserModel(
                request.getEmail(),
                request.getName(),
                request.getLastName(),
                hashedPassword
        );

        return userRepository.saveUser(newUserModel);
    }

    private void checkIfUserExists(String email) throws UserAlreadyExistsException {
        try {
            userRepository.findUserByEmail(email);
            throw new UserAlreadyExistsException("El email ya está registrado.");
        } catch (UserNotFoundException e) {
        }
    }
}
