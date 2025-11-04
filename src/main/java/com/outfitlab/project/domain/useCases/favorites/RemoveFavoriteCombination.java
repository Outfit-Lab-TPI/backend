package com.outfitlab.project.domain.useCases.favorites;

import com.outfitlab.project.domain.interfaces.repositories.UserFavoriteCombinationRepository;

public class RemoveFavoriteCombination {

    private final UserFavoriteCombinationRepository favoriteCombinationRepository;

    public RemoveFavoriteCombination(UserFavoriteCombinationRepository favoriteCombinationRepository) {
        this.favoriteCombinationRepository = favoriteCombinationRepository;
    }

    public void execute(Long favoriteId) {
        favoriteCombinationRepository.deleteById(favoriteId);
    }
}