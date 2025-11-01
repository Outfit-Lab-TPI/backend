package com.outfitlab.project.infrastructure.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.interfaces.repositories.ITripoRepository;
import com.outfitlab.project.domain.model.TripoModel;
import com.outfitlab.project.infrastructure.model.TripoEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.IJpaTripoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class TripoRepositoryImpl implements ITripoRepository {

    @Value("${tripo.api.key}")
    private String tripoApiKey;
    private String uploadUrl = "https://api.tripo3d.ai/v2/openapi/upload";
    private String taskUrl = "https://api.tripo3d.ai/v2/openapi/task";

    private final ObjectMapper mapper;
    private final RestTemplate restTemplate;
    private final IJpaTripoRepository iJpatripoRepository;

    public TripoRepositoryImpl(RestTemplate restTemplate, IJpaTripoRepository iJpatripoRepository) {
        this.restTemplate = restTemplate;
        this.mapper = new ObjectMapper();
        this.iJpatripoRepository = iJpatripoRepository;
    }


    @Override
    public TripoModel buscarPorTaskId(String taskId) {
        return TripoEntity.convertToModel(this.iJpatripoRepository.findByTaskId(taskId));
    }

    @Override
    public String peticionUploadImagenToTripo(ByteArrayResource imageResource) throws ErrorReadJsonException, ErrorUploadImageToTripo {
        String imagetoken = "";

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", imageResource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(tripoApiKey);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(uploadUrl, requestEntity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ErrorUploadImageToTripo("Error al subir imagen a Trippo: " + response.getBody());
        }

        try{
            JsonNode json = mapper.readTree(response.getBody());
            String imageToken = json.get("data").get("image_token").asText();
        }catch (JsonProcessingException e){
            throw new ErrorReadJsonException("Error al leer el Json: " + response.getBody() + "--- Error: " + e.getMessage());
        }

        return imagetoken;
    }

    @Override
    public String peticionGenerateGlbToTripo(Map<String, String> uploadData) throws ErrorReadJsonException, ErrorGenerateGlbException {

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

        String taskBody;

        try{
            taskBody = mapper.writeValueAsString(bodyMap);
        }catch (JsonProcessingException e){
            throw new ErrorReadJsonException("Error al leer el Json: " + e.getMessage());
        }

        HttpEntity<String> taskEntity = new HttpEntity<>(taskBody, taskHeaders);
        ResponseEntity<String> taskResponse = restTemplate.postForEntity(
                taskUrl,
                taskEntity,
                String.class
        );

        if (!taskResponse.getStatusCode().is2xxSuccessful()) {
            throw new ErrorGenerateGlbException("Error al crear task para generar el GLB: " + taskResponse.getBody());
        }

        try{
            JsonNode taskJson = mapper.readTree(taskResponse.getBody());
            return taskJson.get("data").get("task_id").asText();
        }catch (JsonProcessingException e){
            throw new ErrorReadJsonException("Error al leer el Json: " + taskResponse.getBody() + "--- Error: " + e.getMessage());
        }
    }

    @Override
    public TripoModel save(TripoModel tripoModel) {
        TripoEntity entity = TripoEntity.convertToEntity(tripoModel);
        entity = this.iJpatripoRepository.save(entity);
        return TripoEntity.convertToModel(entity);
    }

    @Override
    public TripoModel update(TripoModel model) throws ErrorTripoEntityNotFound {
        TripoEntity entity = this.iJpatripoRepository.findByTaskId(model.getTaskId());

        if (entity == null) {
            throw new ErrorTripoEntityNotFound("No encontramos un registro Tripo con el taskId: " + model.getTaskId());
        }

        entity.setErrorMessage(model.getErrorMessage());
        entity.setFileExtension(model.getFileExtension());
        entity.setImageToken(model.getImageToken());
        entity.setTaskId(model.getTaskId());
        entity.setTripoModelUrl(model.getTripoModelUrl());
        entity.setMinioImagePath(model.getMinioImagePath());
        entity.setMinioModelPath(model.getMinioModelPath());
        entity.setOriginalFilename(model.getOriginalFilename());
        entity.setStatus(model.getStatus());

        return TripoEntity.convertToModel(this.iJpatripoRepository.save(entity));
    }

    @Override
    public String peticionStatusGlbTripo(String taskId) throws ErrorReadJsonException, ErrorWhenSleepException, ErrorGlbGenerateTimeExpiredException {
        String glbUrl = null;
        JsonNode statusJson;

        HttpHeaders taskHeaders = new HttpHeaders();
        taskHeaders.setContentType(MediaType.APPLICATION_JSON);
        taskHeaders.setBearerAuth(tripoApiKey);

        HttpEntity<Void> entityWithTaskHeaders = new HttpEntity<>(taskHeaders);
        ResponseEntity<String> statusResponse;

        for (int i = 0; i < 20; i++) { // acá espero 3 y pico min para ver si ya me generó el glb
            try{
                Thread.sleep(10000);

                statusResponse = restTemplate.exchange(
                        taskUrl + "/" + taskId,
                        HttpMethod.GET,
                        entityWithTaskHeaders,
                        String.class);

                statusJson = mapper.readTree(statusResponse.getBody());

            } catch (InterruptedException e) {
                throw new ErrorWhenSleepException("Hubo un error al esperar a la siguiente petición del GLB: " + e.getMessage());
            } catch (JsonProcessingException e) {
                throw new ErrorReadJsonException("Hubo un error al leer la respuesta del modelo GLB de Tripo: " + e.getMessage());
            }

            String status = statusJson.path("data").get("status").asText();
            System.out.println("EL ESTADO ACTUAL ES: " + status);

            if (status.equalsIgnoreCase("success")) {
                glbUrl = statusJson.path("data").get("result").get("pbr_model").get("url").asText();
            } else if (status.equalsIgnoreCase("failed")) {
                throw new RuntimeException("La tarea falló: " + statusResponse.getBody());
            }
        }

        if (glbUrl == null) {
            throw new ErrorGlbGenerateTimeExpiredException("Se agotó el tiempo de espera para la generación del GLB. El estatus sigue pendiente.");
        }

        return glbUrl;
    }
}
