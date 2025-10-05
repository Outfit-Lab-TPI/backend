package com.outfitlab.project.infrastructure;

import io.minio.*;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
public class MinioStorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket.images}")
    private String imagesBucket;

    @Value("${minio.bucket.models}")
    private String modelsBucket;

    public MinioStorageService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    /**
     * Sube una imagen a MinIO y retorna la ruta
     */
    public String uploadImage(MultipartFile file) throws IOException {
        String objectName = generateObjectName(file.getOriginalFilename(), "images");
        
        try {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(imagesBucket)
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
            
            log.info("Imagen subida a MinIO: {}/{}", imagesBucket, objectName);
            return objectName;
            
        } catch (Exception e) {
            log.error("Error subiendo imagen a MinIO: {}", e.getMessage());
            throw new IOException("Error al subir imagen a MinIO", e);
        }
    }

    /**
     * Descarga un modelo 3D desde URL y lo guarda en MinIO
     */
    public String uploadModelFromUrl(String modelUrl, String originalFilename) throws IOException {
        // TODO: Implementar descarga desde URL de Tripo3D
        // Por ahora retornamos un placeholder
        String objectName = generateObjectName(originalFilename, "models");
        log.info("Placeholder: Modelo se guardaría en {}/{}", modelsBucket, objectName);
        return objectName;
    }

    /**
     * Sube un modelo desde bytes
     */
    public String uploadModel(byte[] modelData, String filename, String contentType) throws IOException {
        String objectName = generateObjectName(filename, "models");
        
        try (InputStream stream = new ByteArrayInputStream(modelData)) {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(modelsBucket)
                    .object(objectName)
                    .stream(stream, modelData.length, -1)
                    .contentType(contentType)
                    .build()
            );
            
            log.info("Modelo 3D subido a MinIO: {}/{}", modelsBucket, objectName);
            return objectName;
            
        } catch (Exception e) {
            log.error("Error subiendo modelo a MinIO: {}", e.getMessage());
            throw new IOException("Error al subir modelo a MinIO", e);
        }
    }

    /**
     * Obtiene la URL pública de un objeto
     */
    public String getObjectUrl(String bucketName, String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(io.minio.http.Method.GET)
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(60 * 60 * 24 * 7) // 7 días
                    .build()
            );
        } catch (Exception e) {
            log.error("Error generando URL: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Obtiene URL de imagen
     */
    public String getImageUrl(String objectName) {
        return getObjectUrl(imagesBucket, objectName);
    }

    /**
     * Obtiene URL de modelo
     */
    public String getModelUrl(String objectName) {
        return getObjectUrl(modelsBucket, objectName);
    }

    /**
     * Elimina un objeto de MinIO
     */
    public void deleteObject(String bucketName, String objectName) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
            log.info("Objeto eliminado de MinIO: {}/{}", bucketName, objectName);
        } catch (Exception e) {
            log.error("Error eliminando objeto de MinIO: {}", e.getMessage());
        }
    }

    /**
     * Genera un nombre único para el objeto
     */
    private String generateObjectName(String originalFilename, String prefix) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String extension = getFileExtension(originalFilename);
        
        return String.format("%s/%s_%s.%s", prefix, timestamp, uuid, extension);
    }

    /**
     * Extrae la extensión del archivo
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "bin";
        }
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex + 1).toLowerCase();
        }
        return "bin";
    }
}
