package com.outfitlab.project.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/garments")
public class GarmentListController {

    private static final String MODELS_DIR = "../frontend/public/tripo-models";

    @GetMapping("/list-models")
    public ResponseEntity<List<Map<String, Object>>> listModels() {
        try {
            Path modelsPath = Paths.get(MODELS_DIR);
            
            if (!Files.exists(modelsPath)) {
                return ResponseEntity.ok(Collections.emptyList());
            }

            List<Map<String, Object>> models = Files.list(modelsPath)
                .filter(path -> path.toString().endsWith(".glb"))
                .map(path -> {
                    try {
                        Map<String, Object> modelInfo = new HashMap<>();
                        String filename = path.getFileName().toString();
                        
                        // Extraer task ID del nombre del archivo
                        String taskId = filename.replace("model-", "").replace(".glb", "");
                        
                        BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
                        
                        modelInfo.put("name", filename);
                        modelInfo.put("path", "/tripo-models/" + filename);
                        modelInfo.put("taskId", taskId);
                        modelInfo.put("createdAt", attrs.creationTime().toMillis());
                        
                        return modelInfo;
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .sorted((a, b) -> Long.compare(
                    (Long) b.get("createdAt"), 
                    (Long) a.get("createdAt")
                ))
                .collect(Collectors.toList());

            return ResponseEntity.ok(models);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}