package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.PrendaEntity;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.model.UserGarmentFavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGarmentFavoriteJpaRepository extends JpaRepository<UserGarmentFavoriteEntity, Long> {

    UserGarmentFavoriteEntity findByGarment_GarmentCodeAndUser_Email(String garmentCode, String userEmail);
    UserGarmentFavoriteEntity findByGarmentAndUser(PrendaEntity garment, UserEntity user);
}
