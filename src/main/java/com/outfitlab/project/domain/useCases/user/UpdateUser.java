package com.outfitlab.project.domain.useCases.user;
import com.outfitlab.project.domain.exceptions.PasswordException;
import com.outfitlab.project.domain.exceptions.PasswordIsNotTheSame;
import com.outfitlab.project.domain.exceptions.UserAlreadyExistsException;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.UserModel;
import io.minio.S3Escaper;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UpdateUser {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UpdateUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserModel execute(String oldUserEmail, String name, String lastname, String newEmail, String password, String confirmPassword, String newImageUrl) {
        if(!oldUserEmail.equals(newEmail)) checkIfNewEmailAlreadyExists(newEmail);

        checkIfUserExists(oldUserEmail);
        String hashedPassword = checkPasswords(password,confirmPassword); //la hasheo y despues la paso para que le haga el update al user
        return this.userRepository.updateUser(oldUserEmail, name,lastname, newEmail, hashedPassword, newImageUrl);
    }

    private String checkPasswords(String password, String confirmPassword) {

        boolean passEmpty = (password == null || password.isBlank());
        boolean confirmEmpty = (confirmPassword == null || confirmPassword.isBlank());

        if (passEmpty && confirmEmpty) return "";
        if (passEmpty != confirmEmpty) throw new PasswordException("Debe enviar la contraseña y su confirmación para actualizarla.");
        if (!password.equals(confirmPassword)) throw new PasswordIsNotTheSame("Las contraseñas no coinciden.");

        return passwordEncoder.encode(password);
    }

    private void checkIfNewEmailAlreadyExists(String newEmail) {
        try{
            this.userRepository.findUserByEmail(newEmail);
            throw new UserAlreadyExistsException("No es posible utilizar el correo: '" + newEmail + "'. Por favor, eliija otro.");
        }catch (UserNotFoundException e){}
    }

    private void checkIfUserExists(String email) {
        this.userRepository.findUserByEmail(email);
    }
}
