package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.dto.TripoCreateTaskResponse;
import com.outfitlab.project.infrastructure.ImgBBService;
import com.outfitlab.project.infrastructure.TripoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/garments")
public class GarmentUploadController {

    private final ImgBBService imgBBService;
    private final TripoService tripoService;

    public GarmentUploadController(ImgBBService imgBBService, TripoService tripoService) {
        this.imgBBService = imgBBService;
        this.tripoService = tripoService;
    }

    @PostMapping("/generate")
    public Mono<ResponseEntity<TripoCreateTaskResponse>> generateGarmentModel(
            @RequestParam("back") MultipartFile backImage,
            @RequestParam("front") MultipartFile frontImage,
            @RequestParam("left") MultipartFile leftImage,
            @RequestParam("right") MultipartFile rightImage) {

        try {
            System.out.println("Uploading images to ImgBB in correct order for multiview_to_model...");

            // Verificar que al menos la imagen frontal esté presente (requerida por Tripo)
            if (frontImage.isEmpty()) {
                System.err.println("Error: La imagen frontal es requerida");
                return Mono.just(ResponseEntity.badRequest().build());
            }

            // Subir imagen frontal (requerida)
            String frontUrl = imgBBService.uploadImage(frontImage);
            System.out.println("Front image uploaded: " + frontUrl);

            // Subir imagen izquierda (opcional)
            String leftUrl = null;
            if (!leftImage.isEmpty()) {
                leftUrl = imgBBService.uploadImage(leftImage);
                System.out.println("Left image uploaded: " + leftUrl);
            }

            // Subir imagen trasera (opcional)
            String backUrl = null;
            if (!backImage.isEmpty()) {
                backUrl = imgBBService.uploadImage(backImage);
                System.out.println("Back image uploaded: " + backUrl);
            }

            // Subir imagen derecha (opcional)
            String rightUrl = null;
            if (!rightImage.isEmpty()) {
                rightUrl = imgBBService.uploadImage(rightImage);
                System.out.println("Right image uploaded: " + rightUrl);
            }

            System.out.println("Images uploaded. Creating multiview_to_model task...");
            return tripoService.createMultiviewToModelTask(frontUrl, leftUrl, backUrl, rightUrl)
                    .map(ResponseEntity::ok);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return Mono.just(ResponseEntity.internalServerError().build());
        }
    }

}