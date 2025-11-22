package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.model.TripoModel;
import com.outfitlab.project.domain.useCases.bucketImages.SaveImage;
import com.outfitlab.project.domain.useCases.tripo.*;
import com.outfitlab.project.presentation.dto.ImageUploadRequest;
import com.outfitlab.project.presentation.dto.TripoModelDTO;
import com.outfitlab.project.presentation.dto.TripoModelResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/tripo")
@AllArgsConstructor
public class TrippoController {

    private final GenerateImageToModelTrippo generateImageToModelTrippo;
    private final FindTripoModelByTaskid findTripoModelByTaskid;
    private final UploadImageToTripo uploadImageToTripo;
    private final UpdateTripoModel updateTripoModel;
    private final CheckTaskStatus checkTaskStatus;
    private final SaveTripoModel saveTripoModel;
    private final SaveImage uploadImageToAws;

    @PostMapping("/upload/image")
    public ResponseEntity<TripoModelResponse> uploadImage(@RequestBody ImageUploadRequest request) {
        try {
            Map<String, Object> uploadData = this.uploadImageToTripo.execute(request.getImageUrl().getImageUrl());
            log.info("ACÁ DEBE ESTAR EL FILE_TOKEN: ------{}",uploadData.toString());

            uploadData.put("minioImagePath", this.uploadImageToAws.execute( (MultipartFile) uploadData.get("imageMultipartFile"), "models_images"));

            String taskId = this.generateImageToModelTrippo.execute(uploadData);

            TripoModelDTO tripoModelDTO = createTripoModelDTO(taskId, uploadData);

            this.saveTripoModel.execute(TripoModelDTO.convertToModel(tripoModelDTO));
            log.info("Modelo guardado en BD con taskId: {}", taskId);

            tripoModelDTO.setTripoModelUrl(this.checkTaskStatus.execute(taskId));
            tripoModelDTO.setStatus(TripoModel.ModelStatus.COMPLETED);

            return ResponseEntity.ok(buildResponse(this.updateTripoModel.execute(TripoModelDTO.convertToModel(tripoModelDTO))));
        } catch (ErroBytesException | ErrorReadJsonException | ErrorUploadImageToTripoException | FileEmptyException | ErrorGenerateGlbException | ErrorGlbGenerateTimeExpiredException | ErrorWhenSleepException | ErrorTripoEntityNotFoundException e) {
            return ResponseEntity.badRequest().body(TripoModelResponse.builder().message(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(TripoModelResponse.builder().message("Error: " + e.getMessage()).build());
        }
    }

    @GetMapping("/models/{taskId}")
    public ResponseEntity<TripoModelResponse> getModelStatus(@PathVariable String taskId) {
        try {
            TripoModel model = this.findTripoModelByTaskid.execute(taskId);
            return ResponseEntity.ok(buildResponse(model));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(TripoModelResponse.builder().message(e.getMessage()).build());
        }
    }

    @GetMapping("/models/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam String url) {
        try {
            URL externalUrl = new URL(url);
            InputStream inputStream = externalUrl.openStream();
            byte[] fileBytes = inputStream.readAllBytes();

            String fileName = url.substring(url.lastIndexOf('/') + 1);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileBytes);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private TripoModelDTO createTripoModelDTO(String taskId, Map<String, Object> uploadData) {
        return new TripoModelDTO(
                taskId,
                (String) uploadData.get("imageToken"),
                (String) uploadData.get("originalFilename"),
                (String) uploadData.get("fileExtension"),
                (String) uploadData.get("minioImagePath"),
                TripoModel.ModelStatus.PENDING
        );
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
