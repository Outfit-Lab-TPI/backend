package com.outfitlab.project.domain.useCases.tripo;

import com.outfitlab.project.domain.exceptions.ErrorTripoEntityNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.TripoRepository;
import com.outfitlab.project.domain.model.TripoModel;

public class UpdateTripoModel {

    private TripoRepository iTripoRepository;

    public UpdateTripoModel(TripoRepository iTripoRepository) {
        this.iTripoRepository = iTripoRepository;
    }

    public TripoModel execute(TripoModel model) throws ErrorTripoEntityNotFoundException {
        return this.iTripoRepository.update(model);
    }
}
