package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.exceptions.PageLessThanZeroException;
import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.exceptions.UserAlreadyExistsException;
import com.outfitlab.project.domain.model.dto.LoginDTO;
import com.outfitlab.project.domain.useCases.user.LoginUser;
import com.outfitlab.project.domain.useCases.brand.GetAllBrands;
import com.outfitlab.project.domain.useCases.user.ActivateUser;
import com.outfitlab.project.domain.useCases.user.DesactivateUser;
import com.outfitlab.project.domain.useCases.user.GetAllUsers;
import com.outfitlab.project.domain.useCases.user.RegisterUser;
import com.outfitlab.project.domain.model.dto.RegisterDTO;
import com.outfitlab.project.presentation.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final RegisterUser registerUserUseCase;
    private final LoginUser loginUserUseCase;
    private final GetAllUsers getAllUsers;
    private final DesactivateUser desactivateUser;
    private final ActivateUser activateUser;

    public UserController(RegisterUser registerUserUseCase, LoginUser loginUserUseCase, GetAllUsers getAllUsers, DesactivateUser desactivateUser, ActivateUser activateUser) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
        this.getAllUsers = getAllUsers;
        this.desactivateUser = desactivateUser;
        this.activateUser = activateUser;
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

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        try {
            return ResponseEntity.ok(this.getAllUsers.execute());
        } catch (UserNotFoundException e) {
            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/desactivate")
    public ResponseEntity<?> desactivateUser(@RequestParam("email") String email) {
        try {
            return ResponseEntity.ok(this.desactivateUser.execute(email));
        } catch (UserNotFoundException e) {
            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/activate")
    public ResponseEntity<?> activateUser(@RequestParam("email") String email) {
        try {
            return ResponseEntity.ok(this.activateUser.execute(email));
        } catch (UserNotFoundException e) {
            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
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