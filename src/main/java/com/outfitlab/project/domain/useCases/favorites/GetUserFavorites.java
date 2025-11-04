package com.outfitlab.project.domain.useCases.favorites;

import com.outfitlab.project.domain.interfaces.repositories.UserFavoriteCombinationRepository;
import com.outfitlab.project.domain.model.UserFavoriteCombinationModel;

import java.util.List;

public class GetUserFavorites {

    private final UserFavoriteCombinationRepository favoriteCombinationRepository;

    public GetUserFavorites(UserFavoriteCombinationRepository favoriteCombinationRepository) {
        this.favoriteCombinationRepository = favoriteCombinationRepository;
    }

    public List<UserFavoriteCombinationModel> execute(Long userId) {
        return favoriteCombinationRepository.findByUserIdAndIsActiveTrue(userId);
    }
}