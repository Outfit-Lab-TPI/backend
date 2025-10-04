package com.outfitlab.project.infrastructure;

import com.outfitlab.project.domain.dto.TripoCreateTaskResponse;
import com.outfitlab.project.domain.dto.TripoTaskStatusResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TripoService {

    private final WebClient webClient;

    @Value("${tripo.api.key:your_api_key_here}")
    private String apiKey;

    public TripoService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.tripo3d.ai/v2/openapi")
                .build();
    }

    public Mono<TripoCreateTaskResponse> createTextToModelTask(String prompt) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("type", "text_to_model");
        requestBody.put("prompt", prompt);

        return webClient.post()
                .uri("/task")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(TripoCreateTaskResponse.class);
    }

    public Mono<TripoCreateTaskResponse> createImageToModelTask(String imageUrl) {
        Map<String, Object> fileInfo = new HashMap<>();
        fileInfo.put("type", "url");
        fileInfo.put("file_url", imageUrl);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("type", "image_to_model");
        requestBody.put("file", fileInfo);

        return webClient.post()
                .uri("/task")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(TripoCreateTaskResponse.class);
    }

public Mono<TripoCreateTaskResponse> createMultiviewToModelTask(List<String> imageUrls) {
    // Tripo requiere exactamente 4 elementos: [front, left, back, right]
    // Si no tenemos las 4, rellenamos con objetos vacíos
    
    List<Map<String, String>> files = new ArrayList<>();
    
    // Front (obligatorio) - índice 0
    if (imageUrls.size() > 0) {
        Map<String, String> front = new HashMap<>();
        front.put("type", "jpg");
        front.put("url", imageUrls.get(0));
        files.add(front);
    } else {
        throw new RuntimeException("Front image is required");
    }
    
    // Left (opcional) - índice 1
    if (imageUrls.size() > 1) {
        // Si el usuario subió una segunda imagen, asumimos que es "back" no "left"
        // Así que dejamos "left" vacío
        files.add(new HashMap<>()); // Empty object
    } else {
        files.add(new HashMap<>());
    }
    
    // Back (opcional) - índice 2
    if (imageUrls.size() > 1) {
        Map<String, String> back = new HashMap<>();
        back.put("type", "png");
        back.put("url", imageUrls.get(1));
        files.add(back);
    } else {
        files.add(new HashMap<>());
    }
    
    // Right (opcional) - índice 3
    if (imageUrls.size() > 2) {
        Map<String, String> right = new HashMap<>();
        right.put("type", "jpg");
        right.put("url", imageUrls.get(2));
        files.add(right);
    } else {
        files.add(new HashMap<>());
    }

    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("type", "multiview_to_model");
    requestBody.put("files", files);
    requestBody.put("model_version", "v2.5-20250123");

    System.out.println("Sending to Tripo: " + requestBody);

    return webClient.post()
            .uri("/task")
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(TripoCreateTaskResponse.class);
}

    public Mono<TripoTaskStatusResponse> getTaskStatus(String taskId) {
        return webClient.get()
                .uri("/task/" + taskId)
                .header("Authorization", "Bearer " + apiKey)
                .retrieve()
                .bodyToMono(TripoTaskStatusResponse.class);
    }
}