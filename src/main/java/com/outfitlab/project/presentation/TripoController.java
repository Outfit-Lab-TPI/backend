package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.dto.TripoCreateTaskResponse;
import com.outfitlab.project.domain.dto.TripoTaskStatusResponse;
import com.outfitlab.project.domain.dto.TripoTextToModelRequest;
import com.outfitlab.project.infrastructure.ModelDownloadService;
import com.outfitlab.project.infrastructure.TripoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/tripo")
public class TripoController {

    private final TripoService tripoService;
    private final ModelDownloadService modelDownloadService;

    public TripoController(TripoService tripoService, ModelDownloadService modelDownloadService) {
        this.tripoService = tripoService;
        this.modelDownloadService = modelDownloadService;
    }

    @PostMapping("/text-to-model")
    public Mono<ResponseEntity<TripoCreateTaskResponse>> createTextToModel(
            @RequestBody TripoTextToModelRequest request) {
        
        return tripoService.createTextToModelTask(request.getPrompt())
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }

    @PostMapping("/image-to-model")
    public Mono<ResponseEntity<TripoCreateTaskResponse>> createImageToModel(
            @RequestBody Map<String, String> request) {
        
        String imageUrl = request.get("imageUrl");
        
        return tripoService.createImageToModelTask(imageUrl)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }

    @GetMapping("/task/{taskId}")
    public Mono<ResponseEntity<TripoTaskStatusResponse>> getTaskStatus(
            @PathVariable String taskId) {
        
        return tripoService.getTaskStatus(taskId)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }

    @PostMapping("/download-and-save/{taskId}")
    public Mono<ResponseEntity<Map<String, String>>> downloadAndSaveModel(@PathVariable String taskId) {
        return tripoService.getTaskStatus(taskId)
            .flatMap(statusResponse -> {
                if ("success".equals(statusResponse.getData().getStatus())) {
                    String modelUrl = statusResponse.getData().getOutput().getPbr_model();
                    if (modelUrl == null) {
                        modelUrl = statusResponse.getData().getOutput().getModel();
                    }
                    
                    if (modelUrl != null) {
                        try {
                            String localPath = modelDownloadService.downloadAndSaveModel(modelUrl, taskId);
                            Map<String, String> response = new HashMap<>();
                            response.put("localPath", localPath);
                            response.put("message", "Modelo descargado exitosamente");
                            return Mono.just(ResponseEntity.ok(response));
                        } catch (Exception e) {
                            e.printStackTrace();
                            return Mono.just(ResponseEntity.internalServerError().<Map<String, String>>build());
                        }
                    }
                }
                return Mono.just(ResponseEntity.badRequest().<Map<String, String>>build());
            });
    }
}