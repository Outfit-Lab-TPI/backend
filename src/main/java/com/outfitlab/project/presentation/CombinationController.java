package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.useCases.combination.AddCombinationToFavourite;
import com.outfitlab.project.domain.useCases.combination.DeleteCombinationFromFavorite;
import com.outfitlab.project.domain.useCases.combination.GetCombinationFavoritesForUserByEmail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/combinations")
public class CombinationController {


    private final AddCombinationToFavourite addCombinationToFavourite;
    private final DeleteCombinationFromFavorite deleteCombinationFromFavorite;
    private final GetCombinationFavoritesForUserByEmail getCombinationFavoritesForUserByEmail;

    public CombinationController(AddCombinationToFavourite addCombinationToFavourite, DeleteCombinationFromFavorite deleteCombinationFromFavorite, GetCombinationFavoritesForUserByEmail getCombinationFavoritesForUserByEmail){
        this.addCombinationToFavourite = addCombinationToFavourite;
        this.deleteCombinationFromFavorite = deleteCombinationFromFavorite;
        this.getCombinationFavoritesForUserByEmail = getCombinationFavoritesForUserByEmail;
    }

    @GetMapping("/favorite/add")
    public ResponseEntity<?> addCombinationToFavorite(@RequestParam String combinationUrl){
        ResponseEntity<?> body = validateInputs(combinationUrl);
        if (body != null) return body;

        try {
            String userEmail = "german@gmail.com"; //acá hay que obtenerlo de la session, NO recibirlo por parámetro sino obtenerlo por session, ahora dejo esto pq no tenemos CRUD de user.
            return ResponseEntity.ok(this.addCombinationToFavourite.execute(combinationUrl, userEmail));
        } catch (PlanLimitExceededException e) {
            // Manejo específico para límites de plan
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("limitType", e.getLimitType());
            errorResponse.put("currentUsage", e.getCurrentUsage());
            errorResponse.put("maxAllowed", e.getMaxAllowed());
            errorResponse.put("upgradeRequired", true);
            return ResponseEntity.status(403).body(errorResponse);
        } catch (UserNotFoundException | FavoritesException | UserCombinationFavoriteAlreadyExistsException | SubscriptionNotFoundException e) {
            return buildResponseEntityError(e.getMessage());
        }
    }


    @GetMapping("/favorite/delete")
    public ResponseEntity<?> deleteCombinationFromFavorite(@RequestParam String combinationUrl){
        ResponseEntity<?> body = validateInputs(combinationUrl);
        if (body != null) return body;

        try {
            String userEmail = "german@gmail.com"; //acá hay que obtenerlo de la session, NO recibirlo por parámetro sino obtenerlo por session, ahora dejo esto pq no tenemos CRUD de user.
            return ResponseEntity.ok(this.deleteCombinationFromFavorite.execute(combinationUrl, userEmail));
        } catch (UserCombinationFavoriteNotFoundException | UserNotFoundException e) {
            return buildResponseEntityError(e.getMessage());
        }
    }


    @GetMapping("/favorite")
    public ResponseEntity<?> getFavorites(@RequestParam(defaultValue = "0") int page){
        try {
            String userEmail = "german@gmail.com"; //acá hay que obtenerlo de la session, NO recibirlo por parámetro sino obtenerlo por session, ahora dejo esto pq no tenemos CRUD de user.
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

    private ResponseEntity<?> buildResponseEntityError(String message){
        return ResponseEntity
                .status(404)
                .body(message);
    }
}
