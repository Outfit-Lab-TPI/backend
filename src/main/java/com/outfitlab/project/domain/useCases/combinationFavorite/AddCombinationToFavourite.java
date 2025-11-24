package com.outfitlab.project.domain.useCases.combinationFavorite;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.interfaces.repositories.UserCombinationFavoriteRepository;
import com.outfitlab.project.domain.useCases.subscription.CheckUserPlanLimit;
import com.outfitlab.project.domain.useCases.subscription.IncrementUsageCounter;

public class AddCombinationToFavourite {

    private final UserCombinationFavoriteRepository userCombinationFavoriteRepository;
    private final CheckUserPlanLimit checkUserPlanLimit;
    private final IncrementUsageCounter incrementUsageCounter;

    public AddCombinationToFavourite(UserCombinationFavoriteRepository userCombinationFavoriteRepository,
                                     CheckUserPlanLimit checkUserPlanLimit,
                                     IncrementUsageCounter incrementUsageCounter) {
        this.userCombinationFavoriteRepository = userCombinationFavoriteRepository;
        this.checkUserPlanLimit = checkUserPlanLimit;
        this.incrementUsageCounter = incrementUsageCounter;
    }

    public String execute(String combinationUrl, String userEmail) 
            throws UserCombinationFavoriteAlreadyExistsException, 
                   UserNotFoundException, 
                   FavoritesException,
                   PlanLimitExceededException,
                   SubscriptionNotFoundException {
        
        // Verificar límite de favoritos
        checkUserPlanLimit.execute(userEmail, "favorites");
        
        checkIfFavoriteAlreadyExists(combinationUrl, userEmail);
        addToFavorites(combinationUrl, userEmail);
        
        // Incrementar contador de favoritos
        incrementUsageCounter.execute(userEmail, "favorites");

        return "Combinación añadida a favoritos.";
    }

    private void addToFavorites(String combinationUrl, String userEmail) throws FavoritesException, UserNotFoundException {
        if (this.userCombinationFavoriteRepository.addToFavorite(combinationUrl, userEmail) == null) throw new FavoritesException("No se pudo agregar la prenda a favoritos.");
    }

    private void checkIfFavoriteAlreadyExists(String combinationUrl, String userEmail) throws UserCombinationFavoriteAlreadyExistsException {
        try {
            this.userCombinationFavoriteRepository.findByCombinationUrlAndUserEmail(combinationUrl, userEmail);
            throw new UserCombinationFavoriteAlreadyExistsException("La combinacion de URL: " + combinationUrl + ", ya está marcada como favorita para el usuario de email: " + userEmail);
        } catch (UserCombinationFavoriteNotFoundException e){
            // como NO se encontró como favorita, no hago nada
        }
    }
}
