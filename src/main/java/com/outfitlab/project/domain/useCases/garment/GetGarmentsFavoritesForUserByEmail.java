package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.exceptions.FavoritesException;
import com.outfitlab.project.domain.exceptions.PageLessThanZeroException;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserGarmentFavoriteRepository;
import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.model.dto.PageDTO;

public class GetGarmentsFavoritesForUserByEmail {

    private final UserGarmentFavoriteRepository userGarmentFavoriteRepository;

    public GetGarmentsFavoritesForUserByEmail(UserGarmentFavoriteRepository userGarmentFavoriteRepository) {
        this.userGarmentFavoriteRepository = userGarmentFavoriteRepository;
    }

    public PageDTO<PrendaModel> execute(String userEmail, int page) throws PageLessThanZeroException, UserNotFoundException, FavoritesException {
        checkPageNumber(page);
        return this.userGarmentFavoriteRepository.getGarmentsFavoritesForUserByEmail(userEmail, page);
    }

    private void checkPageNumber(int page) throws PageLessThanZeroException {
        if (page < 0) throw new PageLessThanZeroException("El número de página es inválido o menor que 0.");
    }
}
