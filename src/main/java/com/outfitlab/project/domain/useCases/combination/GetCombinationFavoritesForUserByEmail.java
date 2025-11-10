package com.outfitlab.project.domain.useCases.combination;

import com.outfitlab.project.domain.exceptions.FavoritesException;
import com.outfitlab.project.domain.exceptions.PageLessThanZeroException;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserCombinationFavoriteRepository;
import com.outfitlab.project.domain.model.UserCombinationFavoriteModel;
import com.outfitlab.project.domain.model.dto.PageDTO;

public class GetCombinationFavoritesForUserByEmail {

    private final UserCombinationFavoriteRepository userCombinationFavoriteRepository;

    public GetCombinationFavoritesForUserByEmail(UserCombinationFavoriteRepository userCombinationFavoriteRepository) {
        this.userCombinationFavoriteRepository = userCombinationFavoriteRepository;
    }

    public PageDTO<UserCombinationFavoriteModel> execute(String userEmail, int page) throws PageLessThanZeroException, UserNotFoundException, FavoritesException {
        checkPageNumber(page);
        return this.userCombinationFavoriteRepository.getCombinationFavoritesForUserByEmail(userEmail, page);
    }

    private void checkPageNumber(int page) throws PageLessThanZeroException {
        if (page < 0) throw new PageLessThanZeroException("El número de página es inválido o menor que 0.");
    }
}
