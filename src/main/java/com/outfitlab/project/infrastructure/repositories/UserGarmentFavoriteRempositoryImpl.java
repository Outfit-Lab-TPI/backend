package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.exceptions.FavoritesException;
import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.exceptions.UserGarmentFavoriteNotFoundException;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserGarmentFavoriteRepository;
import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.model.UserGarmentFavoriteModel;
import com.outfitlab.project.domain.model.dto.PageDTO;
import com.outfitlab.project.infrastructure.model.PrendaEntity;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.model.UserGarmentFavoriteEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.GarmentJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserGarmentFavoriteJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class UserGarmentFavoriteRempositoryImpl implements UserGarmentFavoriteRepository {

    private final int PAGE_SIZE = 10;
    private final UserGarmentFavoriteJpaRepository userGarmentFavoriteJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final GarmentJpaRepository garmentJpaRepository;

    public UserGarmentFavoriteRempositoryImpl(UserGarmentFavoriteJpaRepository userGarmentFavoriteJpaRepository,
                                              UserJpaRepository userJpaRepository,
                                              GarmentJpaRepository garmentJpaRepository) {
        this.userGarmentFavoriteJpaRepository = userGarmentFavoriteJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.garmentJpaRepository = garmentJpaRepository;
    }

    @Override
    public UserGarmentFavoriteModel findByGarmentCodeAndUserEmail(String garmentCode, String userEmail) throws UserGarmentFavoriteNotFoundException {
        UserGarmentFavoriteEntity entity = this.userGarmentFavoriteJpaRepository.findByGarment_GarmentCodeAndUser_Email(garmentCode, userEmail);
        if (entity == null) throw new UserGarmentFavoriteNotFoundException("El usuario no tiene la prenda: " + garmentCode + " como favorito.");

        return UserGarmentFavoriteEntity.convertEntityToModelWithoutUser(entity);
    }

    @Override
    public UserGarmentFavoriteEntity addToFavorite(String garmentCode, String userEmail) throws UserNotFoundException, GarmentNotFoundException {
        UserEntity user = findUserByEmail(userEmail);
        PrendaEntity garment = findGarmentByCode(garmentCode);

        if (user == null) throw new UserNotFoundException("No encontramos un usuario con el email: " + userEmail);
        if (garment == null) throw new GarmentNotFoundException("No encontramos una prenda con el código: " + garmentCode);

        return this.userGarmentFavoriteJpaRepository.save(new UserGarmentFavoriteEntity(user,garment));
    }

    private PrendaEntity findGarmentByCode(String garmentCode) {
        PrendaEntity garment = this.garmentJpaRepository.findByGarmentCode(garmentCode);
        return garment;
    }

    @Override
    public void deleteFromFavorites(String garmentCode, String userEmail) throws UserNotFoundException, GarmentNotFoundException {
        UserEntity user = findUserByEmail(userEmail);
        PrendaEntity garment = findGarmentByCode(garmentCode);

        if (user == null) throw new UserNotFoundException("No encontramos un usuario con el email: " + userEmail);
        if (garment == null) throw new GarmentNotFoundException("No encontramos una prenda con el código: " + garmentCode);

        this.userGarmentFavoriteJpaRepository.delete(this.userGarmentFavoriteJpaRepository.findByGarmentAndUser(garment, user));
    }

    @Override
    public PageDTO<PrendaModel> getGarmentsFavoritesForUserByEmail(String userEmail, int page) throws UserNotFoundException, FavoritesException {
        UserEntity user = findUserByEmail(userEmail);
        if (user == null) throw new UserNotFoundException("No encontramos un usuario con el email: " + userEmail);

        Page<UserGarmentFavoriteEntity> favorites = this.userGarmentFavoriteJpaRepository.findFavoritesByUserEmail(userEmail, PageRequest.of(page, PAGE_SIZE));
        if (favorites.isEmpty()) throw new FavoritesException("El usuario no tiene prendas favoritas.");

        Page<PrendaModel> garmentPage = favorites.map(fav -> PrendaEntity.convertToModel(fav.getGarment()));
        return new PageDTO<>(
                garmentPage.getContent(),
                garmentPage.getNumber(),
                garmentPage.getSize(),
                garmentPage.getTotalElements(),
                garmentPage.getTotalPages(),
                garmentPage.isLast()
        );
    }

    private UserEntity findUserByEmail(String userEmail) {
        return this.userJpaRepository.findByEmail(userEmail);
    }
}
