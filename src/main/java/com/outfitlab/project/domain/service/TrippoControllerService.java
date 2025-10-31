package com.outfitlab.project.domain.service;

import com.outfitlab.project.infrastructure.model.TripoEntity;
import com.outfitlab.project.domain.interfaces.repositories.ITripoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@AllArgsConstructor
public class TrippoControllerService {

    private final TrippoService trippoService;
    private final ITripoRepository tripoModelRepository;

    public TripoEntity uploadAndProcessImage(MultipartFile imageFile) throws Exception {
        if (imageFile.isEmpty()) throw new IllegalArgumentException("Archivo vacío");

        Map<String, String> uploadData = trippoService.uploadImageToTrippo(imageFile);
        String taskId = trippoService.generateImageToModelTrippo(uploadData);
        System.out.println(this.trippoService.checkTaskStatus(taskId));

        TripoEntity model = tripoModelRepository.findByTaskId(taskId)
                .orElseThrow(() -> new IllegalStateException("Modelo no encontrado en base de datos"));

        model.setMinioImagePath(uploadData.get("minioImagePath"));
        return tripoModelRepository.save(model);
    }

    public TripoEntity getModelByTaskId(String taskId) {
        return tripoModelRepository.findByTaskId(taskId)
                .orElseThrow(() -> new IllegalStateException("Modelo no encontrado"));
    }
}
