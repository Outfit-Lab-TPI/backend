package com.outfitlab.project.infrastructure.trippo.usecases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outfitlab.project.domain.models.TripoModel;
import com.outfitlab.project.domain.repositories.TripoModelRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class ImageToModelTrippo {
    @Value("${tripo.api.key}")
    private String tripoApiKey;
    private String taskUrl = "https://api.tripo3d.ai/v2/openapi/task";

    private final RestTemplate restTemplate;
    private final TripoModelRepository tripoModelRepository;

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
}
