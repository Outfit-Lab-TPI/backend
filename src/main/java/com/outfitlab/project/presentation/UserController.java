package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.exceptions.UserAlreadyExistsException;
import com.outfitlab.project.domain.model.dto.LoginDTO;
import com.outfitlab.project.domain.useCases.brand.CreateBrand;
import com.outfitlab.project.domain.useCases.bucketImages.SaveImage;
import com.outfitlab.project.domain.useCases.subscription.AssignFreePlanToUser;
import com.outfitlab.project.domain.useCases.user.*;
import com.outfitlab.project.domain.model.dto.RegisterDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final RegisterUser registerUserUseCase;
    private final LoginUser loginUserUseCase;
    private final GetAllUsers getAllUsers;
    private final DesactivateUser desactivateUser;
    private final ActivateUser activateUser;
    private final ConvertToAdmin convertToAdmin;
    private final ConvertToUser convertToUser;
    private final CreateBrand createBrand;
    private final UpdateBrandUser updateBrandUser;
    private final SaveImage saveImage;
    private final AssignFreePlanToUser assignFreePlanToUser;

    public UserController(RegisterUser registerUserUseCase, LoginUser loginUserUseCase, GetAllUsers getAllUsers,
            DesactivateUser desactivateUser,
            ActivateUser activateUser, ConvertToAdmin convertToAdmin, ConvertToUser convertToUser,
            CreateBrand createBrand, UpdateBrandUser updateBrandUser, SaveImage saveImage,
            AssignFreePlanToUser assignFreePlanToUser) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
        this.getAllUsers = getAllUsers;
        this.desactivateUser = desactivateUser;
        this.activateUser = activateUser;
        this.convertToAdmin = convertToAdmin;
        this.convertToUser = convertToUser;
        this.createBrand = createBrand;
        this.updateBrandUser = updateBrandUser;
        this.saveImage = saveImage;
        this.assignFreePlanToUser = assignFreePlanToUser;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterDTO request) {

        try {
            // 1. Registrar usuario
            UserModel newUser = registerUserUseCase.execute(request);

            // 2. Asignar plan gratuito
            assignFreePlanToUser.execute(newUser.getEmail());

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

    @PostMapping(value = "/register-brand", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerbrandAndUser(@Valid @ModelAttribute RegisterDTO request) {

        try {
            // 1. Registrar usuario
            UserModel newUser = registerUserUseCase.execute(request);

            // 2. Asignar plan gratuito
            assignFreePlanToUser.execute(newUser.getEmail());

            // 3. Crear marca y asociarla al usuario
            String brandCode = createAndReturnBrand(
                    request.getBrandName(),
                    saveImageAndGetUrl(request.getLogoBrand(), "brand_logo_images"),
                    request.getUrlSite());

            updateBrandInUser(request.getEmail(), brandCode);

            Map<String, Object> response = new HashMap<>();
            response.put("email", newUser.getEmail());
            response.put("name", newUser.getName());

            response.put("message", "Registro exitoso. ¡Un administrador revisará tu marca!");
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
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
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

    @PutMapping("/convert-to-admin/{email}")
    public ResponseEntity<?> convertToAdmin(@PathVariable String email) {
        try {
            return ResponseEntity.ok(this.convertToAdmin.execute(email));
        } catch (UserNotFoundException e) {
            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/convert-to-user/{email}")
    public ResponseEntity<?> convertToUser(@PathVariable String email) {
        try {
            return ResponseEntity.ok(this.convertToUser.execute(email));
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

    private void updateBrandInUser(String email, String brandCode) {
        this.updateBrandUser.execute(email, brandCode);
    }

    private String createAndReturnBrand(String brandName, String logoUrl, String urlStie) {
        return this.createBrand.execute(brandName, logoUrl, urlStie);
    }

    private String saveImageAndGetUrl(MultipartFile image, String folder) {
        return this.saveImage.execute(image, folder);
    }
}