package com.outfitlab.project.domain.useCases.tripo;

import com.outfitlab.project.domain.interfaces.repositories.ITripoRepository;
import com.outfitlab.project.domain.model.TripoModel;
import com.outfitlab.project.infrastructure.model.TripoEntity;

import java.util.Map;

public class SaveTripoModel {

    private final ITripoRepository iTripoRepository;

    public SaveTripoModel(ITripoRepository repository) {
        this.iTripoRepository = repository;
    }

    public TripoModel execute(TripoModel tripoModel) {
        return this.iTripoRepository.save(tripoModel);
    };
}
