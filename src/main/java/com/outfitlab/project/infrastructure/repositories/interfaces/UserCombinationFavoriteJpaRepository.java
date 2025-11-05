package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.UserCombinationFavoriteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCombinationFavoriteJpaRepository extends JpaRepository<UserCombinationFavoriteEntity, Long> {
    UserCombinationFavoriteEntity findBycombinationUrlAndUser_Email(String combinationUrl, String userEmail);
    Page<UserCombinationFavoriteEntity> findByUser_Email(String userEmail, PageRequest of);
}
