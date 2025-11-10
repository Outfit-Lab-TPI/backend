package com.outfitlab.project.domain.useCases.combination;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.interfaces.repositories.UserCombinationFavoriteRepository;

public class AddCombinationToFavourite {

    private final UserCombinationFavoriteRepository userCombinationFavoriteRepository;

    public AddCombinationToFavourite(UserCombinationFavoriteRepository userCombinationFavoriteRepository) {
        this.userCombinationFavoriteRepository = userCombinationFavoriteRepository;
    }

    public String execute(String combinationUrl, String userEmail) throws UserCombinationFavoriteAlreadyExistsException, UserNotFoundException, FavoritesException {
        checkIfFavoriteAlreadyExists(combinationUrl, userEmail);
        addToFavorites(combinationUrl, userEmail);
        return "Combinación añadida a favoritos.";
    }

    private void addToFavorites(String combinationUrl, String userEmail) throws FavoritesException, UserNotFoundException {
        if (this.userCombinationFavoriteRepository.addToFavorite(combinationUrl, userEmail) == null) throw new FavoritesException("No se pudo agregar la prenda a favoritos.");
    }

    private void checkIfFavoriteAlreadyExists(String combinationUrl, String userEmail) throws UserCombinationFavoriteAlreadyExistsException {
        try {
            this.userCombinationFavoriteRepository.findByCombinationUrlAndUserEmail(combinationUrl, userEmail);
            throw new UserCombinationFavoriteAlreadyExistsException("La combinacion de URL: " + combinationUrl + ", ya está marcada como favorita para el usuario de email: " + userEmail);
        } catch (UserCombinationFavoriteNotFoundException e){
            // como NO se encontró como favorita, no hago nada
        }
    }
}
