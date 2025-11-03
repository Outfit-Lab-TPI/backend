package com.outfitlab.project.domain.useCases.tripo;

import com.outfitlab.project.domain.exceptions.ErrorGenerateGlbException;
import com.outfitlab.project.domain.exceptions.ErrorReadJsonException;
import com.outfitlab.project.domain.interfaces.repositories.TripoRepository;
import java.util.Map;

public class GenerateImageToModelTrippo {

    private final TripoRepository iTripoRepository;

    public GenerateImageToModelTrippo(TripoRepository iTripoRepository) {
        this.iTripoRepository = iTripoRepository;
    }

    public String execute(Map<String, String> uploadData) throws ErrorGenerateGlbException, ErrorReadJsonException {
        return this.iTripoRepository.peticionGenerateGlbToTripo(uploadData);
    }
}
