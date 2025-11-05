package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.useCases.combination.AddCombinationToFavourite;
import com.outfitlab.project.domain.useCases.combination.DeleteCombinationFromFavorite;
import com.outfitlab.project.domain.useCases.combination.GetCombinationFavoritesForUserByEmail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cambinations")
public class CombinationController {


    private final AddCombinationToFavourite addCombinationToFavourite;
    private final DeleteCombinationFromFavorite deleteCombinationFromFavorite;
    private final GetCombinationFavoritesForUserByEmail getCombinationFavoritesForUserByEmail;

    public CombinationController(AddCombinationToFavourite addCombinationToFavourite, DeleteCombinationFromFavorite deleteCombinationFromFavorite, GetCombinationFavoritesForUserByEmail getCombinationFavoritesForUserByEmail){
        this.addCombinationToFavourite = addCombinationToFavourite;
        this.deleteCombinationFromFavorite = deleteCombinationFromFavorite;
        this.getCombinationFavoritesForUserByEmail = getCombinationFavoritesForUserByEmail;
    }

    @GetMapping("/favorite/add/{combinationUrl}")
    public ResponseEntity<?> addCombinationToFavorite(@PathVariable String combinationUrl){
        ResponseEntity<?> body = validateInputs(combinationUrl);
        if (body != null) return body;

        try {
            String userEmail = "german@gmail.com"; //acá hay que obtenerlo de la session, NO recibirlo por parámetro sino obtenerlo por session, ahora dejo esto pq no tenemos CRUD de user.
            return ResponseEntity.ok(this.addCombinationToFavourite.execute(combinationUrl, userEmail));
        } catch (UserNotFoundException | FavoritesException | UserCombinationFavoriteAlreadyExistsException e) {
            return buildResponseEntityError(e.getMessage());
        }
    }


    @GetMapping("/favorite/delete/{combinationUrl}")
    public ResponseEntity<?> deleteCombinationFromFavorite(@PathVariable String combinationUrl){
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
        } catch (UserNotFoundException | PageLessThanZeroException | FavoritesException e) {
            return buildResponseEntityError(e.getMessage());
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
