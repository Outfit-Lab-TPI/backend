package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.exceptions.UserGarmentFavoriteNotFoundException;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserGarmentFavoriteRepository;
import com.outfitlab.project.domain.model.UserGarmentFavoriteModel;
import com.outfitlab.project.infrastructure.model.PrendaEntity;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.model.UserGarmentFavoriteEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.GarmentJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserGarmentFavoriteJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;

public class UserGarmentFavoriteRempositoryImpl implements UserGarmentFavoriteRepository {

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

        return UserGarmentFavoriteEntity.convertEntityToModel(entity);
    }

    @Override
    public UserGarmentFavoriteEntity addToFavorite(String garmentCode, String userEmail) throws UserNotFoundException, GarmentNotFoundException {
        UserEntity user = this.userJpaRepository.findByEmail(userEmail);
        PrendaEntity garment = this.garmentJpaRepository.findByGarmentCode(garmentCode);

        if (user == null) throw new UserNotFoundException("No encontramos un usuario con el email: " + userEmail);
        if (garment == null) throw new GarmentNotFoundException("No encontramos una prenda con el código: " + garmentCode);

        return this.userGarmentFavoriteJpaRepository.save(new UserGarmentFavoriteEntity(user,garment));
    }
}
