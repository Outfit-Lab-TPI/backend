package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.exceptions.FashnApiException;
import com.outfitlab.project.domain.exceptions.PredictionFailedException;
import com.outfitlab.project.domain.exceptions.PredictionTimeoutException;
import com.outfitlab.project.domain.interfaces.repositories.FashnRepository;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.presentation.dto.FashnResponse;
import com.outfitlab.project.presentation.dto.StatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class FashnRepositoryImpl implements FashnRepository {

    @Value("${FASHION_IA_SECRET_KEY}")
    private String FASH_API_KEY;

    private final String AVATAR_MAN = "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/models_images/men-with-jean-menmodel.jpg";
    private final String AVATAR_WOMAN = "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/models_images/avatar-mujer-real-grande.png";
    private final String MODE = "balanced";
    private final String GARMENT_PHOTO_TYPE = "flat-lay";
    private final int MAX_ATTEMPS = 20;
    private final int DELAY = 3000;

    private final String BASE_URL = "https://api.fashn.ai/v1";
    private final RestTemplate restTemplate;

    public FashnRepositoryImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String combine(String garmentUrl, String category, String avatarType, UserDetails user) throws FashnApiException {
        return getFashnCombineId(new HttpEntity<>(buildHttpBody(garmentUrl, category, avatarType, user), buildHttpHeaders()));
    }

    private String getFashnCombineId(HttpEntity<Map<String, Object>> httpEntity) throws FashnApiException {
        ResponseEntity<FashnResponse> response = getFashnResponse(httpEntity);

        if (isResponseOk(response)) throw new FashnApiException("Respuesta inesperada de /run: " + response.getStatusCode());
        if (isNullBody(response.getBody())) throw new FashnApiException("Respuesta /run no contiene el id o el body es nulo: " + response);

        return response.getBody().getId();
    }

    @Override
    public String pollStatus(String id) throws FashnApiException, PredictionFailedException {
        String urlTemplate = BASE_URL + "/status/{id}";
        HttpEntity<Void> httpEntity = new HttpEntity<>(buildHttpHeaders());

        System.out.println("---------------- Iniciando polling de status para Task ID: " + id);

        for (int attempt = 1; attempt <= MAX_ATTEMPS; attempt++) {
            waitDelay(attempt);

            ResponseEntity<StatusResponse> responseResult = pollFashnResult(id, urlTemplate, httpEntity);;
            checkIsNotOkStatus(responseResult);
            StatusResponse statusResponseBody = responseResult.getBody();
            checkIfIsNullStatus(statusResponseBody);

            System.out.println("---------------------- Poll intento - Estado actual: {}" + attempt + "  ..Status: " + statusResponseBody.getStatus());

            if (isCompleted(statusResponseBody)) return getUrlGarmentCombined(statusResponseBody);
            else if (isFailed(statusResponseBody)) throw new PredictionFailedException("Predicción falló: " + statusResponseBody.getError());
        }

        System.out.println("---------------- Se agotaron los intentos de polling para la predicción: " + id);
        throw new PredictionTimeoutException("Se agotaron los intentos de polling para la predicción " + id);
    }

    @Override
    public String combineTopAndBottom(String top, String bottom, String avatarType, UserDetails user) throws PredictionFailedException, FashnApiException {
        return pollStatus(combineSecondGarment(bottom, "bottoms", pollStatus(combine(top, "tops", avatarType, user))));
    }

    @Override
    public String combineSecondGarment(String garmentUrl, String category, String avatarCombinedUrl) throws FashnApiException {
        return getFashnCombineId(new HttpEntity<>(buildHttpBodySecondGarment(garmentUrl, category, avatarCombinedUrl), buildHttpHeaders()));
    }

    @NotNull
    private ResponseEntity<FashnResponse> getFashnResponse(HttpEntity<Map<String, Object>> httpEntity) throws FashnApiException {
        String url = BASE_URL + "/run";
        ResponseEntity<FashnResponse> response;

        try {
            response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, FashnResponse.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new FashnApiException("------------ Error al invocar /run de FASHN: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (ResourceAccessException e) {
            throw new FashnApiException("Error de conexión al API de FASHN", e);
        }
        return response;
    }

    private static boolean isResponseOk(ResponseEntity<FashnResponse> response) {
        return response.getStatusCode() != HttpStatus.OK;
    }

    private static boolean isNullBody(FashnResponse body) {
        return body == null || body.getId() == null;
    }

    @NotNull
    private Map<String, Object> buildHttpBody(String garmentUrl, String category, String avatarType, UserDetails user) throws FashnApiException {
        Map<String, Object> body = new HashMap<>();
        body.put("model_name", "tryon-v1.6");

        Map<String, String> inputs = new HashMap<>();
        inputs.put("model_image", getAvatarUrl(avatarType, user));
        inputs.put("garment_image", garmentUrl);
        inputs.put("garment_photo_type", GARMENT_PHOTO_TYPE);
        inputs.put("moderation_level", "none");
        inputs.put("mode", MODE);
        inputs.put("category", category);
        inputs.put("num_samples", "1");
        body.put("inputs", inputs);
        return body;
    }

    @NotNull
    private Map<String, Object> buildHttpBodySecondGarment(String garmentUrl, String category, String avatarCombinedUrl) {
        Map<String, Object> body = new HashMap<>();
        body.put("model_name", "tryon-v1.6");

        Map<String, String> inputs = new HashMap<>();
        inputs.put("model_image", avatarCombinedUrl);
        inputs.put("garment_image", garmentUrl);
        inputs.put("garment_photo_type", GARMENT_PHOTO_TYPE);
        inputs.put("moderation_level", "none");
        inputs.put("mode", MODE);
        inputs.put("category", category);
        inputs.put("num_samples", "1");
        body.put("inputs", inputs);
        return body;
    }

    @NotNull
    private HttpHeaders buildHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(FASH_API_KEY);
        return headers;
    }

    @NotNull
    private ResponseEntity<StatusResponse> pollFashnResult(String id, String urlTemplate, HttpEntity<Void> httpEntity) throws FashnApiException {
        ResponseEntity<StatusResponse> resp;
        try {
            resp = restTemplate.exchange(urlTemplate, HttpMethod.GET, httpEntity, StatusResponse.class, id);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            String msg = "Error al consultar /status de FASHN: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString();
            throw new FashnApiException(msg, ex);
        } catch (ResourceAccessException ex) {
            throw new FashnApiException("Error de conexión al consultar status", ex);
        }
        return resp;
    }

    private boolean isFailed(StatusResponse statusResponseBody) {
        return "failed".equalsIgnoreCase(statusResponseBody.getStatus());
    }

    private boolean isCompleted(StatusResponse statusResponseBody) {
        return "completed".equalsIgnoreCase(statusResponseBody.getStatus());
    }

    private String getUrlGarmentCombined(StatusResponse statusResponseBody) throws PredictionFailedException {
        List<String> outputs = statusResponseBody.getOutput();
        if (outputs != null && !outputs.isEmpty()) {
            return outputs.get(0);
        } else {
            throw new PredictionFailedException("Estado completed pero no hay output: " + statusResponseBody.getError());
        }
    }

    private void checkIfIsNullStatus(StatusResponse statusResp) throws FashnApiException {
        if (statusResp == null) {
            throw new FashnApiException("Respuesta /status vacía");
        }
    }

    private void checkIsNotOkStatus(ResponseEntity<StatusResponse> responseResult) throws FashnApiException {
        if (responseResult.getStatusCode() != HttpStatus.OK) {
            throw new FashnApiException("Respuesta inesperada de /status: " + responseResult.getStatusCode());
        }
    }

    private void waitDelay(int attempt) throws FashnApiException {
        try {
            if (attempt > 1) {
                Thread.sleep(DELAY);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new FashnApiException("Polling interrumpido, falló la espera del Sleep", e);
        }
    }

    private String getAvatarUrl(String avatarType, UserDetails user) throws FashnApiException {
        return switch (avatarType.toUpperCase()) {
            case "MAN" -> AVATAR_MAN;
            case "WOMAN" -> AVATAR_WOMAN;
            case "CUSTOM" -> getUserAvatarUrl(user);
            default -> throw new FashnApiException("Tipo de avatar inválido: " + avatarType + ". Use 'MAN', 'WOMAN' o 'CUSTOM'.");
        };
    }

    private String getUserAvatarUrl(UserDetails user) {
        String userAvatarUrl = null;
        if (user instanceof UserEntity) {
            userAvatarUrl = ((UserEntity) user).getUserImageUrl();
        }
        if (userAvatarUrl == null || userAvatarUrl.isEmpty()) {
            return AVATAR_MAN;
        }
        return userAvatarUrl;
    }

}
