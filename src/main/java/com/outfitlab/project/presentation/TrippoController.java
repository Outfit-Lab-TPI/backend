package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.entities.TripoModel;
import com.outfitlab.project.domain.repositories.TripoModelRepository;
import com.outfitlab.project.infrastructure.MinioStorageService;
import com.outfitlab.project.infrastructure.TrippoService;
import com.outfitlab.project.presentation.dto.TripoModelResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/trippo")
public class TrippoController {

    private final TrippoService trippoService;
    private final TripoModelRepository tripoModelRepository;
    private final MinioStorageService minioService;

    public TrippoController(TrippoService trippoService, 
                           TripoModelRepository tripoModelRepository,
                           MinioStorageService minioService) {
        this.trippoService = trippoService;
        this.tripoModelRepository = tripoModelRepository;
        this.minioService = minioService;
    }

    @PostMapping("/upload/image")
    public ResponseEntity<TripoModelResponse> uploadImage(@RequestParam("image") MultipartFile imageFile) {
        try {
            log.info("Recibiendo imagen: {} ({} bytes)", imageFile.getOriginalFilename(), imageFile.getSize());
            
            // 1. Subir imagen a Tripo3D y MinIO
            Map<String, String> uploadData = trippoService.uploadImageToTrippo(imageFile);
            
            // 2. Crear tarea de generación de modelo 3D
            String taskId = trippoService.generateImageToModelTrippo(uploadData);
            
            // 3. Buscar el modelo guardado en BD
            Optional<TripoModel> modelOpt = tripoModelRepository.findByTaskId(taskId);
            
            if (modelOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(TripoModelResponse.builder()
                                .message("Error: Modelo no encontrado en base de datos")
                                .build());
            }
            
            TripoModel model = modelOpt.get();
            
            // 4. Preparar respuesta
            TripoModelResponse response = TripoModelResponse.builder()
                    .id(model.getId())
                    .taskId(model.getTaskId())
                    .status(model.getStatus().name())
                    .originalFilename(model.getOriginalFilename())
                    .fileExtension(model.getFileExtension())
                    .minioImagePath(model.getMinioImagePath())
                    .imageUrl(minioService.getImageUrl(model.getMinioImagePath()))
                    .message("Imagen subida exitosamente. El modelo 3D se está generando.")
                    .build();
            
            log.info("Proceso completado exitosamente. TaskId: {}", taskId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error procesando imagen: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(TripoModelResponse.builder()
                            .message("Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/models/{taskId}")
    public ResponseEntity<TripoModelResponse> getModelStatus(@PathVariable String taskId) {
        try {
            Optional<TripoModel> modelOpt = tripoModelRepository.findByTaskId(taskId);
            
            if (modelOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(TripoModelResponse.builder()
                                .message("Modelo no encontrado")
                                .build());
            }
            
            TripoModel model = modelOpt.get();
            
            TripoModelResponse response = TripoModelResponse.builder()
                    .id(model.getId())
                    .taskId(model.getTaskId())
                    .status(model.getStatus().name())
                    .originalFilename(model.getOriginalFilename())
                    .fileExtension(model.getFileExtension())
                    .minioImagePath(model.getMinioImagePath())
                    .imageUrl(minioService.getImageUrl(model.getMinioImagePath()))
                    .message("Estado del modelo obtenido exitosamente")
                    .build();
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error obteniendo estado del modelo: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(TripoModelResponse.builder()
                            .message("Error: " + e.getMessage())
                            .build());
        }
    }
}
