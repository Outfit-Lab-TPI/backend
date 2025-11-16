package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.interfaces.repositories.UserGarmentFavoriteRepository;

public class DeleteGarmentFromFavorite {

    private final UserGarmentFavoriteRepository userGarmentFavoriteRepository;

    public DeleteGarmentFromFavorite(UserGarmentFavoriteRepository userGarmentFavoriteRepository) {
        this.userGarmentFavoriteRepository = userGarmentFavoriteRepository;
    }

    public String execute(String garmentCode, String userEmail) throws UserGarmentFavoriteNotFoundException, UserNotFoundException, GarmentNotFoundException {
        checkIfFavoriteExists(garmentCode, userEmail);
        deleteFromFavorites(garmentCode, userEmail);
        return "Prenda eliminada de favoritos.";
    }

    private void checkIfFavoriteExists(String garmentCode, String userEmail) throws UserGarmentFavoriteNotFoundException {
        this.userGarmentFavoriteRepository.findByGarmentCodeAndUserEmail(garmentCode, userEmail);
    }

    private void deleteFromFavorites(String garmentCode, String userEmail) throws UserNotFoundException, GarmentNotFoundException {
        this.userGarmentFavoriteRepository.deleteFromFavorites(garmentCode, userEmail);
    }
}
