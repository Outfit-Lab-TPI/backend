package com.outfitlab.project.domain.useCases.fashn;

import com.outfitlab.project.domain.exceptions.FashnApiException;
import com.outfitlab.project.domain.exceptions.PredictionFailedException;
import com.outfitlab.project.domain.interfaces.repositories.FashnRepository;
import com.outfitlab.project.domain.model.dto.CombineRequestDTO;

public class CombinePrendas {

    private final String TOPS = "tops";
    private final String BOTTOMS = "bottoms";
    private final FashnRepository iFashnRepository;

    public CombinePrendas(FashnRepository iFashnRepository) {
        this.iFashnRepository = iFashnRepository;
    }

    public String execute(CombineRequestDTO request) throws FashnApiException, PredictionFailedException {
        System.out.println(request.toString());
        checkRequestCombine(request.getTop(), request.getBottom());

        if (isOnlyTop(request.getTop(), request.getBottom())) return combine(request.getTop(), TOPS, request.getAvatarType());
        if (isOnlyBotton(request.getBottom(), request.getTop())) return combine(request.getBottom(), BOTTOMS, request.getAvatarType());

        return combineTopAndBottom(request.getTop(), request.getBottom(), request.getAvatarType());
    }

    private boolean isOnlyTop(String top, String bottom) {
        return top != null && (bottom == null || bottom.isBlank());
    }

    private void checkRequestCombine(String top, String bottom) throws FashnApiException {
        if ((top == null || top.isBlank()) && (bottom == null || bottom.isBlank())) throw new FashnApiException("Debe proporcionarse al menos una prenda (superior o inferior).");
    }

    private boolean isOnlyBotton(String bottom, String top) {
        return bottom != null && (top == null || top.isBlank());
    }

    private String combine(String garmentUrl, String category, String avatarType) throws FashnApiException, PredictionFailedException {
        return this.iFashnRepository.pollStatus(this.iFashnRepository.combine(garmentUrl, category, avatarType));
    }

    private String combineTopAndBottom(String top, String bottom, String avatarType) throws FashnApiException, PredictionFailedException {
        return this.iFashnRepository.combineTopAndBottom(top, bottom, avatarType);
    }
}
