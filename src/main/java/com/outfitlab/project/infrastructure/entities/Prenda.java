package com.outfitlab.project.infrastructure.entities;

import com.outfitlab.project.domain.models.TripoModel;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public class Prenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String taskId;              // ID de la tarea en Tripo3D

    @Column(nullable = false)
    private String imageToken;          // Token de imagen de Tripo3D

    @Column(nullable = false)
    private String originalFilename;    // Nombre original del archivo

    @Column(nullable = false)
    private String fileExtension;       // jpg, png, webp, etc.

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ModelStatus status;         // Estado del modelo

    @Column(length = 500)
    private String minioImagePath;      // Ruta en MinIO de la imagen original

    @Column(length = 500)
    private String minioModelPath;      // Ruta en MinIO del modelo 3D

    @Column(length = 1000)
    private String tripoModelUrl;       // URL del modelo en Tripo3D (cuando esté listo)

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(length = 500)
    private String errorMessage;        // Mensaje de error si falla

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

    public enum ModelStatus {
        PENDING,
        PROCESSING,
        COMPLETED,
        FAILED,
        DOWNLOADED
    }

    public static Prenda convertToEntity(TripoModel model) {
        Prenda prenda = new Prenda();
        prenda.id = model.getId();
        prenda.taskId = model.getTaskId();
        prenda.imageToken = model.getImageToken();
        prenda.originalFilename = model.getOriginalFilename();
        prenda.fileExtension = model.getFileExtension();
        prenda.status = Prenda.ModelStatus.valueOf(model.getStatus().name());
        prenda.minioImagePath = model.getMinioImagePath();
        prenda.minioModelPath = model.getMinioModelPath();
        prenda.tripoModelUrl = model.getTripoModelUrl();
        prenda.createdAt = model.getCreatedAt();
        prenda.updatedAt = model.getUpdatedAt();
        prenda.errorMessage = model.getErrorMessage();
        return prenda;
    }

}
