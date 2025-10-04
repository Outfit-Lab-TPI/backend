package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.dto.GarmentUploadResponse;
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
        @RequestParam("files") MultipartFile[] files) {
    
    try {
        if (files.length != 4) {
            System.err.println("Error: Se requieren exactamente 4 imágenes, recibidas: " + files.length);
            return Mono.just(ResponseEntity.badRequest().build());
        }

        System.out.println("Uploading 4 files to ImgBB...");
        
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String publicUrl = imgBBService.uploadImage(file);
                imageUrls.add(publicUrl);
                System.out.println("Uploaded: " + publicUrl);
            }
        }

        if (imageUrls.size() != 4) {
            System.err.println("Error: No se pudieron subir las 4 imágenes");
            return Mono.just(ResponseEntity.internalServerError().build());
        }

        System.out.println("All 4 images uploaded. Using multiview_to_model");
        return tripoService.createMultiviewToModelTask(imageUrls)
                .map(ResponseEntity::ok);

    } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
        e.printStackTrace();
        return Mono.just(ResponseEntity.internalServerError().build());
    }
}

}