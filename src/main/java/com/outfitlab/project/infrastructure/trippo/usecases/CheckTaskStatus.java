package com.outfitlab.project.infrastructure.trippo.usecases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class CheckTaskStatus {
    @Value("${tripo.api.key}")
    private String tripoApiKey;
    private String taskUrl = "https://api.tripo3d.ai/v2/openapi/task";
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper = new ObjectMapper();
    public Map<String, String> checkTaskStatus(String taskId) throws JsonProcessingException, InterruptedException {
        Map<String, String> taskStatus = new HashMap<>();
        String glbUrl = null;
        String webpUrl = null;

        for (int i = 0; i < 30; i++) { // acá espero 1 min para ver si ya me generó el glb
            Thread.sleep(2000);

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
}
