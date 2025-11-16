package com.outfitlab.project.presentation.dto;

import com.outfitlab.project.domain.model.TripoModel;
import java.time.LocalDateTime;

public class TripoModelDTO {

    private String taskId;
    private String imageToken;
    private String originalFilename;
    private String fileExtension;
    private TripoModel.ModelStatus status;
    private String minioImagePath;
    private String minioModelPath;
    private String tripoModelUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String errorMessage;

    public TripoModelDTO() {}

    public TripoModelDTO(String taskId, String imageToken, String originalFilename, String fileExtension, TripoModel.ModelStatus status, String minioImagePath, String minioModelPath, String tripoModelUrl, LocalDateTime createdAt, LocalDateTime updatedAt, String errorMessage) {
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

    public TripoModelDTO(String taskId, String imageToken, String originalFilename, String fileExtension, String minioImagePath, TripoModel.ModelStatus modelStatus) {
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

    public TripoModel.ModelStatus getStatus() {
        return status;
    }

    public void setStatus(TripoModel.ModelStatus status) {
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

    @Override
    public String toString() {
        return "TripoModel{" +
                "taskId='" + taskId + '\'' +
                ", imageToken='" + imageToken + '\'' +
                ", originalFilename='" + originalFilename + '\'' +
                ", fileExtension='" + fileExtension + '\'' +
                ", status=" + status +
                ", minioImagePath='" + minioImagePath + '\'' +
                ", minioModelPath='" + minioModelPath + '\'' +
                ", tripoModelUrl='" + tripoModelUrl + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }

    public static TripoModel convertToModel(TripoModelDTO dto) {
        TripoModel tripoModel = new TripoModel();
        tripoModel.setTaskId(dto.getTaskId());
        tripoModel.setImageToken(dto.getImageToken());
        tripoModel.setOriginalFilename(dto.getOriginalFilename());
        tripoModel.setFileExtension(dto.getFileExtension());
        tripoModel.setStatus(dto.getStatus());
        tripoModel.setMinioImagePath(dto.getMinioImagePath());
        tripoModel.setMinioModelPath(dto.getMinioModelPath());
        tripoModel.setTripoModelUrl(dto.getTripoModelUrl());
        tripoModel.setCreatedAt(dto.getCreatedAt());
        tripoModel.setUpdatedAt(dto.getUpdatedAt());
        tripoModel.setErrorMessage(dto.getErrorMessage());
        return tripoModel;
    }

}
