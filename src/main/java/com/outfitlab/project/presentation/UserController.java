package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.entities.User;
import com.outfitlab.project.domain.exceptions.UserNotFound;
import com.outfitlab.project.infrastructure.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) throws UserNotFound {
        return this.userService.findUserById(id);
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFound ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("{\"error\": \"" + ex.getMessage() + "\"}");
    }

}
