package com.outfitlab.project.domain.model;

import java.time.LocalDateTime;

public class TripoModel {

    private String taskId;
    private String imageToken;
    private String originalFilename;
    private String fileExtension;
    private ModelStatus status;
    private String minioImagePath;
    private String minioModelPath;
    private String tripoModelUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String errorMessage;

    public TripoModel(String taskId, String imageToken, String originalFilename, String fileExtension, ModelStatus status, String minioImagePath, String minioModelPath, String tripoModelUrl, LocalDateTime createdAt, LocalDateTime updatedAt, String errorMessage) {
        this.taskId = taskId;
        this.imageToken = imageToken;
        this.originalFilename = originalFilename;
        this.fileExtension = fileExtension;
        this.status = status;
        this.minioImagePath = minioImagePath;
        this.minioModelPath = minioModelPath;
        this.tripoModelUrl = tripoModelUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.errorMessage = errorMessage;
    }

    public TripoModel() {}

    public TripoModel(String taskId, String imageToken, String originalFilename, String fileExtension, String minioImagePath, ModelStatus modelStatus) {
        this.taskId = taskId;
        this.imageToken = imageToken;
        this.originalFilename = originalFilename;
        this.fileExtension = fileExtension;
        this.status = modelStatus;
        this.minioImagePath = minioImagePath;
        this.minioModelPath = minioModelPath;
        this.tripoModelUrl = tripoModelUrl;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public enum ModelStatus {
        PENDING,        // Imagen subida, esperando procesamiento
        PROCESSING,     // Tripo3D está generando el modelo
        COMPLETED,      // Modelo 3D generado exitosamente
        FAILED,         // Falló la generación
        DOWNLOADED      // Modelo descargado y guardado en MinIO
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getImageToken() {
        return imageToken;
    }

    public void setImageToken(String imageToken) {
        this.imageToken = imageToken;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public ModelStatus getStatus() {
        return status;
    }

    public void setStatus(ModelStatus status) {
        this.status = status;
    }

    public String getMinioImagePath() {
        return minioImagePath;
    }

    public void setMinioImagePath(String minioImagePath) {
        this.minioImagePath = minioImagePath;
    }

    public String getMinioModelPath() {
        return minioModelPath;
    }

    public void setMinioModelPath(String minioModelPath) {
        this.minioModelPath = minioModelPath;
    }

    public String getTripoModelUrl() {
        return tripoModelUrl;
    }

    public void setTripoModelUrl(String tripoModelUrl) {
        this.tripoModelUrl = tripoModelUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
