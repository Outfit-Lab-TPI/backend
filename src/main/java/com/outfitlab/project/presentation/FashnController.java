package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.FashnApiException;
import com.outfitlab.project.domain.exceptions.PlanLimitExceededException;
import com.outfitlab.project.domain.exceptions.PredictionFailedException;
import com.outfitlab.project.domain.exceptions.PredictionTimeoutException;
import com.outfitlab.project.domain.exceptions.SubscriptionNotFoundException;
import com.outfitlab.project.domain.useCases.fashn.CombinePrendas;
import com.outfitlab.project.presentation.dto.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.outfitlab.project.domain.useCases.subscription.CheckUserPlanLimit;
import com.outfitlab.project.domain.useCases.subscription.IncrementUsageCounter;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/fashion")
public class FashnController {

    private final CombinePrendas combinePrendas;
    private final CheckUserPlanLimit checkUserPlanLimit;
    private final IncrementUsageCounter incrementUsageCounter;
    private final RestTemplate restTemplate;
    private static final Logger log = LoggerFactory.getLogger(FashnController.class);

    public FashnController(CombinePrendas combinePrendas,
            CheckUserPlanLimit checkUserPlanLimit,
            IncrementUsageCounter incrementUsageCounter,
            RestTemplate restTemplate) {
        this.combinePrendas = combinePrendas;
        this.checkUserPlanLimit = checkUserPlanLimit;
        this.incrementUsageCounter = incrementUsageCounter;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/combinar-prendas")
    public ResponseEntity<GeneratedResponse> combine(@RequestBody CombineRequest request) {
        System.out.println(request.toString());

        try {
            // Obtener email del usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();

            // Validar límite de combinaciones
            checkUserPlanLimit.execute(userEmail, "combinations");

            String result = this.combinePrendas.execute(CombineRequest.convertToDomainModel(request), userEmail);

            // Incrementar contador
            incrementUsageCounter.execute(userEmail, "combinations");

            return ResponseEntity.ok(new GeneratedResponse("OK", result));
        } catch (PlanLimitExceededException e) {
            return buildHttpResponse(e.getMessage(), "LIMIT_EXCEEDED", FORBIDDEN);
        } catch (SubscriptionNotFoundException e) {
            return buildHttpResponse(e.getMessage(), "SUBSCRIPTION_NOT_FOUND", NOT_FOUND);
        } catch (PredictionFailedException e) {
            return buildHttpResponse(e.getMessage(), "FAILED", BAD_GATEWAY);
        } catch (PredictionTimeoutException e) {
            return buildHttpResponse(e.getMessage(), "TIMEOUT", GATEWAY_TIMEOUT);
        } catch (FashnApiException e) {
            return buildHttpResponse(e.getMessage(), "ERROR", BAD_GATEWAY);
        } catch (Exception e) {
            return buildHttpResponse("Error inesperado: " + e.getMessage(), "ERROR", INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadImage(@RequestParam String imageUrl) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();

            // Validar límite de descargas
            checkUserPlanLimit.execute(userEmail, "downloads");

            // Descargar imagen
            byte[] imageBytes = restTemplate.getForObject(imageUrl, byte[].class);

            // Incrementar contador
            incrementUsageCounter.execute(userEmail, "downloads");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"combination.png\"")
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageBytes);

        } catch (PlanLimitExceededException e) {
            return buildHttpResponse(e.getMessage(), "LIMIT_EXCEEDED", FORBIDDEN);
        } catch (Exception e) {
            return buildHttpResponse("Error al descargar imagen: " + e.getMessage(), "ERROR", INTERNAL_SERVER_ERROR);
        }
    }

    @NotNull
    private static ResponseEntity<GeneratedResponse> buildHttpResponse(String message, String status,
            HttpStatus badGateway) {
        log.info(message);
        return ResponseEntity.status(badGateway).body(new GeneratedResponse(status, null, message));
    }
}
