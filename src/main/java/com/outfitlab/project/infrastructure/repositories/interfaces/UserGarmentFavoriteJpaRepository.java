package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.PrendaEntity;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.model.UserGarmentFavoriteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserGarmentFavoriteJpaRepository extends JpaRepository<UserGarmentFavoriteEntity, Long> {

    UserGarmentFavoriteEntity findByGarment_GarmentCodeAndUser_Email(String garmentCode, String userEmail);
    UserGarmentFavoriteEntity findByGarmentAndUser(PrendaEntity garment, UserEntity user);

    Page<UserGarmentFavoriteEntity> findFavoritesByUserEmail(String userEmail, PageRequest of);

    @Transactional
    @Modifying
    @Query("""
    DELETE FROM UserGarmentFavoriteEntity uf
    WHERE uf.garment.garmentCode = :garmentCode
    """)
    void deleteAllByGarmentCode(@Param("garmentCode")String garmentCode);
}
