package com.outfitlab.project.domain.useCases.favorites;

import com.outfitlab.project.domain.exceptions.FavoriteLimitExceededException;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRecomendationRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserFavoriteCombinationRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.GarmentRecomendationModel;
import com.outfitlab.project.domain.model.UserFavoriteCombinationModel;
import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.domain.model.enums.SubscriptionStatus;

import java.time.LocalDateTime;

public class AddFavoriteCombination {

    private final UserFavoriteCombinationRepository favoriteCombinationRepository;
    private final UserRepository userRepository;
    private final GarmentRecomendationRepository garmentRecomendationRepository;

    public AddFavoriteCombination(
            UserFavoriteCombinationRepository favoriteCombinationRepository,
            UserRepository userRepository,
            GarmentRecomendationRepository garmentRecomendationRepository
    ) {
        this.favoriteCombinationRepository = favoriteCombinationRepository;
        this.userRepository = userRepository;
        this.garmentRecomendationRepository = garmentRecomendationRepository;
    }

    public void execute(Long userId, Long garmentRecomendationId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new FavoriteLimitExceededException("Usuario no encontrado"));
        int favoriteCount = favoriteCombinationRepository.countByUserIdAndIsActiveTrue(userId);

        if (user.getSubscriptionStatus() == SubscriptionStatus.FREE && favoriteCount >= 2) {
            throw new FavoriteLimitExceededException("Límite alcanzado. Actualiza a Premium");
        }

        if (user.getSubscriptionStatus() == SubscriptionStatus.PREMIUM_ACTIVE && favoriteCount >= 20) {
            throw new FavoriteLimitExceededException("Límite de favoritos alcanzado");
        }

        GarmentRecomendationModel garmentRecomendation = garmentRecomendationRepository.findById(garmentRecomendationId)
                .orElseThrow(() -> new FavoriteLimitExceededException("Combinación no encontrada"));

        UserFavoriteCombinationModel favoriteCombination = new UserFavoriteCombinationModel();
        favoriteCombination.setUser(user);
        favoriteCombination.setGarmentRecomendation(garmentRecomendation);
        favoriteCombination.setCreatedAt(LocalDateTime.now());
        favoriteCombination.setActive(true);

        favoriteCombinationRepository.save(favoriteCombination);
    }
}