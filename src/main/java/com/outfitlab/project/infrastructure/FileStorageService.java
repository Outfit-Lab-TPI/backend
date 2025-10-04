package com.outfitlab.project.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${upload.temp-dir}")
    private String tempDir;

    @Value("${upload.base-url}")
    private String baseUrl;

    public String storeFile(MultipartFile file) throws IOException {
        // Crear directorio si no existe
        Path uploadPath = Paths.get(tempDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generar nombre único
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null && originalFilename.contains(".") 
            ? originalFilename.substring(originalFilename.lastIndexOf("."))
            : ".jpg";
        
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        // Guardar archivo
        Path targetLocation = uploadPath.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Retornar URL pública
        return baseUrl + "/" + uniqueFileName;
    }

    public void deleteFile(String fileName) throws IOException {
        Path filePath = Paths.get(tempDir).resolve(fileName);
        Files.deleteIfExists(filePath);
    }

    public void cleanupTempFiles() throws IOException {
        Path uploadPath = Paths.get(tempDir);
        if (Files.exists(uploadPath)) {
            Files.walk(uploadPath)
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        System.err.println("Could not delete file: " + path);
                    }
                });
        }
    }
}