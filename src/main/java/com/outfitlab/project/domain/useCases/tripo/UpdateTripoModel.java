package com.outfitlab.project.domain.useCases.tripo;

import com.outfitlab.project.domain.exceptions.ErrorTripoEntityNotFound;
import com.outfitlab.project.domain.interfaces.repositories.ITripoRepository;
import com.outfitlab.project.domain.model.TripoModel;

public class UpdateTripoModel {

    private ITripoRepository iTripoRepository;

    public UpdateTripoModel(ITripoRepository iTripoRepository) {
        this.iTripoRepository = iTripoRepository;
    }

    public TripoModel execute(TripoModel model) throws ErrorTripoEntityNotFound {
        return this.iTripoRepository.update(model);
    }
}
