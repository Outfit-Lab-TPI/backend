package com.outfitlab.project.domain.service;

import org.springframework.http.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outfitlab.project.infrastructure.model.TripoEntity;
import com.outfitlab.project.domain.exceptions.ImageInvalidFormatException;
import com.outfitlab.project.domain.interfaces.service.ITrippoService;
import com.outfitlab.project.domain.interfaces.repositories.ITripoRepository;
import com.outfitlab.project.infrastructure.config.s3.S3Service;
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
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class TrippoService implements ITrippoService {

    @Value("${tripo.api.key}")
    private String tripoApiKey;
    private String uploadUrl = "https://api.tripo3d.ai/v2/openapi/upload";
    private String taskUrl = "https://api.tripo3d.ai/v2/openapi/task";

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper = new ObjectMapper();
    private final S3Service s3Service;
    private final ITripoRepository tripoModelRepository;

    public TrippoService(S3Service s3Service, ITripoRepository tripoModelRepository, RestTemplate restTemplate) {
        this.s3Service = s3Service;
        this.tripoModelRepository = tripoModelRepository;
        this.restTemplate = restTemplate;
    }

    @Override
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

    @Override
    public ByteArrayResource getImageResource(MultipartFile image) throws IOException {
        return new ByteArrayResource(image.getBytes()) {
            @Override
            public String getFilename() {
                return image.getOriginalFilename();
            }
        };
    }


    @Override
    public String generateImageToModelTrippo(Map<String, String> uploadData) throws JsonProcessingException {

        HttpHeaders taskHeaders = new HttpHeaders();
        taskHeaders.setContentType(MediaType.APPLICATION_JSON);
        taskHeaders.setBearerAuth(tripoApiKey);

        Map<String, String> fileMap = new HashMap<>();
        fileMap.put("type", uploadData.get("fileExtension"));
        fileMap.put("file_token", uploadData.get("imageToken"));

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("type", "image_to_model");
        bodyMap.put("file", fileMap);

        ObjectMapper mapper = new ObjectMapper();
        String taskBody = mapper.writeValueAsString(bodyMap);

        HttpEntity<String> taskEntity = new HttpEntity<>(taskBody, taskHeaders);
        ResponseEntity<String> taskResponse = restTemplate.postForEntity(
                taskUrl,
                taskEntity,
                String.class
        );

        if (!taskResponse.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error al crear task: " + taskResponse.getBody());
        }

        JsonNode taskJson = mapper.readTree(taskResponse.getBody());
        String taskId = taskJson.get("data").get("task_id").asText();
        
        // Guardar en base de datos
        TripoEntity tripoModel = TripoEntity.builder()
                .taskId(taskId)
                .imageToken(uploadData.get("imageToken"))
                .originalFilename(uploadData.get("originalFilename"))
                .fileExtension(uploadData.get("fileExtension"))
                .minioImagePath(uploadData.get("minioImagePath"))
                .status(TripoEntity.ModelStatus.PENDING)
                .build();
        
        tripoModelRepository.save(tripoModel);
        log.info("Modelo guardado en BD con taskId: {}", taskId);
        
        return taskId;
    }


    @Override
    public boolean validateExtension(String filename) {
        String extension = getFileExtension(filename);
        return extension.equals("jpg") ||
                extension.equals("jpeg") ||
                extension.equals("png") ||
                extension.equals("webp");
    }

    @Override
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

    @Override
    public Map<String, String> checkTaskStatus(String taskId) throws JsonProcessingException, InterruptedException {
        Map<String, String> taskStatus = new HashMap<>();
        String glbUrl = null;
        String webpUrl = null;

        for (int i = 0; i < 40; i++) { // acá espero 1 min para ver si ya me generó el glb
            Thread.sleep(10000);

            HttpHeaders taskHeaders = new HttpHeaders();
            taskHeaders.setContentType(MediaType.APPLICATION_JSON);
            taskHeaders.setBearerAuth(tripoApiKey);

            HttpEntity<Void> entityWithTaskHeaders = new HttpEntity<>(taskHeaders);
            ResponseEntity<String> statusResponse = restTemplate.exchange(
                    taskUrl + "/" + taskId,
                    HttpMethod.GET,
                    entityWithTaskHeaders,
                    String.class);

            JsonNode statusJson = mapper.readTree(statusResponse.getBody());
            String status = statusJson.path("data").get("status").asText();
            System.out.println("EL ESTADO ACTUAL ES: " + status + ". ITERACIÓN N°: " + i);

            if (status.equalsIgnoreCase("success")) {
                glbUrl = statusJson.path("data").get("result").get("pbr_model").get("url").asText();
                webpUrl = statusJson.path("data").get("result").get("rendered_image").get("url").asText();
                taskStatus.put("glbUrl", glbUrl);
                taskStatus.put("webpUrl", webpUrl);
                taskStatus.put("taskId", taskId);
                break;
            } else if (status.equalsIgnoreCase("failed")) {
                throw new RuntimeException("La tarea falló: " + statusResponse.getBody());
            }
        }
        return taskStatus;
    }

    @Override
    public Map<String, String> saveFilesFromTask(Map<String, String> taskResponse) throws IOException {
        // cuando tengamos el AWS, la idea es que estas url las bajemos y enviemos esos archivos a nuestro AWS y luego obtener
        // la URL de ESOS archivos recién alojados en nuestro AWS y enviarlas al front así pueden usarlas para mostrarlos.
        // (ya que las url de trippo no nos deja usarla en el front)

        String imageUrl = taskResponse.get("webpUrl");
        String modelUrl = taskResponse.get("glbUrl");
        String taskId = taskResponse.get("taskId");

        if (imageUrl == null || modelUrl == null || taskId == null) {
            throw new IllegalArgumentException("Faltan datos en el mapa");
        }

        Path resourcesPath = Paths.get("src/main/resources");
        Map<String, String> localPaths = new HashMap<>();

        Path imagePath = resourcesPath.resolve(taskId + "_image.webp");
        try (InputStream in = new URL(imageUrl).openStream()) {
            Files.copy(in, imagePath, StandardCopyOption.REPLACE_EXISTING);
        }
        localPaths.put("image", "resources/" + imagePath.getFileName());

        Path modelPath = resourcesPath.resolve(taskId + "_3d.glb");
        try (InputStream in = new URL(modelUrl).openStream()) {
            Files.copy(in, modelPath, StandardCopyOption.REPLACE_EXISTING);
        }
        localPaths.put("glb", "resources/" + modelPath.getFileName());

        return localPaths;
    }
}
