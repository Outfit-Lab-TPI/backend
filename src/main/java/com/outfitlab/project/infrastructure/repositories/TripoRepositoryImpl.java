package com.outfitlab.project.infrastructure.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.interfaces.repositories.TripoRepository;
import com.outfitlab.project.domain.model.TripoModel;
import com.outfitlab.project.infrastructure.model.TripoEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.TripoJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TripoRepositoryImpl implements TripoRepository {

    @Value("${tripo.api.key}")
    private String tripoApiKey;
    private String uploadUrl = "https://api.tripo3d.ai/v2/openapi/upload";
    private String taskUrl = "https://api.tripo3d.ai/v2/openapi/task";

    private final ObjectMapper mapper;
    private final RestTemplate restTemplate;
    private final TripoJpaRepository iJpatripoRepository;

    public TripoRepositoryImpl(RestTemplate restTemplate, TripoJpaRepository iJpatripoRepository) {
        this.restTemplate = restTemplate;
        this.mapper = new ObjectMapper();
        this.iJpatripoRepository = iJpatripoRepository;
    }

    @Override
    public TripoModel buscarPorTaskId(String taskId) {
        return TripoEntity.convertToModel(this.iJpatripoRepository.findByTaskId(taskId));
    }

    @Override
    public Map<String, Object> requestUploadImagenToTripo(String url) throws ErrorReadJsonException, ErrorUploadImageToTripoException, ErroBytesException, FileEmptyException {
        MultipartFile imageFile = convertImageUrlToMultipartFile(url);
        if (imageFile.isEmpty()) throw new FileEmptyException("Archivo vacío.");

        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("imageMultipartFile", imageFile);

        String originalFilename = imageFile.getOriginalFilename();
        checkIfExtensionIsValid(getFileExtension(originalFilename));
        uploadResult.put("originalFilename", originalFilename);
        uploadResult.put("fileExtension", getFileExtension(originalFilename));

        ResponseEntity<String> response = generateRequestToUploadImageToTripo(imageFile, originalFilename);
        checkIfResponseIsOk(response);
        uploadResult.put("imageToken", tryGetImageToken(response));

        return uploadResult;
    }

    @Override
    public String requestGenerateGlbToTripo(Map<String, Object> uploadData) throws ErrorReadJsonException, ErrorGenerateGlbException {
        HttpHeaders taskHeaders = getHttpHeaders(MediaType.APPLICATION_JSON);
        Map<String, Object> bodyMap = getHttpBodyImageToModelTripo(uploadData);
        ObjectMapper mapper = new ObjectMapper();
        String taskBody = tryGetTaskBody(mapper, bodyMap);

        HttpEntity<String> taskEntity = new HttpEntity<>(taskBody, taskHeaders);
        ResponseEntity<String> taskResponse = restTemplate.postForEntity(taskUrl, taskEntity, String.class);
        checkIfStatusResponseIsOk(taskResponse);

        return tryGetTaskIdFromResponse(mapper, taskResponse);
    }

    @Override
    public TripoModel save(TripoModel tripoModel) {
        return TripoEntity.convertToModel(this.iJpatripoRepository.save(TripoEntity.convertToEntity(tripoModel)));
    }

    @Override
    public TripoModel update(TripoModel model) throws ErrorTripoEntityNotFoundException {
        TripoEntity entity = this.iJpatripoRepository.findByTaskId(model.getTaskId());
        if (entity == null) throw new ErrorTripoEntityNotFoundException("No encontramos un registro Tripo con el taskId: " + model.getTaskId());

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
    public String requestStatusGlbTripo(String taskId) throws ErrorReadJsonException, ErrorWhenSleepException, ErrorGlbGenerateTimeExpiredException, ErrorGenerateGlbException {
        JsonNode statusJson;

        HttpEntity<Void> entityWithTaskHeaders = new HttpEntity<>(getHttpHeaders(MediaType.APPLICATION_JSON));
        ResponseEntity<String> statusResponse;

        for (int i = 0; i < 20; i++) { // acá espero 3 y pico min para ver si ya me generó el glb
            try{
                Thread.sleep(10000);

                statusResponse = requestTripoTaskStatus(taskId, entityWithTaskHeaders);
                statusJson = mapper.readTree(statusResponse.getBody());
            } catch (InterruptedException e) {
                throw new ErrorWhenSleepException("Hubo un error al esperar a la siguiente petición del GLB: " + e.getMessage());
            } catch (JsonProcessingException e) {
                throw new ErrorReadJsonException("Hubo un error al leer la respuesta del modelo GLB de Tripo: " + e.getMessage());
            }

            String status = statusJson.path("data").get("status").asText();
            log.info("EL ESTADO ACTUAL ES: {}", status);

            if (status.equalsIgnoreCase("success")) {
                return statusJson.path("data").get("result").get("pbr_model").get("url").asText();
            } else if (status.equalsIgnoreCase("failed")) {
                throw new ErrorGenerateGlbException("La tarea falló: " + statusResponse.getBody());
            }
        }

        throw new ErrorGlbGenerateTimeExpiredException("Se agotó el tiempo de espera para la generación del GLB. El estatus sigue pendiente.");
    }


    //---------------------------------- private methods ------------------------------------------------------------------------
    private void checkIfExtensionIsValid(String extension) {
        if (!validateExtension(extension)) {
            throw new ImageInvalidFormatException("Formato de imagen no válido. Solo se aceptan JPG, JPEG, PNG y WEBP.");
        }
    }

    private MultipartFile convertImageUrlToMultipartFile(String imageUrl) throws ErrorUploadImageToTripoException {
        InputStream inputStream = null;
        try {
            URL url = new URL(imageUrl);
            inputStream = url.openStream();

            return new MockMultipartFile(
                    "image",
                    "imagen.png",
                    "image/png",
                    inputStream
            );

        } catch (IOException e) {
            throw new ErrorUploadImageToTripoException("Hubo un error al convertir la URL en una imagen. Error: " + e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("Error al cerrar el InputStream: {}", e.getMessage());
                }
            }
        }
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

    private boolean validateExtension(String extension) {
        if (extension == null || extension.isEmpty()) return false;
        return extension.equals("jpg") ||
                extension.equals("jpeg") ||
                extension.equals("png") ||
                extension.equals("webp");
    }

    private String tryGetTaskBody(ObjectMapper mapper, Map<String, Object> bodyMap) throws ErrorReadJsonException {
        String taskBody;
        try{
            taskBody = mapper.writeValueAsString(bodyMap);
        }catch (JsonProcessingException e){
            throw new ErrorReadJsonException("Error al leer el Json: " + e.getMessage());
        }
        return taskBody;
    }

    private String tryGetTaskIdFromResponse(ObjectMapper mapper, ResponseEntity<String> taskResponse) throws ErrorReadJsonException {
        try{
            JsonNode taskJson = mapper.readTree(taskResponse.getBody());
            return taskJson.get("data").get("task_id").asText();
        }catch (JsonProcessingException e){
            throw new ErrorReadJsonException("Error al leer el Json: " + taskResponse.getBody() + "--- Error: " + e.getMessage());
        }
    }

    private void checkIfStatusResponseIsOk(ResponseEntity<String> taskResponse) throws ErrorGenerateGlbException {
        if (!taskResponse.getStatusCode().is2xxSuccessful()) {
            throw new ErrorGenerateGlbException("Error al crear task para generar el GLB: " + taskResponse.getBody());
        }
    }

    private Map<String, Object> getHttpBodyImageToModelTripo(Map<String, Object> uploadData) {
        Map<String, String> fileMap = new HashMap<>();
        fileMap.put("type", (String) uploadData.get("fileExtension"));
        fileMap.put("file_token", (String) uploadData.get("imageToken"));

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("type", "image_to_model");
        bodyMap.put("texture", true);
        bodyMap.put("pbr", true);
        bodyMap.put("texture_quality", "detailed");
        bodyMap.put("texture_alignment", "original_image");
        bodyMap.put("face_limit", 50000);
        bodyMap.put("auto_size", true);
        bodyMap.put("orientation", "align_image");
        bodyMap.put("file", fileMap);
        return bodyMap;
    }

    private HttpHeaders getHttpHeaders(MediaType type) {
        HttpHeaders taskHeaders = new HttpHeaders();
        taskHeaders.setContentType(type);
        taskHeaders.setBearerAuth(tripoApiKey);
        return taskHeaders;
    }

    private ByteArrayResource tryGetByteArrayResourceFromImage(MultipartFile imageFile, String originalFilename) throws ErroBytesException {
        byte[] imageBytes = tryGetBytesFromImage(imageFile);

        return new ByteArrayResource(imageBytes) {
            @Override
            public String getFilename() {
                return originalFilename;
            }
        };
    }

    private byte[] tryGetBytesFromImage(MultipartFile imageFile) throws ErroBytesException {
        byte[] imageBytes;
        try{
            imageBytes = imageFile.getBytes();
        }catch (IOException e){
            throw new ErroBytesException("Hubo un error al obtener los bytes de la imagen.");
        }
        return imageBytes;
    }

    private static void checkIfResponseIsOk(ResponseEntity<String> response) throws ErrorUploadImageToTripoException {
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ErrorUploadImageToTripoException("Error al subir imagen a Trippo: " + response.getBody());
        }
    }

    private ResponseEntity<String> generateRequestToUploadImageToTripo(MultipartFile imageFile, String originalFilename) throws ErroBytesException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", tryGetByteArrayResourceFromImage(imageFile, originalFilename));
        HttpHeaders headers = getHttpHeaders(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        return restTemplate.postForEntity(uploadUrl, requestEntity, String.class);
    }

    private String tryGetImageToken(ResponseEntity<String> response) throws ErrorReadJsonException {
        String imageToken = "";
        try{
            JsonNode json = mapper.readTree(response.getBody());
            imageToken = json.get("data").get("image_token").asText();
        }catch (JsonProcessingException e){
            throw new ErrorReadJsonException("Error al leer el Json: " + response.getBody() + "--- Error: " + e.getMessage());
        }
        return imageToken;
    }

    private ResponseEntity<String> requestTripoTaskStatus(String taskId, HttpEntity<Void> entityWithTaskHeaders) {
        return restTemplate.exchange(
                taskUrl + "/" + taskId,
                HttpMethod.GET,
                entityWithTaskHeaders,
                String.class);
    }
}