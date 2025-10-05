package com.outfitlab.project.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outfitlab.project.domain.entities.TripoModel;
import com.outfitlab.project.domain.exceptions.ImageInvalidFormatException;
import com.outfitlab.project.domain.interfaces.ITrippoService;
import com.outfitlab.project.domain.repositories.TripoModelRepository;
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
public class TrippoService implements ITrippoService {

    @Value("${tripo.api.key}")
    private String tripoApiKey;
    private String uploadUrl = "https://api.tripo3d.ai/v2/openapi/upload";
    private String taskUrl = "https://api.tripo3d.ai/v2/openapi/task";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();
    private final MinioStorageService minioService;
    private final TripoModelRepository tripoModelRepository;

    public TrippoService(MinioStorageService minioService, TripoModelRepository tripoModelRepository) {
        this.minioService = minioService;
        this.tripoModelRepository = tripoModelRepository;
    }

    @Override
    public Map<String, String> uploadImageToTrippo(MultipartFile image) throws IOException {
        Map<String, String> uploadResult = new HashMap<>();

        if (!validateExtension(image.getOriginalFilename())) {
            throw new ImageInvalidFormatException("Formato de imagen no válido. Solo se aceptan JPG, JPEG, PNG y WEBP.");
        }
        
        String fileExtension = getFileExtension(image.getOriginalFilename());
        uploadResult.put("fileExtension", fileExtension);
        uploadResult.put("originalFilename", image.getOriginalFilename());

        // 1. Guardar imagen en MinIO
        log.info("Guardando imagen en MinIO: {}", image.getOriginalFilename());
        String minioImagePath = minioService.uploadImage(image);
        uploadResult.put("minioImagePath", minioImagePath);

        // 2. Subir imagen a Tripo3D
        ByteArrayResource imageResource = this.getImageResource(image);

        HttpHeaders uploadHeaders = new HttpHeaders();
        uploadHeaders.setBearerAuth(tripoApiKey);
        uploadHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file",imageResource);

        HttpEntity<MultiValueMap<String, Object>> uploadRequest = new HttpEntity<>(body, uploadHeaders);
        ResponseEntity<String> uploadResponse = restTemplate.postForEntity(
                uploadUrl,
                uploadRequest,
                String.class
        );

        if (!uploadResponse.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error al cargarle la imagen a Trippo: " + uploadResponse.getBody());
        }

        JsonNode uploadJson = mapper.readTree(uploadResponse.getBody());
        String imageToken = uploadJson.get("data").get("image_token").asText();

        uploadResult.put("imageToken", imageToken);
        log.info("Imagen subida exitosamente a Tripo3D. Token: {}", imageToken);
        
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
        TripoModel tripoModel = TripoModel.builder()
                .taskId(taskId)
                .imageToken(uploadData.get("imageToken"))
                .originalFilename(uploadData.get("originalFilename"))
                .fileExtension(uploadData.get("fileExtension"))
                .minioImagePath(uploadData.get("minioImagePath"))
                .status(TripoModel.ModelStatus.PENDING)
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
}
