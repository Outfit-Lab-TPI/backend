package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.exceptions.FashnApiException;
import com.outfitlab.project.domain.exceptions.PredictionFailedException;
import com.outfitlab.project.domain.exceptions.PredictionTimeoutException;
import com.outfitlab.project.domain.interfaces.repositories.IFashnRepository;
import com.outfitlab.project.presentation.dto.CombineRequest;
import com.outfitlab.project.presentation.dto.RunResponse;
import com.outfitlab.project.presentation.dto.StatusResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FashnRepositoryImpl implements IFashnRepository {

    @Value("${FASHION_IA_SECRET_KEY}")
    private String fashnApiKey;

    private final String BASE_URL = "https://api.fashn.ai/v1";
    private final RestTemplate restTemplate;

    public FashnRepositoryImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public String combine(CombineRequest req, String category, String avatarUrl) throws FashnApiException {
        String url = BASE_URL + "/run";

        String mode = "balanced";
        String garmentPhotoType = "flat-lay";
        String prenda = !req.getInferior().isEmpty() ? req.getInferior() : req.getSuperior();

        System.out.println("-------------- Enviando request a FASHN /run con baseImage='{}' y garment='{}'" +  avatarUrl + " ----  prenda: " + prenda);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(fashnApiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model_name", "tryon-v1.6");

        Map<String, String> inputs = new HashMap<>();
        inputs.put("model_image", avatarUrl);
        inputs.put("garment_image", prenda);
        inputs.put("garment_photo_type", garmentPhotoType);
        inputs.put("moderation_level", "none");
        inputs.put("mode", mode);
        inputs.put("category", category);
        inputs.put("num_samples", "1");
        body.put("inputs", inputs);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(body, headers);
        ResponseEntity<RunResponse> resp;

        try {
            resp = restTemplate.exchange(url, HttpMethod.POST, httpEntity, RunResponse.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new FashnApiException("------------ Error al invocar /run de FASHN: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (ResourceAccessException e) {
            throw new FashnApiException("Error de conexión al API de FASHN", e);
        }

        if (resp.getStatusCode() != HttpStatus.OK) {
            throw new FashnApiException("Respuesta inesperada de /run: " + resp.getStatusCode());
        }

        RunResponse runResp = resp.getBody();
        if (runResp == null || runResp.getId() == null) {
            throw new FashnApiException("Respuesta /run no contiene id: " + (runResp != null ? runResp.getError() : "null body"));
        }

        System.out.println(" GOOOOD ------------- Request /run enviada correctamente. Task ID: {}" + runResp.getId());
        return runResp.getId();
    }

    @Override
    public String pollStatus(String id, int maxIntentos, long delay) throws FashnApiException, PredictionFailedException {
        String urlTemplate = BASE_URL + "/status/{id}";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(fashnApiKey);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        System.out.println("---------------- Iniciando polling de status para Task ID: " + id);

        for (int attempt = 1; attempt <= maxIntentos; attempt++) {
            try {
                if (attempt > 1) {
                    Thread.sleep(delay);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new FashnApiException("Polling interrumpido, falló la espera del Sleep", e);
            }

            ResponseEntity<StatusResponse> resp;
            try {
                resp = restTemplate.exchange(urlTemplate, HttpMethod.GET, httpEntity, StatusResponse.class, id);
            } catch (HttpClientErrorException | HttpServerErrorException ex) {
                String msg = "Error al consultar /status de FASHN: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString();
                throw new FashnApiException(msg, ex);
            } catch (ResourceAccessException ex) {
                throw new FashnApiException("Error de conexión al consultar status", ex);
            }

            if (resp.getStatusCode() != HttpStatus.OK) {
                throw new FashnApiException("Respuesta inesperada de /status: " + resp.getStatusCode());
            }

            StatusResponse statusResp = resp.getBody();
            if (statusResp == null) {
                throw new FashnApiException("Respuesta /status vacía");
            }

            String status = statusResp.getStatus();
            System.out.println("---------------------- Poll intento - Estado actual: {}" + attempt + "  ..Status: " + status);

            if ("completed".equalsIgnoreCase(status)) {
                List<String> outputs = statusResp.getOutput();
                if (outputs != null && !outputs.isEmpty()) {
                    return outputs.get(0);
                } else {
                    throw new PredictionFailedException("Estado completed pero no hay output: " + statusResp.getError());
                }
            } else if ("failed".equalsIgnoreCase(status)) {
                throw new PredictionFailedException("Predicción falló: " + statusResp.getError());
            }
        }

        System.out.println("---------------- Se agotaron los intentos de polling para la predicción: " + id);
        throw new PredictionTimeoutException("Se agotaron los intentos de polling para la predicción " + id);
    }
}
