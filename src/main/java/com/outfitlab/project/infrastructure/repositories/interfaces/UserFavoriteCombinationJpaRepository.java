package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.UserFavoriteCombinationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFavoriteCombinationJpaRepository extends JpaRepository<UserFavoriteCombinationEntity, Long> {

    List<UserFavoriteCombinationEntity> findByUserIdAndIsActiveTrue(Long userId);

    int countByUserIdAndIsActiveTrue(Long userId);
}