package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.UserSubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserSubscriptionJpaRepository extends JpaRepository<UserSubscriptionEntity, Long> {

    @Query("SELECT us FROM UserSubscriptionEntity us JOIN FETCH us.user u JOIN FETCH us.subscription WHERE u.email = :email")
    Optional<UserSubscriptionEntity> findByUserEmail(@Param("email") String email);

    @Modifying
    @Query("UPDATE UserSubscriptionEntity u SET u.combinationsUsed = u.combinationsUsed + 1 WHERE u.user.id = :userId")
    void incrementCombinationsByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE UserSubscriptionEntity u SET u.favoritesCount = u.favoritesCount + 1 WHERE u.user.id = :userId")
    void incrementFavoritesByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE UserSubscriptionEntity u SET u.favoritesCount = u.favoritesCount - 1 WHERE u.user.id = :userId AND u.favoritesCount > 0")
    void decrementFavoritesByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE UserSubscriptionEntity u SET u.modelsGenerated = u.modelsGenerated + 1 WHERE u.user.id = :userId")
    void incrementModelsByUserId(@Param("userId") Long userId);
}
