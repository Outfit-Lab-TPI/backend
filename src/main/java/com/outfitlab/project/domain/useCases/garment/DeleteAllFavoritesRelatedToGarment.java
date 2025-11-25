package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.interfaces.repositories.UserGarmentFavoriteRepository;

public class DeleteAllFavoritesRelatedToGarment {

    private final UserGarmentFavoriteRepository userGarmentFavoriteRepository;

    public DeleteAllFavoritesRelatedToGarment(UserGarmentFavoriteRepository userGarmentFavoriteRepository){
        this.userGarmentFavoriteRepository = userGarmentFavoriteRepository;
    }

    public void execute(String garmentCode){
        this.userGarmentFavoriteRepository.deleteFavoritesRelatedToGarment(garmentCode);
    }
}
