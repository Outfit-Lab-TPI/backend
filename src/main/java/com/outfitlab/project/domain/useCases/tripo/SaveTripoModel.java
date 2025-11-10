package com.outfitlab.project.domain.useCases.tripo;

import com.outfitlab.project.domain.interfaces.repositories.TripoRepository;
import com.outfitlab.project.domain.model.TripoModel;

public class SaveTripoModel {

    private final TripoRepository iTripoRepository;

    public SaveTripoModel(TripoRepository repository) {
        this.iTripoRepository = repository;
    }

    public TripoModel execute(TripoModel tripoModel) {
        return this.iTripoRepository.save(tripoModel);
    };
}
