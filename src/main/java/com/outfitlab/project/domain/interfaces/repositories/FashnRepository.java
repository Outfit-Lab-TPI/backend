package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.exceptions.FashnApiException;
import com.outfitlab.project.domain.exceptions.PredictionFailedException;
import org.springframework.security.core.userdetails.UserDetails;

public interface FashnRepository {

    String combine(String garmentUrl, String category, String avatarType, UserDetails user) throws FashnApiException;

    String combineSecondGarment(String garmentUrl, String category, String avatarCombinedUrl) throws FashnApiException;

    String pollStatus(String id) throws FashnApiException, PredictionFailedException;

    String combineTopAndBottom(String top, String bottom, String avatarType, UserDetails user) throws PredictionFailedException, FashnApiException;
}
