package com.outfitlab.project.infrastructure;

import com.outfitlab.project.domain.exceptions.fashion.FashnApiException;
import com.outfitlab.project.domain.exceptions.fashion.PredictionFailedException;
import com.outfitlab.project.domain.exceptions.fashion.PredictionTimeoutException;
import com.outfitlab.project.presentation.dto.CombineRequest;
import com.outfitlab.project.presentation.dto.RunResponse;
import com.outfitlab.project.presentation.dto.StatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.util.*;

@Service
public class TryOnService {

    private static final Logger logger = LoggerFactory.getLogger(TryOnService.class);

    @Value("${FASHION_IA_SECRET_KEY}")
    private String fashnApiKey;

    private final RestTemplate restTemplate;

    private final String BASE_URL = "https://api.fashn.ai/v1";

    private final String AVATAR_HOMBRE = "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/models_images/men-with-jean-menmodel.jpg";
    private final String AVATAR_MUJER = "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/models_images/avatar-mujer-real-grande.png";

    public TryOnService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String combine(CombineRequest req) {
        String avatarUrl = req.getEsHombre() ? AVATAR_HOMBRE : AVATAR_MUJER;
        String superior = req.getSuperior();
        String inferior = req.getInferior();
        String category = "auto";
        String mode = "balanced";
        String garmentPhotoType = "flat-lay";


        logger.info("🔹 Iniciando combinación de prendas...");
        logger.info("🧍 Avatar seleccionado: {}", avatarUrl);
        logger.info("👕 URL prenda superior: {}", superior);
        logger.info("👖 URL prenda inferior: {}", inferior);

        if ((superior == null || superior.isBlank()) && (inferior == null || inferior.isBlank())) {
            throw new FashnApiException("Debe proporcionarse al menos una prenda (superior o inferior).");
        }

        try {
            if (superior != null && (inferior == null || inferior.isBlank())) {
                logger.info("🪄 Combinando solo prenda superior...");
                category = "tops";
                String id = callRun(avatarUrl, superior, category, garmentPhotoType, mode);
                String result = pollStatusUntilComplete(id);
                logger.info("✅ Combinación de prenda superior completada correctamente.");
                return result;
            }

            if (inferior != null && (superior == null || superior.isBlank())) {
                logger.info("🪄 Combinando solo prenda inferior...");
                category = "bottoms";
                String id = callRun(avatarUrl, inferior, category, garmentPhotoType, mode);
                String result = pollStatusUntilComplete(id);
                logger.info("✅ Combinación de prenda inferior completada correctamente.");
                return result;
            }

            logger.info("🪄 Combinando prenda superior primero...");
            category = "tops";
            String firstPredictionId = callRun(avatarUrl, superior, category, garmentPhotoType, mode);
            String firstResultUrl = pollStatusUntilComplete(firstPredictionId);
            logger.info("✅ Primera combinación completada. Resultado intermedio: {}", firstResultUrl);

            category = "bottoms";
            logger.info("🪄 Ahora combinando resultado intermedio con prenda inferior...");
            String secondPredictionId = callRun(firstResultUrl, inferior, category, garmentPhotoType, mode);
            String finalResult = pollStatusUntilComplete(secondPredictionId);
            logger.info("✅ Combinación completa finalizada correctamente.");

            return finalResult;

        } catch (FashnApiException | PredictionFailedException | PredictionTimeoutException ex) {
            logger.error("❌ Error durante la combinación de prendas: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("⚠️ Error inesperado al combinar prendas: {}", ex.getMessage(), ex);
            throw new FashnApiException("Error inesperado al combinar prendas: " + ex.getMessage(), ex);
        }
    }

    private String callRun(String baseImageUrl, String garmentUrl, String category, String garmentPhotoType, String mode) {
        logger.debug("📤 Enviando request a FASHN /run con baseImage='{}' y garment='{}'", baseImageUrl, garmentUrl);

        String url = BASE_URL + "/run";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(fashnApiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model_name", "tryon-v1.6");

        Map<String, String> inputs = new HashMap<>();
        inputs.put("model_image", baseImageUrl);
        inputs.put("garment_image", garmentUrl);
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
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            String msg = "Error al invocar /run de FASHN: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString();
            logger.error("❌ {}", msg);
            throw new FashnApiException(msg, ex);
        } catch (ResourceAccessException ex) {
            logger.error("❌ Error de conexión al API de FASHN: {}", ex.getMessage());
            throw new FashnApiException("Error de conexión al API de FASHN", ex);
        }

        if (resp.getStatusCode() != HttpStatus.OK) {
            logger.error("❌ Respuesta inesperada de /run: {}", resp.getStatusCode());
            throw new FashnApiException("Respuesta inesperada de /run: " + resp.getStatusCode());
        }

        RunResponse runResp = resp.getBody();
        if (runResp == null || runResp.getId() == null) {
            logger.error("❌ Respuesta /run sin id: {}", runResp != null ? runResp.getError() : "null body");
            throw new FashnApiException("Respuesta /run no contiene id: " + (runResp != null ? runResp.getError() : "null body"));
        }

        logger.info("✅ Request /run enviada correctamente. Task ID: {}", runResp.getId());
        return runResp.getId();
    }

    private String pollStatusUntilComplete(String predictionId) {
        String urlTemplate = BASE_URL + "/status/{id}";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(fashnApiKey);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        int maxAttempts = 20;
        long delayMillis = 3000;

        logger.info("⏳ Iniciando polling de status para Task ID: {}", predictionId);

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                if (attempt > 1) {
                    Thread.sleep(delayMillis);
                }
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                logger.error("⚠️ Polling interrumpido para Task ID {}", predictionId);
                throw new FashnApiException("Polling interrumpido", ie);
            }

            ResponseEntity<StatusResponse> resp;
            try {
                resp = restTemplate.exchange(urlTemplate, HttpMethod.GET, httpEntity, StatusResponse.class, predictionId);
            } catch (HttpClientErrorException | HttpServerErrorException ex) {
                String msg = "Error al consultar /status de FASHN: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString();
                logger.error("❌ {}", msg);
                throw new FashnApiException(msg, ex);
            } catch (ResourceAccessException ex) {
                logger.error("❌ Error de conexión al consultar status: {}", ex.getMessage());
                throw new FashnApiException("Error de conexión al consultar status", ex);
            }

            if (resp.getStatusCode() != HttpStatus.OK) {
                logger.error("❌ Respuesta inesperada de /status: {}", resp.getStatusCode());
                throw new FashnApiException("Respuesta inesperada de /status: " + resp.getStatusCode());
            }

            StatusResponse statusResp = resp.getBody();
            if (statusResp == null) {
                logger.error("❌ Respuesta /status vacía");
                throw new FashnApiException("Respuesta /status vacía");
            }

            String status = statusResp.getStatus();
            logger.debug("📡 Poll intento {} - Estado actual: {}", attempt, status);

            if ("completed".equalsIgnoreCase(status)) {
                List<String> outputs = statusResp.getOutput();
                if (outputs != null && !outputs.isEmpty()) {
                    logger.info("✅ Task completada con éxito. Resultado: {}", outputs.get(0));
                    return outputs.get(0);
                } else {
                    logger.error("❌ Estado 'completed' pero sin output válido: {}", statusResp.getError());
                    throw new PredictionFailedException("Estado completed pero no hay output: " + statusResp.getError());
                }
            } else if ("failed".equalsIgnoreCase(status)) {
                logger.error("❌ Predicción falló: {}", statusResp.getError());
                throw new PredictionFailedException("Predicción falló: " + statusResp.getError());
            }
        }

        logger.error("⏱️ Se agotaron los intentos de polling para la predicción {}", predictionId);
        throw new PredictionTimeoutException("Se agotaron los intentos de polling para la predicción " + predictionId);
    }

    public String getCombinationUrl(String nombreCombinacion) {
        System.out.println("Requested combination ****************************************** : " + nombreCombinacion);
        return "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/expo/combinaciones/" + nombreCombinacion + ".png";
        // return "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/Model_Posing_On_Typical_Studio_Set.jpg/250px-Model_Posing_On_Typical_Studio_Set.jpg";
    }
}
