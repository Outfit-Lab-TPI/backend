package com.outfitlab.project.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName = System.getenv("AWS_BUCKET_NAME");

    public String uploadFile(MultipartFile file, String folder) {
        try {
            String key = folder + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .contentType(file.getContentType())
                            .acl(ObjectCannedACL.PUBLIC_READ)
                            .build(),
                    RequestBody.fromBytes(file.getBytes()));

            String url = String.format("https://%s.s3.%s.amazonaws.com/%s",
                    bucketName, System.getenv("AWS_REGION"), key);

            log.info("Archivo subido a S3: {}", url);
            return url;

        } catch (Exception e) {
            log.error("Error subiendo archivo a S3: {}", e.getMessage());
            throw new RuntimeException("Error al subir archivo a S3", e);
        }
    }

    public void deleteFile(String key) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build());
            log.info("Archivo eliminado de S3: {}", key);
        } catch (Exception e) {
            log.error("Error eliminando archivo de S3: {}", e.getMessage());
            throw new RuntimeException("Error al eliminar archivo de S3", e);
        }
    }
}
