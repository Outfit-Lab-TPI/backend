package com.outfitlab.project.infrastructure;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ModelDownloadService {

    private final RestTemplate restTemplate;
    private static final String MODELS_DIR = "../frontend/public/tripo-models";

    public ModelDownloadService() {
        this.restTemplate = new RestTemplate();
    }

    public String downloadAndSaveModel(String modelUrl, String taskId) throws IOException {
        // Crear directorio si no existe
        Path modelsPath = Paths.get(MODELS_DIR);
        if (!Files.exists(modelsPath)) {
            Files.createDirectories(modelsPath);
        }

        // Descargar modelo
        byte[] modelData = restTemplate.getForObject(modelUrl, byte[].class);

        // Guardar con nombre único
        String filename = "model-" + taskId + ".glb";
        Path filePath = modelsPath.resolve(filename);

        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            fos.write(modelData);
        }

        System.out.println("Modelo guardado en: " + filePath.toAbsolutePath());
        return "/tripo-models/" + filename;
    }
}