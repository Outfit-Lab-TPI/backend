package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.interfaces.repositories.UserGarmentFavoriteRepository;

public class AddGarmentToFavorite {

    private final UserGarmentFavoriteRepository userGarmentFavoriteRepository;

    public AddGarmentToFavorite(UserGarmentFavoriteRepository userGarmentFavoriteRepository) {
        this.userGarmentFavoriteRepository = userGarmentFavoriteRepository;
    }

    public String execute(String garmentCode, String userEmail) throws GarmentNotFoundException, UserNotFoundException, UserGarmentFavoriteAlreadyExistsException, FavoritesException {
        checkIfFavoriteAlreadyExists(garmentCode, userEmail);
        addToFavorites(garmentCode, userEmail);
        return "Prenda añadida a favoritos.";
    }

    private void addToFavorites(String garmentCode, String userEmail) throws FavoritesException, UserNotFoundException, GarmentNotFoundException {
        if (this.userGarmentFavoriteRepository.addToFavorite(garmentCode, userEmail) == null) throw new FavoritesException("No se pudo agregar la prenda a favoritos.");
    }

    private void checkIfFavoriteAlreadyExists(String garmentCode, String userEmail) throws UserGarmentFavoriteAlreadyExistsException {
        try {
            this.userGarmentFavoriteRepository.findByGarmentCodeAndUserEmail(garmentCode, userEmail);
            throw new UserGarmentFavoriteAlreadyExistsException("La prenda de código: " + garmentCode + ", ya está marcada como favorita para el usuario de email: " + userEmail);
        } catch (UserGarmentFavoriteNotFoundException e){
            // como NO se encontró como favorita, no hago nada
        }
    }
}
