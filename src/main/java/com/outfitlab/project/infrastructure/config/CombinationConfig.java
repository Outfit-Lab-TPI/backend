package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.repositories.CombinationAttemptRepository;
import com.outfitlab.project.domain.interfaces.repositories.CombinationRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserCombinationFavoriteRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.useCases.combination.CreateCombination;
import com.outfitlab.project.domain.useCases.combination.DeleteAllCombinationRelatedToGarment;
import com.outfitlab.project.domain.useCases.combination.GetCombinationByPrendas;
import com.outfitlab.project.domain.useCases.combinationAttempt.DeleteAllCombinationAttempsRelatedToCombinationsRelatedToGarment;
import com.outfitlab.project.domain.useCases.combinationAttempt.RegisterCombinationAttempt;
import com.outfitlab.project.domain.useCases.combinationFavorite.AddCombinationToFavourite;
import com.outfitlab.project.domain.useCases.combinationFavorite.DeleteCombinationFromFavorite;
import com.outfitlab.project.domain.useCases.combinationFavorite.GetCombinationFavoritesForUserByEmail;
import com.outfitlab.project.domain.useCases.subscription.CheckUserPlanLimit;
import com.outfitlab.project.domain.useCases.subscription.IncrementUsageCounter;
import com.outfitlab.project.infrastructure.repositories.UserCombinationFavoriteRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.interfaces.CombinationJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserCombinationFavoriteJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CombinationConfig {

    @Bean
    public UserCombinationFavoriteRepository userCombinationFavoriteRepository(UserCombinationFavoriteJpaRepository jpaRepository,
                                                                           UserJpaRepository userJpaRepository) {
        return new UserCombinationFavoriteRepositoryImpl(jpaRepository, userJpaRepository);
    }

    @Bean
    public AddCombinationToFavourite addCombinationToFavourite(UserCombinationFavoriteRepository userCombinationFavoriteRepository,
                                                               CheckUserPlanLimit checkUserPlanLimit,
                                                               IncrementUsageCounter incrementUsageCounter){
        return new AddCombinationToFavourite(userCombinationFavoriteRepository, checkUserPlanLimit, incrementUsageCounter);
    }

    @Bean
    public GetCombinationFavoritesForUserByEmail getCombinationFavoritesForUserByEmail(UserCombinationFavoriteRepository userCombinationFavoriteRepository){
        return new GetCombinationFavoritesForUserByEmail(userCombinationFavoriteRepository);
    }

    @Bean
    public DeleteCombinationFromFavorite deleteCombinationFromFavorite(UserCombinationFavoriteRepository userCombinationFavoriteRepository){
        return new DeleteCombinationFromFavorite(userCombinationFavoriteRepository);
    }

    @Bean
    public DeleteAllCombinationRelatedToGarment deleteAllCombinationRelatedToGarment(CombinationRepository combinationRepository){
        return new DeleteAllCombinationRelatedToGarment(combinationRepository);
    }

    @Bean
    public DeleteAllCombinationAttempsRelatedToCombinationsRelatedToGarment deleteAllCombinationAttempsRelatedToCombinationsRelatedToGarment(CombinationAttemptRepository combinationAttemptRepository){
        return new DeleteAllCombinationAttempsRelatedToCombinationsRelatedToGarment(combinationAttemptRepository);
    }

    @Bean
    public RegisterCombinationAttempt registerCombinationAttempt(
            CombinationAttemptRepository attemptRepository,
            UserRepository userRepository
    ) {
        return new RegisterCombinationAttempt(attemptRepository, userRepository);
    }

    @Bean
    public GetCombinationByPrendas getCombinationByPrendas(CombinationRepository combinationRepository) {
        return new GetCombinationByPrendas(combinationRepository);
    }

    @Bean
    public CreateCombination createCombination(CombinationRepository combinationRepository) {
        return new CreateCombination(combinationRepository);
    }
}
