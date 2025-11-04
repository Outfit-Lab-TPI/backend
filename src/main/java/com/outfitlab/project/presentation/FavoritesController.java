package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.useCases.favorites.AddFavoriteCombination;
import com.outfitlab.project.domain.useCases.favorites.GetUserFavorites;
import com.outfitlab.project.domain.useCases.favorites.RemoveFavoriteCombination;
import com.outfitlab.project.presentation.dto.AddFavoriteRequest;
import com.outfitlab.project.presentation.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/{userId}/favorites")
public class FavoritesController {

    private final AddFavoriteCombination addFavoriteCombination;
    private final GetUserFavorites getUserFavorites;
    private final RemoveFavoriteCombination removeFavoriteCombination;

    public FavoritesController(
            AddFavoriteCombination addFavoriteCombination,
            GetUserFavorites getUserFavorites,
            RemoveFavoriteCombination removeFavoriteCombination
    ) {
        this.addFavoriteCombination = addFavoriteCombination;
        this.getUserFavorites = getUserFavorites;
        this.removeFavoriteCombination = removeFavoriteCombination;
    }

    @PostMapping
    public ResponseEntity<Void> addFavorite(
            @PathVariable Long userId,
            @RequestBody AddFavoriteRequest request
    ) {
        addFavoriteCombination.execute(userId, request.getGarmentRecomendationId());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@PathVariable Long userId) {
        List<FavoriteResponse> favorites = getUserFavorites.execute(userId).stream()
                .map(FavoriteResponse::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(favorites);
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Void> removeFavorite(
            @PathVariable Long userId,
            @PathVariable Long favoriteId
    ) {
        removeFavoriteCombination.execute(favoriteId);
        return ResponseEntity.noContent().build();
    }
}