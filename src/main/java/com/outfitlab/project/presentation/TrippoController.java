package com.outfitlab.project.presentation;

import com.outfitlab.project.infrastructure.TrippoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
public class TrippoController {

    private final TrippoService trippoService;

    public TrippoController(TrippoService trippoService) {
        this.trippoService = trippoService;
    }

    @PostMapping("/image")
    public ResponseEntity<String> uploadStaticImage(@RequestParam("image") MultipartFile imageFile) {
        try {
            Map<String, String> uploadData = trippoService.uploadImageToTrippo(imageFile);
            String taskId = trippoService.generateImageToModelTrippo(uploadData);
            return ResponseEntity.ok("");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
