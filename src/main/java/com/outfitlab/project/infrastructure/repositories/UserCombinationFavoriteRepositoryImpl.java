package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.interfaces.repositories.UserCombinationFavoriteRepository;
import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.model.UserCombinationFavoriteModel;
import com.outfitlab.project.domain.model.dto.PageDTO;
import com.outfitlab.project.infrastructure.model.PrendaEntity;
import com.outfitlab.project.infrastructure.model.UserCombinationFavoriteEntity;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.model.UserGarmentFavoriteEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserCombinationFavoriteJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class UserCombinationFavoriteRepositoryImpl implements UserCombinationFavoriteRepository {

    private final UserCombinationFavoriteJpaRepository userCombinationFavoriteJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final int PAGE_SIZE = 10;

    public UserCombinationFavoriteRepositoryImpl(UserCombinationFavoriteJpaRepository userCombinationFavoriteJpaRepository, UserJpaRepository userJpaRepository){
        this.userCombinationFavoriteJpaRepository = userCombinationFavoriteJpaRepository;
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public UserCombinationFavoriteModel findByCombinationUrlAndUserEmail(String combinationUrl, String userEmail) throws UserCombinationFavoriteNotFoundException {
        UserCombinationFavoriteEntity entity = this.userCombinationFavoriteJpaRepository.findBycombinationUrlAndUser_Email(combinationUrl, userEmail);
        if (entity == null) throw new UserCombinationFavoriteNotFoundException("El usuario no tiene la combinacion con esta URL: " + combinationUrl + " como favorito.");

        return UserCombinationFavoriteEntity.convertEntityToModelWithoutUser(entity);
    }

    @Override
    public UserCombinationFavoriteEntity addToFavorite(String combinationUrl, String userEmail) throws UserNotFoundException {
        UserEntity user = findUserByEmail(userEmail);
        if (user == null) throw new UserNotFoundException("No encontramos un usuario con el email: " + userEmail);

        return this.userCombinationFavoriteJpaRepository.save(new UserCombinationFavoriteEntity(user,combinationUrl));
    }

    @Override
    public void deleteFromFavorites(String comninationUrl, String userEmail) throws UserNotFoundException {
        UserEntity user = findUserByEmail(userEmail);
        if (user == null) throw new UserNotFoundException("No encontramos un usuario con el email: " + userEmail);

        this.userCombinationFavoriteJpaRepository.delete(this.userCombinationFavoriteJpaRepository.findBycombinationUrlAndUser_Email(comninationUrl, user.getEmail()));
    }

    @Override
    public PageDTO<UserCombinationFavoriteModel> getCombinationFavoritesForUserByEmail(String userEmail, int page) throws UserNotFoundException, FavoritesException {
        UserEntity user = findUserByEmail(userEmail);
        if (user == null) throw new UserNotFoundException("No encontramos un usuario con el email: " + userEmail);

        Page<UserCombinationFavoriteEntity> favorites = this.userCombinationFavoriteJpaRepository.findByUser_Email(userEmail, PageRequest.of(page, PAGE_SIZE));
        if (favorites.isEmpty()) throw new FavoritesException("El usuario no tiene combinaciones favoritas.");

        Page<UserCombinationFavoriteModel> combinationPage = favorites.map(UserCombinationFavoriteEntity::convertEntityToModelWithoutUser);
        return new PageDTO<>(
                combinationPage.getContent(),
                combinationPage.getNumber(),
                combinationPage.getSize(),
                combinationPage.getTotalElements(),
                combinationPage.getTotalPages(),
                combinationPage.isLast()
        );
    }

    private UserEntity findUserByEmail(String userEmail) {
        return this.userJpaRepository.findByEmail(userEmail);
    }
}
