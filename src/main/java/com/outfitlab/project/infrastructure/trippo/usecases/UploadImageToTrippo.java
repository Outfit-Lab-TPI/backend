package com.outfitlab.project.infrastructure.trippo.usecases;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outfitlab.project.domain.exceptions.ImageInvalidFormatException;
import com.outfitlab.project.infrastructure.config.s3.S3Service;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class UploadImageToTrippo {

    @Value("${tripo.api.key}")
    private String tripoApiKey;
    private String uploadUrl = "https://api.tripo3d.ai/v2/openapi/upload";

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper = new ObjectMapper();
    private final S3Service s3Service;
    public Map<String, String> uploadImageToTrippo(MultipartFile image) throws IOException {
        if (!validateExtension(image.getOriginalFilename())) {
            throw new ImageInvalidFormatException("Formato de imagen no válido. Solo se aceptan JPG, JPEG, PNG y WEBP.");
        }

        byte[] imageBytes = image.getBytes();
        String originalFilename = image.getOriginalFilename();
        String extension = getFileExtension(originalFilename);

        Map<String, String> uploadResult = new HashMap<>();
        uploadResult.put("originalFilename", originalFilename);
        uploadResult.put("fileExtension", extension);

        // 1️⃣ Subir imagen a MinIO
        log.info("Guardando imagen en MinIO: {}", originalFilename);
        String s3Url = s3Service.uploadFile(image, "models_images");
        uploadResult.put("minioImagePath", s3Url);

        // 2️⃣ Subir imagen a Trippo
        ByteArrayResource imageResource = new ByteArrayResource(imageBytes) {
            @Override
            public String getFilename() {
                return originalFilename;
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", imageResource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(tripoApiKey);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(uploadUrl, requestEntity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error al subir imagen a Trippo: " + response.getBody());
        }

        JsonNode json = mapper.readTree(response.getBody());
        String imageToken = json.get("data").get("image_token").asText();
        uploadResult.put("imageToken", imageToken);

        log.info("Imagen subida exitosamente a Trippo. Token: {}", imageToken);

        return uploadResult;
    }

    private boolean validateExtension(String filename) {
        String extension = getFileExtension(filename);
        return extension.equals("jpg") ||
                extension.equals("jpeg") ||
                extension.equals("png") ||
                extension.equals("webp");
    }

    private String getFileExtension(String nombreArchivo) {
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
