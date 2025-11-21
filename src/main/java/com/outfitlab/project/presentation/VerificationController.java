package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.useCases.user.VerifyEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/users")
public class VerificationController {

    private final VerifyEmail verifyEmailUseCase;

    @Value("${app.frontend.login-url}")
    private String frontendLoginUrl;

    public VerificationController(VerifyEmail verifyEmailUseCase) {
        this.verifyEmailUseCase = verifyEmailUseCase;
    }

    @GetMapping("/verify")
    public ResponseEntity<Void> verifyUser(@RequestParam("token") String token) throws URISyntaxException {

        final String successMessage = "?verification=success";
        final String errorMessage = "?verification=error&message=";

        URI location;

        try {
            verifyEmailUseCase.execute(token);

            location = new URI(frontendLoginUrl + successMessage);

        } catch (UserNotFoundException e) {
            String userFriendlyMessage = "Token inválido o expirado.";
            String encodedMessage = URLEncoder.encode(userFriendlyMessage, StandardCharsets.UTF_8);

            location = new URI(frontendLoginUrl + errorMessage + encodedMessage);

        } catch (Exception e) {
            String userFriendlyMessage = "Error interno del servidor durante la verificación.";
            String encodedMessage = URLEncoder.encode(userFriendlyMessage, StandardCharsets.UTF_8);

            location = new URI(frontendLoginUrl + errorMessage + encodedMessage);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}