package com.outfitlab.project.domain.useCases.fashn;

import com.outfitlab.project.domain.exceptions.FashnApiException;
import com.outfitlab.project.domain.exceptions.PlanLimitExceededException;
import com.outfitlab.project.domain.exceptions.PredictionFailedException;
import com.outfitlab.project.domain.exceptions.SubscriptionNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.FashnRepository;
import com.outfitlab.project.domain.model.dto.CombineRequestDTO;
import com.outfitlab.project.domain.useCases.subscription.CheckUserPlanLimit;
import com.outfitlab.project.domain.useCases.subscription.IncrementUsageCounter;
import org.springframework.security.core.userdetails.UserDetails;

public class CombinePrendas {

    private final String TOPS = "tops";
    private final String BOTTOMS = "bottoms";
    private final FashnRepository iFashnRepository;
    private final CheckUserPlanLimit checkUserPlanLimit;
    private final IncrementUsageCounter incrementUsageCounter;

    public CombinePrendas(FashnRepository iFashnRepository,
            CheckUserPlanLimit checkUserPlanLimit,
            IncrementUsageCounter incrementUsageCounter) {
        this.iFashnRepository = iFashnRepository;
        this.checkUserPlanLimit = checkUserPlanLimit;
        this.incrementUsageCounter = incrementUsageCounter;
    }

    public String execute(CombineRequestDTO request, UserDetails user)
            throws FashnApiException, PredictionFailedException, PlanLimitExceededException,
            SubscriptionNotFoundException {
        System.out.println(request.toString());

        String userEmail = user.getUsername();

        // Validar límite de combinaciones
        checkUserPlanLimit.execute(userEmail, "combinations");

        checkRequestCombine(request.getTop(), request.getBottom());

        String result;
        if (isOnlyTop(request.getTop(), request.getBottom())) {
            result = combine(request.getTop(), TOPS, request.getAvatarType(), user);
        } else if (isOnlyBotton(request.getBottom(), request.getTop())) {
            result = combine(request.getBottom(), BOTTOMS, request.getAvatarType(), user);
        } else {
            result = combineTopAndBottom(request.getTop(), request.getBottom(), request.getAvatarType(), user);
        }

        // Incrementar contador de combinaciones después de éxito
        incrementUsageCounter.execute(userEmail, "combinations");

        return result;
    }

    private boolean isOnlyTop(String top, String bottom) {
        return top != null && (bottom == null || bottom.isBlank());
    }

    private void checkRequestCombine(String top, String bottom) throws FashnApiException {
        if ((top == null || top.isBlank()) && (bottom == null || bottom.isBlank()))
            throw new FashnApiException("Debe proporcionarse al menos una prenda (superior o inferior).");
    }

    private boolean isOnlyBotton(String bottom, String top) {
        return bottom != null && (top == null || top.isBlank());
    }

    private String combine(String garmentUrl, String category, String avatarType, UserDetails user)
            throws FashnApiException, PredictionFailedException {
        return this.iFashnRepository.pollStatus(this.iFashnRepository.combine(garmentUrl, category, avatarType, user));
    }

    private String combineTopAndBottom(String top, String bottom, String avatarType, UserDetails user)
            throws FashnApiException, PredictionFailedException {
        return this.iFashnRepository.combineTopAndBottom(top, bottom, avatarType, user);
    }
}
