package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.exceptions.FavoritesException;
import com.outfitlab.project.domain.exceptions.UserCombinationFavoriteNotFoundException;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.model.UserCombinationFavoriteModel;
import com.outfitlab.project.domain.model.dto.PageDTO;
import com.outfitlab.project.infrastructure.model.UserCombinationFavoriteEntity;

public interface UserCombinationFavoriteRepository {
    UserCombinationFavoriteModel findByCombinationUrlAndUserEmail(String combinationUrl, String userEmail) throws UserCombinationFavoriteNotFoundException;
    UserCombinationFavoriteEntity addToFavorite(String combinationUrl, String userEmail) throws UserNotFoundException;

    void deleteFromFavorites(String garmentCode, String userEmail) throws UserNotFoundException;
    PageDTO<UserCombinationFavoriteModel> getCombinationFavoritesForUserByEmail(String userEmail, int page) throws UserNotFoundException, FavoritesException;
}
