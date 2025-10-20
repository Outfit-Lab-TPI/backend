package com.outfitlab.project.infrastructure.trippo.usecases;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class SaveFilesFromTask {
    public Map<String, String> saveFilesFromTask(Map<String, String> taskResponse) throws IOException {
        // cuando tengamos el AWS, la idea es que estas url las bajemos y enviemos esos archivos a nuestro AWS y luego obtener
        // la URL de ESOS archivos recién alojados en nuestro AWS y enviarlas al front así pueden usarlas para mostrarlos.
        // (ya que las url de trippo no nos deja usarla en el front)

        String imageUrl = taskResponse.get("webpUrl");
        String modelUrl = taskResponse.get("glbUrl");
        String taskId = taskResponse.get("taskId");

        if (imageUrl == null || modelUrl == null || taskId == null) {
            throw new IllegalArgumentException("Faltan datos en el mapa");
        }

        Path resourcesPath = Paths.get("src/main/resources");
        Map<String, String> localPaths = new HashMap<>();

        Path imagePath = resourcesPath.resolve(taskId + "_image.webp");
        try (InputStream in = new URL(imageUrl).openStream()) {
            Files.copy(in, imagePath, StandardCopyOption.REPLACE_EXISTING);
        }
        localPaths.put("image", "resources/" + imagePath.getFileName());

        Path modelPath = resourcesPath.resolve(taskId + "_3d.glb");
        try (InputStream in = new URL(modelUrl).openStream()) {
            Files.copy(in, modelPath, StandardCopyOption.REPLACE_EXISTING);
        }
        localPaths.put("glb", "resources/" + modelPath.getFileName());

        return localPaths;
    }
}
