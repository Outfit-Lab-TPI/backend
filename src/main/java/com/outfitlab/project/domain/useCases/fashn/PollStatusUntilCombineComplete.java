package com.outfitlab.project.domain.useCases.fashn;

import com.outfitlab.project.domain.exceptions.FashnApiException;
import com.outfitlab.project.domain.exceptions.PredictionFailedException;
import com.outfitlab.project.domain.interfaces.repositories.IFashnRepository;

public class PollStatusUntilCombineComplete {

    private final IFashnRepository iFashnRepository;

    public PollStatusUntilCombineComplete(IFashnRepository iFashnRepository) {
        this.iFashnRepository = iFashnRepository;
    }

    public String execute(String id) throws FashnApiException, PredictionFailedException {
        int maxIntentos = 20;
        long delay = 3000;
        return this.iFashnRepository.pollStatus(id, maxIntentos, delay);
    }
}
