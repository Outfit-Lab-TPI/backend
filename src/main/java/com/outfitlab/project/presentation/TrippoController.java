package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.model.TripoModel;
import com.outfitlab.project.domain.service.TrippoService;
import com.outfitlab.project.presentation.dto.ImageUploadRequest;
import com.outfitlab.project.presentation.dto.TripoModelResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;

@RestController
@RequestMapping("/api/tripo")
@AllArgsConstructor
public class TrippoController {

    private final TrippoService trippoService;

    @PostMapping("/upload/image")
    public ResponseEntity<TripoModelResponse> uploadImage(@RequestBody ImageUploadRequest request) {
        String url = request.getImageUrl().getImageUrl();
        System.out.println("URL que recibo: " + url);
        try {
            MultipartFile imageFile = convertImageUrlToMultipartFile(url);
            TripoModel model = this.trippoService.procesarYEnviarATripo(imageFile);
            return ResponseEntity.ok(buildResponse(model));

        } catch (FileEmptyException | ErroBytesException | ErrorReadJsonException | ErrorUploadImageToTripoException |
                 ErrorGenerateGlbException | ErrorGlbGenerateTimeExpiredException | ErrorWhenSleepException |
                 ErrorTripoEntityNotFoundException e) {

            return ResponseEntity.badRequest()
                    .body(TripoModelResponse.builder().message(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(TripoModelResponse.builder().message("Error: " + e.getMessage()).build());
        }
    }

    public MultipartFile convertImageUrlToMultipartFile(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        try (InputStream inputStream = url.openStream()) {
            return new MockMultipartFile(
                    "image",
                    "imagen.png",
                    "image/png",
                    inputStream
            );
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
