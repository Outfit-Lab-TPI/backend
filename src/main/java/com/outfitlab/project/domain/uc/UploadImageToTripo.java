package com.outfitlab.project.domain.uc;

import com.fasterxml.jackson.databind.JsonNode;
import com.outfitlab.project.domain.exceptions.ImageInvalidFormatException;
import com.outfitlab.project.domain.interfaces.ITripoService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UploadImageToTripo {

    private final ITripoService tripoService;

    public UploadImageToTripo(ITripoService tripoService1) {
        this.tripoService = tripoService1;
    }

    public Map<String, String> execute(MultipartFile image) throws IOException {
        if (!validateExtension(image.getOriginalFilename())) {
            throw new ImageInvalidFormatException("Formato de imagen no válido. Solo se aceptan JPG, JPEG, PNG y WEBP.");
        }

        byte[] imageBytes = image.getBytes();
        String originalFilename = image.getOriginalFilename();
        String extension = getFileExtension(originalFilename);

        Map<String, String> uploadResult = new HashMap<>();
        uploadResult.put("originalFilename", originalFilename);
        uploadResult.put("fileExtension", extension);

        String imageToken = tripoService.requestUploadImageApiTripo(image);
        uploadResult.put("imageToken", imageToken);

        return uploadResult;
    }

    public boolean validateExtension(String filename) {
        String extension = getFileExtension(filename);
        return extension.equals("jpg") ||
                extension.equals("jpeg") ||
                extension.equals("png") ||
                extension.equals("webp");
    }

    public String getFileExtension(String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.isEmpty()) {
            return "";
        }
        int dotIndex = nombreArchivo.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < nombreArchivo.length() - 1) {
            return nombreArchivo.substring(dotIndex+1).toLowerCase();
        }
        return "";
    }

}
