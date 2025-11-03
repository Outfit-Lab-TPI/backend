package com.outfitlab.project.domain.useCases.tripo;

import com.outfitlab.project.domain.interfaces.repositories.TripoRepository;
import com.outfitlab.project.domain.model.TripoModel;

public class FindTripoModelByTaskid {
    private final TripoRepository tripoRepository;

    public FindTripoModelByTaskid(TripoRepository tripoRepository) {
        this.tripoRepository = tripoRepository;
    }

    public TripoModel execute(String taskid) {
        return this.tripoRepository.buscarPorTaskId(taskid);
    }
}
