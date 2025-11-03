package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.model.TripoModel;
import com.outfitlab.project.domain.service.TrippoService;
import com.outfitlab.project.presentation.dto.TripoModelResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/trippo")
@AllArgsConstructor
public class TrippoController {

    private final TrippoService trippoService;

    @PostMapping("/upload/image")
    public ResponseEntity<TripoModelResponse> uploadImage(@RequestParam("image") MultipartFile imageFile) {
        try {
            TripoModel model = this.trippoService.procesarYEnviarATripo(imageFile);
            return ResponseEntity.ok(buildResponse(model));

        } catch (FileEmptyException | ErroBytesException | ErrorReadJsonException | ErrorUploadImageToTripo |
                 ErrorGenerateGlbException | ErrorGlbGenerateTimeExpiredException | ErrorWhenSleepException |
                 ErrorTripoEntityNotFound e) {

            return ResponseEntity.badRequest()
                    .body(TripoModelResponse.builder().message(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(TripoModelResponse.builder().message("Error: " + e.getMessage()).build());
        }
    }

    @GetMapping("/models/{taskId}")
    public ResponseEntity<TripoModelResponse> getModelStatus(@PathVariable String taskId) {
        try {
            TripoModel model = this.trippoService.buscarPorTaskid(taskId);
            return ResponseEntity.ok(buildResponse(model));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(TripoModelResponse.builder().message(e.getMessage()).build());
        }
    }

    private TripoModelResponse buildResponse(TripoModel model) {
        return TripoModelResponse.builder()
                .taskId(model.getTaskId())
                .status(model.getStatus().name())
                .originalFilename(model.getOriginalFilename())
                .fileExtension(model.getFileExtension())
                .minioImagePath(model.getMinioImagePath())
                .imageUrl(model.getMinioImagePath())
                .tripoModelUrl(model.getTripoModelUrl())
                .message("Imagen subida exitosamente. El modelo 3D se está generando.")
                .build();
    }
}
