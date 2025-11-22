package com.outfitlab.project.domain.useCases.user;
import com.outfitlab.project.domain.exceptions.PasswordIsNotTheSame;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;

public class UpdateUser {
    private final UserRepository userRepository;

    public UpdateUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(String name, String lastname, String email, String password, String confirmPassword, String newImageUrl) {
        checkIfUserExists(email);
        checkPasswords(password,confirmPassword);
        this.userRepository.updateUser(name,lastname, email, password, confirmPassword, newImageUrl);
    }

    private void checkPasswords(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) throw new PasswordIsNotTheSame("Las contraseñas no coinciden.");
    }

    private void checkIfUserExists(String email) {
        if (this.userRepository.findUserByEmail(email) == null) throw new UserNotFoundException("No encontramos el user:" + email);
    }
}
