package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.model.TripoModel;
import org.springframework.core.io.ByteArrayResource;

import java.util.Map;


public interface ITripoRepository {

    TripoModel buscarPorTaskId(String taskId);

    String peticionUploadImagenToTripo(ByteArrayResource imageResource) throws ErrorReadJsonException, ErrorUploadImageToTripo;

    String peticionGenerateGlbToTripo(Map<String, String> uploadData) throws ErrorReadJsonException, ErrorGenerateGlbException;

    String peticionStatusGlbTripo(String taskId) throws ErrorReadJsonException, ErrorWhenSleepException, ErrorGlbGenerateTimeExpiredException;

    TripoModel save(TripoModel tripoModel);

    TripoModel update(TripoModel model) throws ErrorTripoEntityNotFound;
}
