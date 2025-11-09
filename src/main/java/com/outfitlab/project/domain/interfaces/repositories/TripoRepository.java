package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.model.TripoModel;

import java.util.Map;


public interface TripoRepository {

    TripoModel buscarPorTaskId(String taskId);

    Map<String, Object> requestUploadImagenToTripo(String url) throws ErrorReadJsonException, ErrorUploadImageToTripoException, ErroBytesException, FileEmptyException;

    String requestGenerateGlbToTripo(Map<String, Object> uploadData) throws ErrorReadJsonException, ErrorGenerateGlbException;

    String requestStatusGlbTripo(String taskId) throws ErrorReadJsonException, ErrorWhenSleepException, ErrorGlbGenerateTimeExpiredException, ErrorGenerateGlbException;

    TripoModel save(TripoModel tripoModel);

    TripoModel update(TripoModel model) throws ErrorTripoEntityNotFoundException;
}
