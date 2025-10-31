package com.outfitlab.project.domain.interfaces.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface ITrippoService {
    Map<String, String> uploadImageToTrippo(MultipartFile image) throws IOException; // me va a devolver el image_token

    ByteArrayResource getImageResource(MultipartFile image) throws IOException;

    String generateImageToModelTrippo(Map<String, String> imageToken) throws JsonProcessingException; // me va a devolver task_id

    boolean validateExtension(String filename);

    String getFileExtension(String nombreArchivo);

    Map<String, String> checkTaskStatus(String taskId) throws JsonProcessingException, InterruptedException;

    Map<String, String> saveFilesFromTask(Map<String, String> taskResponse) throws IOException;
}
