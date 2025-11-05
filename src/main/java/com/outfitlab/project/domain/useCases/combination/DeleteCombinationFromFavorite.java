package com.outfitlab.project.domain.useCases.combination;

import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.exceptions.UserCombinationFavoriteNotFoundException;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserCombinationFavoriteRepository;

public class DeleteCombinationFromFavorite {

    private final UserCombinationFavoriteRepository userCombinationFavoriteRepository;

    public DeleteCombinationFromFavorite(UserCombinationFavoriteRepository userCombinationFavoriteRepository) {
        this.userCombinationFavoriteRepository = userCombinationFavoriteRepository;
    }

    public String execute(String combinationUrl, String userEmail) throws UserCombinationFavoriteNotFoundException, UserNotFoundException {
        checkIfFavoriteExists(combinationUrl, userEmail);
        deleteFromFavorites(combinationUrl, userEmail);
        return "Combinación eliminada de favoritos.";
    }

    private void checkIfFavoriteExists(String combinationUrl, String userEmail) throws UserCombinationFavoriteNotFoundException {
        this.userCombinationFavoriteRepository.findByCombinationUrlAndUserEmail(combinationUrl, userEmail);
    }

    private void deleteFromFavorites(String combinationUrl, String userEmail) throws UserNotFoundException {
        this.userCombinationFavoriteRepository.deleteFromFavorites(combinationUrl, userEmail);
    }
}
