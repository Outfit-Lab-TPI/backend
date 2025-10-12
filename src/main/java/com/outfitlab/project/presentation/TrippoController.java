package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.models.TripoModel;
import com.outfitlab.project.domain.uc.UploadImageToTripo;
import com.outfitlab.project.infrastructure.TrippoControllerService;
import com.outfitlab.project.presentation.dto.TripoModelResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/trippo")
@AllArgsConstructor
public class TrippoController {

    private final TrippoControllerService trippoControllerService;
    private final UploadImageToTripo uploadImageToTripo;

    @PostMapping("/upload/image")
    public ResponseEntity<TripoModelResponse> uploadImage(@RequestParam("image") MultipartFile imageFile) {
        try {
            Map<String, String> uploadResult = this.uploadImageToTripo.execute(imageFile);
        }catch (Exception e){

        }


        try {
            TripoModel model = trippoControllerService.uploadAndProcessImage(imageFile);
            return ResponseEntity.ok(buildResponse(model));
        } catch (IllegalArgumentException e) {
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
            TripoModel model = trippoControllerService.getModelByTaskId(taskId);
            return ResponseEntity.ok(buildResponse(model));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(TripoModelResponse.builder().message(e.getMessage()).build());
        }
    }

    private TripoModelResponse buildResponse(TripoModel model) {
        return TripoModelResponse.builder()
                .id(model.getId())
                .taskId(model.getTaskId())
                .status(model.getStatus().name())
                .originalFilename(model.getOriginalFilename())
                .fileExtension(model.getFileExtension())
                .minioImagePath(model.getMinioImagePath())
                .imageUrl(model.getMinioImagePath())
                .message("Imagen subida exitosamente. El modelo 3D se está generando.")
                .build();
    }
}
