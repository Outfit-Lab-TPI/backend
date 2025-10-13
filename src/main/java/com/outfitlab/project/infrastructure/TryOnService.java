package com.outfitlab.project.infrastructure;

import com.outfitlab.project.domain.exceptions.fashion.FashnApiException;
import com.outfitlab.project.domain.exceptions.fashion.PredictionFailedException;
import com.outfitlab.project.domain.exceptions.fashion.PredictionTimeoutException;
import com.outfitlab.project.presentation.dto.CombineRequest;
import com.outfitlab.project.presentation.dto.RunResponse;
import com.outfitlab.project.presentation.dto.StatusResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.time.Duration;
import java.util.*;

@Service
public class TryOnService {

    @Value("${FASHION_IA_SECRET_KEY}")
    private String fashnApiKey;

    private final RestTemplate restTemplate;

    private final String BASE_URL = "https://api.fashn.ai/v1";

    public TryOnService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String combine(CombineRequest req) {
        String predictionId = callRun(req);
        return pollStatusUntilComplete(predictionId);
    }

    private String callRun(CombineRequest req) {
        String url = BASE_URL + "/run";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(fashnApiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model_name", "tryon-v1.6");
        Map<String, String> inputs = new HashMap<>();
        inputs.put("model_image", req.getAvatarUrl());
        inputs.put("garment_image", req.getGarmentUrl());
        body.put("inputs", inputs);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(body, headers);

        ResponseEntity<RunResponse> resp;
        try {
            resp = restTemplate.exchange(url, HttpMethod.POST, httpEntity, RunResponse.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            String msg = "Error al invocar /run de FASHN: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString();
            throw new FashnApiException(msg, ex);
        } catch (ResourceAccessException ex) {
            throw new FashnApiException("Error de conexión al API de FASHN", ex);
        }

        if (resp.getStatusCode() != HttpStatus.OK) {
            throw new FashnApiException("Respuesta inesperada de /run: " + resp.getStatusCode());
        }

        RunResponse runResp = resp.getBody();
        if (runResp == null || runResp.getId() == null) {
            throw new FashnApiException("Respuesta /run no contiene id: " + (runResp != null ? runResp.getError() : "null body"));
        }

        return runResp.getId();
    }

    private String pollStatusUntilComplete(String predictionId) {
        String urlTemplate = BASE_URL + "/status/{id}";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(fashnApiKey);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        int maxAttempts = 20;
        long delayMillis = 3000;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                if (attempt > 1) {
                    Thread.sleep(delayMillis);
                }
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new FashnApiException("Polling interrumpido", ie);
            }

            ResponseEntity<StatusResponse> resp;
            try {
                resp = restTemplate.exchange(urlTemplate, HttpMethod.GET, httpEntity, StatusResponse.class, predictionId);
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
            if ("completed".equalsIgnoreCase(status)) {
                List<String> outputs = statusResp.getOutput();
                if (outputs != null && !outputs.isEmpty()) {
                    return outputs.get(0);
                } else {
                    throw new PredictionFailedException("Estado completed pero no hay output: " + statusResp.getError());
                }
            } else if ("failed".equalsIgnoreCase(status)) {
                throw new PredictionFailedException("Predicción falló: " + statusResp.getError());
            } else {
                // este ultimo por si NO se cumple los dos anteriores, podría dejarlo vacío sin el else pero bueno
            }
        }

        throw new PredictionTimeoutException("Se agotaron los intentos de polling para la predicción " + predictionId);
    }
}

