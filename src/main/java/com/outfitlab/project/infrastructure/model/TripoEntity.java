package com.outfitlab.project.infrastructure.model;

import com.outfitlab.project.domain.model.TripoModel;
import com.outfitlab.project.domain.model.TripoModel.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "tripo_models")
@Data
@Builder
@AllArgsConstructor
public class TripoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String taskId;

    @Column(nullable = false)
    private String imageToken;

    @Column(nullable = false)
    private String originalFilename;

    @Column(nullable = false)
    private String fileExtension;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ModelStatus status;

    @Column(length = 500)
    private String minioImagePath;

    @Column(length = 500)
    private String minioModelPath;

    @Column(length = 1000)
    private String tripoModelUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(length = 500)
    private String errorMessage;

    public TripoEntity(){}

    public TripoEntity(String taskId, String imageToken, String originalFilename, String fileExtension, ModelStatus status, String minioImagePath, String minioModelPath, String tripoModelUrl, LocalDateTime createdAt, LocalDateTime updatedAt, String errorMessage) {
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

    // TODO: Relación con User cuando esté implementada
    // @ManyToOne
    // @JoinColumn(name = "user_id")
    // private User user;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getter manual para taskId (Lombok parece no generar esto correctamente en build)
    public String getTaskId() {
        return taskId;
    }

// ------------------- los dos convert ----------------------------
    public static TripoEntity convertToEntity(TripoModel model){
        return new TripoEntity(
                model.getTaskId(),
                model.getImageToken(),
                model.getOriginalFilename(),
                model.getFileExtension(),
                model.getStatus(),
                model.getMinioImagePath(),
                model.getMinioModelPath(),
                model.getTripoModelUrl(),
                model.getCreatedAt(),
                model.getUpdatedAt(),
                model.getErrorMessage()
        );
    }

    public static TripoModel convertToModel(TripoEntity entity){
        return new TripoModel(
                entity.getTaskId(),
                entity.getImageToken(),
                entity.getOriginalFilename(),
                entity.getFileExtension(),
                entity.getStatus(),
                entity.getMinioImagePath(),
                entity.getMinioModelPath(),
                entity.getTripoModelUrl(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getErrorMessage()
        );
    }
    // ---------------------------------------------------------
}
