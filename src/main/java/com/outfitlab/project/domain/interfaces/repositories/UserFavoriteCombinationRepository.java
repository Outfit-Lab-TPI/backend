package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.model.UserFavoriteCombinationModel;

import java.util.List;

public interface UserFavoriteCombinationRepository {

    List<UserFavoriteCombinationModel> findByUserIdAndIsActiveTrue(Long userId);

    int countByUserIdAndIsActiveTrue(Long userId);

    UserFavoriteCombinationModel save(UserFavoriteCombinationModel favoriteCombination);

    void deleteById(Long id);
}