package com.outfitlab.project.infrastructure;

import com.outfitlab.project.domain.entities.TripoModel;
import com.outfitlab.project.domain.repositories.TripoModelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@AllArgsConstructor
public class TrippoControllerService {

    private final TrippoService trippoService;
    private final TripoModelRepository tripoModelRepository;

    public TripoModel uploadAndProcessImage(MultipartFile imageFile) throws Exception {
        if (imageFile.isEmpty()) throw new IllegalArgumentException("Archivo vacío");

        Map<String, String> uploadData = trippoService.uploadImageToTrippo(imageFile);
        String taskId = trippoService.generateImageToModelTrippo(uploadData);

        TripoModel model = tripoModelRepository.findByTaskId(taskId)
                .orElseThrow(() -> new IllegalStateException("Modelo no encontrado en base de datos"));

        model.setMinioImagePath(uploadData.get("minioImagePath"));
        return tripoModelRepository.save(model);
    }

    public TripoModel getModelByTaskId(String taskId) {
        return tripoModelRepository.findByTaskId(taskId)
                .orElseThrow(() -> new IllegalStateException("Modelo no encontrado"));
    }
}
