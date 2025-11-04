package com.outfitlab.project.domain.useCases.favorites;

import com.outfitlab.project.domain.exceptions.UserNotFound;
import com.outfitlab.project.domain.interfaces.repositories.UserFavoriteCombinationRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.domain.model.enums.SubscriptionStatus;
import com.outfitlab.project.presentation.dto.SubscriptionStatusResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class GetUserSubscriptionStatus {

    private final UserRepository userRepository;
    private final UserFavoriteCombinationRepository userFavoriteCombinationRepository;

    public GetUserSubscriptionStatus(UserRepository userRepository, UserFavoriteCombinationRepository userFavoriteCombinationRepository) {
        this.userRepository = userRepository;
        this.userFavoriteCombinationRepository = userFavoriteCombinationRepository;
    }

    public SubscriptionStatusResponse execute(Long userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFound("User not found with id: " + userId));

        int currentFavorites = userFavoriteCombinationRepository.countByUserIdAndIsActiveTrue(userId);
        int maxFavorites = getMaxFavoritesForUser(user.getSubscriptionStatus());

        return new SubscriptionStatusResponse(
                user.getId(),
                user.getSubscriptionStatus(),
                user.getSubscriptionExpiresAt(),
                maxFavorites,
                currentFavorites
        );
    }

    private int getMaxFavoritesForUser(SubscriptionStatus status) {
        return switch (status) {
            case FREE -> 2;
            case PREMIUM_ACTIVE -> 20;
            case PREMIUM_EXPIRED -> 2; // Expired premium users revert to free limit
        };
    }
}
