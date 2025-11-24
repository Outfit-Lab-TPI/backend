package com.outfitlab.project.presentation;
import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.model.CombinationModel;
import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.useCases.combination.CreateCombination;
import com.outfitlab.project.domain.useCases.combination.GetCombinationByPrendas;
import com.outfitlab.project.domain.useCases.combinationAttempt.RegisterCombinationAttempt;
import com.outfitlab.project.domain.useCases.combinationFavorite.AddCombinationToFavourite;
import com.outfitlab.project.domain.useCases.combinationFavorite.DeleteCombinationFromFavorite;
import com.outfitlab.project.domain.useCases.combinationFavorite.GetCombinationFavoritesForUserByEmail;
import com.outfitlab.project.domain.useCases.garment.GetGarmentByCode;
import com.outfitlab.project.presentation.dto.RegisterAttemptRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/combinations")
public class CombinationController {

    private final AddCombinationToFavourite addCombinationToFavourite;
    private final DeleteCombinationFromFavorite deleteCombinationFromFavorite;
    private final GetCombinationFavoritesForUserByEmail getCombinationFavoritesForUserByEmail;
    private final GetGarmentByCode getGarmentByCode;
    private final GetCombinationByPrendas getCombinationByPrendas;
    private final CreateCombination createCombination;
    private final RegisterCombinationAttempt registerCombinationAttempt;

    public CombinationController(AddCombinationToFavourite addCombinationToFavourite,
                                 DeleteCombinationFromFavorite deleteCombinationFromFavorite,
                                 GetCombinationFavoritesForUserByEmail getCombinationFavoritesForUserByEmail,
                                 GetGarmentByCode getGarmentByCode,
                                 GetCombinationByPrendas getCombinationByPrendas,
                                 CreateCombination createCombination,
                                 RegisterCombinationAttempt registerCombinationAttempt
    ){
        this.addCombinationToFavourite = addCombinationToFavourite;
        this.deleteCombinationFromFavorite = deleteCombinationFromFavorite;
        this.getCombinationFavoritesForUserByEmail = getCombinationFavoritesForUserByEmail;
        this.getGarmentByCode = getGarmentByCode;
        this.getCombinationByPrendas = getCombinationByPrendas;
        this.createCombination = createCombination;
        this.registerCombinationAttempt = registerCombinationAttempt;
    }

    @PostMapping("/attempt/register")
    public ResponseEntity<?> registerAttempt(
            @RequestBody RegisterAttemptRequest req
    ) {
        try {
            PrendaModel sup = getGarmentByCode.execute(req.prendaSupCode());
            PrendaModel inf = getGarmentByCode.execute(req.prendaInfCode());

            CombinationModel combination;
            try {
                combination = getCombinationByPrendas.execute(sup.getId(), inf.getId());
            } catch (CombinationNotFoundException e) {
                combination = createCombination.execute(sup, inf);
            }

            var attemptId = registerCombinationAttempt.execute(
                    req.userEmail(),
                    combination,
                    req.imageUrl()
            );

            return ResponseEntity.ok("Combinacion registrada con ID: " + attemptId);
        } catch (GarmentNotFoundException e) {
            return buildResponseEntityError(e.getMessage());
        }
    }

    @GetMapping("/favorite/add")
    public ResponseEntity<?> addCombinationToFavorite(@RequestParam String combinationUrl) {
        ResponseEntity<?> body = validateInputs(combinationUrl);
        if (body != null)
            return body;

        try {
            // Obtener email del usuario autenticado desde el JWT token
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();

            System.out.println(
                    "🔍 DEBUG addCombinationToFavorite - Email autenticado: " + userEmail + ", URL: " + combinationUrl);

            return ResponseEntity.ok(this.addCombinationToFavourite.execute(combinationUrl, userEmail));
        } catch (PlanLimitExceededException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("limitType", e.getLimitType());
            errorResponse.put("currentUsage", e.getCurrentUsage());
            errorResponse.put("maxAllowed", e.getMaxAllowed());
            errorResponse.put("upgradeRequired", true);
            return ResponseEntity.status(403).body(errorResponse);
        } catch (UserNotFoundException | FavoritesException | UserCombinationFavoriteAlreadyExistsException
                | SubscriptionNotFoundException e) {
            return buildResponseEntityError(e.getMessage());
        }
    }

    @GetMapping("/favorite/delete")
    public ResponseEntity<?> deleteCombinationFromFavorite(@RequestParam String combinationUrl) {
        ResponseEntity<?> body = validateInputs(combinationUrl);
        if (body != null)
            return body;

        try {
            // Obtener email del usuario autenticado desde el JWT token
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();

            return ResponseEntity.ok(this.deleteCombinationFromFavorite.execute(combinationUrl, userEmail));
        } catch (UserCombinationFavoriteNotFoundException | UserNotFoundException e) {
            return buildResponseEntityError(e.getMessage());
        }
    }

    @GetMapping("/favorite")
    public ResponseEntity<?> getFavorites(@RequestParam(defaultValue = "0") int page) {
        try {
            // Obtener email del usuario autenticado desde el JWT token
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();

            return ResponseEntity.ok(this.getCombinationFavoritesForUserByEmail.execute(userEmail, page));
        } catch (UserNotFoundException | PageLessThanZeroException e) {
            return buildResponseEntityError(e.getMessage());
        } catch (FavoritesException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    private ResponseEntity<?> validateInputs(String combinationUrl) {
        if (combinationUrl == null || combinationUrl.trim().isEmpty()) {
            return buildResponseEntityError("El parámetro: " + combinationUrl + " no puede estar vacío.");
        }
        return null;
    }

    private ResponseEntity<?> buildResponseEntityError(String message) {
        return ResponseEntity
                .status(404)
                .body(message);
    }
}
