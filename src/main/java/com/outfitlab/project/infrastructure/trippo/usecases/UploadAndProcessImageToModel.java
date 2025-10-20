package com.outfitlab.project.infrastructure.trippo.usecases;

import com.outfitlab.project.domain.models.TripoModel;
import com.outfitlab.project.domain.repositories.TripoModelRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@AllArgsConstructor
public class UploadAndProcessImageToModel {

    private final TripoModelRepository tripoModelRepository;
    private final UploadImageToTrippo uploadImageToTrippo;
    private final ImageToModelTrippo imageToModelTrippo;

    public TripoModel uploadAndProcessImageToModel(MultipartFile imageFile) throws Exception {
        if (imageFile.isEmpty()) throw new IllegalArgumentException("Archivo vacío");


        Map<String, String> uploadData = uploadImageToTrippo.uploadImageToTrippo(imageFile);
        String taskId = imageToModelTrippo.generateImageToModelTrippo(uploadData);
        TripoModel model = tripoModelRepository.findByTaskId(taskId)
                .orElseThrow(() -> new IllegalStateException("Modelo no encontrado en base de datos"));

        model.setMinioImagePath(uploadData.get("minioImagePath"));
        return tripoModelRepository.save(model);
    }


}
