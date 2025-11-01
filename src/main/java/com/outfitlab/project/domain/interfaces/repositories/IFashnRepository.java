package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.exceptions.FashnApiException;
import com.outfitlab.project.domain.exceptions.PredictionFailedException;
import com.outfitlab.project.presentation.dto.CombineRequest;

public interface IFashnRepository {
    String combine(CombineRequest req, String category, String avatarUrl) throws FashnApiException;

    String pollStatus(String id, int maxIntentos, long delay) throws FashnApiException, PredictionFailedException;
}
