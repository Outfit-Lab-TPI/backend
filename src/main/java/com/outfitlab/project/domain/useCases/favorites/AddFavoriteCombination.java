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

    private final UserRepository userRepository;
    private final UserFavoriteCombinationRepository favoriteCombinationRepository;
    private final GarmentRecomendationRepository garmentRecomendationRepository;

    public AddFavoriteCombination(
            UserRepository userRepository,
            UserFavoriteCombinationRepository favoriteCombinationRepository,
            GarmentRecomendationRepository garmentRecomendationRepository
    ) {
        this.userRepository = userRepository;
        this.favoriteCombinationRepository = favoriteCombinationRepository;
        this.garmentRecomendationRepository = garmentRecomendationRepository;
    }

    public UserFavoriteCombinationModel execute(Long userId, Long garmentRecomendationId) {
        UserModel user = userRepository.findById(userId);
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado: " + userId);
        }

        int currentFavorites = favoriteCombinationRepository.countActiveByUserId(userId);
        int maxFavorites = getMaxFavoritesForStatus(user.getSubscriptionStatus());

        if (currentFavorites >= maxFavorites) {
            throw new FavoriteLimitExceededException(
                    "Límite de favoritos alcanzado. Máximo: " + maxFavorites
            );
        }

        GarmentRecomendationModel garmentRecomendation = garmentRecomendationRepository.findById(garmentRecomendationId);
        if (garmentRecomendation == null) {
            throw new RuntimeException("Recomendación no encontrada: " + garmentRecomendationId);
        }

        UserFavoriteCombinationModel favorite = new UserFavoriteCombinationModel();
        favorite.setUser(user);
        favorite.setGarmentRecomendation(garmentRecomendation);
        favorite.setCreatedAt(LocalDateTime.now());
        favorite.setIsActive(true);

        return favoriteCombinationRepository.save(favorite);
    }

    private int getMaxFavoritesForStatus(SubscriptionStatus status) {
        if (status == null || status == SubscriptionStatus.FREE || status == SubscriptionStatus.PREMIUM_EXPIRED) {
            return 1;
        }
        return 20;
    }
}
