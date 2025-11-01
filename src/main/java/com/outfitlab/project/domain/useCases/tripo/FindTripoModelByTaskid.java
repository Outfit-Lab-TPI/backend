package com.outfitlab.project.domain.useCases.tripo;

import com.outfitlab.project.domain.interfaces.repositories.ITripoRepository;
import com.outfitlab.project.domain.model.TripoModel;

public class FindTripoModelByTaskid {
    private final ITripoRepository tripoRepository;

    public FindTripoModelByTaskid(ITripoRepository tripoRepository) {
        this.tripoRepository = tripoRepository;
    }

    public TripoModel execute(String taskid) {
        return this.tripoRepository.buscarPorTaskId(taskid);
    }
}
