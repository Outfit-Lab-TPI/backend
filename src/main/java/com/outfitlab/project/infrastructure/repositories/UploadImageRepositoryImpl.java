package com.outfitlab.project.infrastructure.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class UploadImageRepositoryImpl implements com.outfitlab.project.domain.interfaces.repositories.UploadImageRepository {

    private final S3Client s3Client;

    @Value("${AWS_REGION}")
    private String region;

    @Value("${AWS_BUCKET_NAME}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file, String folder) {
        try {
            String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String key = folder + "/" + UUID.randomUUID() + "." + extension;

            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromBytes(file.getBytes()));

            String url = String.format("https://%s.s3.%s.amazonaws.com/%s",
                    bucketName, region, key);

            log.info("Archivo subido a S3: {}", url);
            return url;

        } catch (Exception e) {
            log.error("Error subiendo archivo a S3: {}", e.getMessage());
            throw new RuntimeException("Error al subir archivo a S3", e);
        }
    }

    @Override
    public void deleteFile(String key) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(this.extractKeyFromUrl(key))
                    .build());
            log.info("Archivo eliminado de S3: {}", key);
        } catch (Exception e) {
            log.error("Error eliminando archivo de S3: {}", e.getMessage());
            throw new RuntimeException("Error al eliminar archivo de S3", e);
        }
    }

    @Override
    public String getFileUrl(String key) {
        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + key;
    }

    private String extractKeyFromUrl(String url) {
        int index = url.indexOf(".amazonaws.com/");
        if (index == -1) {throw new IllegalArgumentException("URL de S3 inválida: " + url);}
        return url.substring(index + ".amazonaws.com/".length());
    }
}
