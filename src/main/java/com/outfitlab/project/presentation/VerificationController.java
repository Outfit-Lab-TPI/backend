package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.useCases.user.VerifyEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/users")
public class VerificationController {

    private final VerifyEmail verifyEmailUseCase;

    @Value("${app.frontend.login-url}")
    private String frontendLoginUrl;

    public VerificationController(VerifyEmail verifyEmailUseCase) {
        this.verifyEmailUseCase = verifyEmailUseCase;
    }

    @GetMapping("/verify")
    public String verifyUser(@RequestParam("token") String token) {
        try {
            verifyEmailUseCase.execute(token);
            return "redirect:" + frontendLoginUrl + "?verification=sucess";
        } catch (UserNotFoundException e) {
            return "redirect:" + frontendLoginUrl + "?verification=error&message=Token inválido o expirado.";
        }
    }

}
