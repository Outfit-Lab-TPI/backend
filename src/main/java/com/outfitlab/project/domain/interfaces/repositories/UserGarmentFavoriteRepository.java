package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.exceptions.FavoritesException;
import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.exceptions.UserGarmentFavoriteNotFoundException;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.model.UserGarmentFavoriteModel;
import com.outfitlab.project.domain.model.dto.PageDTO;
import com.outfitlab.project.infrastructure.model.UserGarmentFavoriteEntity;

public interface UserGarmentFavoriteRepository {
        UserGarmentFavoriteModel findByGarmentCodeAndUserEmail(String garmentCode, String userEmail)
                        throws UserGarmentFavoriteNotFoundException;

        UserGarmentFavoriteEntity addToFavorite(String garmentCode, String userEmail)
                        throws UserNotFoundException, GarmentNotFoundException;

        void deleteFromFavorites(String garmentCode, String userEmail)
                        throws UserNotFoundException, GarmentNotFoundException;

        PageDTO<PrendaModel> getGarmentsFavoritesForUserByEmail(String userEmail, int page)
                        throws UserNotFoundException, FavoritesException;
}
