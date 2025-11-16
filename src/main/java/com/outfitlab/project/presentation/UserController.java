package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.exceptions.UserAlreadyExistsException;
import com.outfitlab.project.domain.model.dto.LoginDTO;
import com.outfitlab.project.domain.useCases.user.LoginUser;
import com.outfitlab.project.domain.useCases.user.RegisterUser;
import com.outfitlab.project.domain.model.dto.RegisterDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final RegisterUser registerUserUseCase;
    private final LoginUser loginUserUseCase;

    public UserController(RegisterUser registerUserUseCase, LoginUser loginUserUseCase){
        this.registerUserUseCase = registerUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterDTO request) {

        try {
            UserModel newUser = registerUserUseCase.execute(request);

            Map<String, Object> response = new HashMap<>();
            response.put("email", newUser.getEmail());
            response.put("name", newUser.getName());
            response.put("message", "Registro exitoso. ¡Bienvenido a Outfit Lab!");

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (UserAlreadyExistsException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("email", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginDTO loginDTO) {

        try {
            return loginUserUseCase.execute(loginDTO);

        } catch (UserNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("email", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public UserModel getUser(@PathVariable int id) throws UserNotFoundException {
        return null;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("{\"error\": \"" + ex.getMessage() + "\"}");
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("{\"error\": \"" + ex.getMessage() + "\"}");
    }
}