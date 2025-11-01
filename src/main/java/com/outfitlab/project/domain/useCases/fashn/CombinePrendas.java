package com.outfitlab.project.domain.useCases.fashn;

import com.outfitlab.project.domain.exceptions.FashnApiException;
import com.outfitlab.project.domain.interfaces.repositories.IFashnRepository;
import com.outfitlab.project.presentation.dto.CombineRequest;

public class CombinePrendas {


    private final IFashnRepository iFashnRepository;

    public CombinePrendas(IFashnRepository iFashnRepository) {
        this.iFashnRepository = iFashnRepository;
    }

    public String execute(CombineRequest req, String category, String avatarUrl) throws FashnApiException {
        return this.iFashnRepository.combine(req, category, avatarUrl);
    }
}
