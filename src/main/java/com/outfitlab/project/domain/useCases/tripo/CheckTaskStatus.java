package com.outfitlab.project.domain.useCases.tripo;

import com.outfitlab.project.domain.exceptions.ErrorGlbGenerateTimeExpiredException;
import com.outfitlab.project.domain.exceptions.ErrorReadJsonException;
import com.outfitlab.project.domain.exceptions.ErrorWhenSleepException;
import com.outfitlab.project.domain.interfaces.repositories.TripoRepository;


public class CheckTaskStatus {

    private final TripoRepository iTripoRepository;

    public CheckTaskStatus(TripoRepository iTripoRepository) {
        this.iTripoRepository = iTripoRepository;
    }

    public String execute(String taskId) throws ErrorGlbGenerateTimeExpiredException, ErrorWhenSleepException, ErrorReadJsonException {
        return this.iTripoRepository.peticionStatusGlbTripo(taskId);
    }
}
