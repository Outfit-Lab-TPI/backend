package com.outfitlab.project.domain.useCases.tripo;

import com.outfitlab.project.domain.exceptions.ErrorTripoEntityNotFound;
import com.outfitlab.project.domain.interfaces.repositories.TripoRepository;
import com.outfitlab.project.domain.model.TripoModel;

public class UpdateTripoModel {

    private TripoRepository iTripoRepository;

    public UpdateTripoModel(TripoRepository iTripoRepository) {
        this.iTripoRepository = iTripoRepository;
    }

    public TripoModel execute(TripoModel model) throws ErrorTripoEntityNotFound {
        return this.iTripoRepository.update(model);
    }
}
