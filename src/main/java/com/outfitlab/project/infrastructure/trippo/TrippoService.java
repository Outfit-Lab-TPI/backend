package com.outfitlab.project.infrastructure.trippo;

import com.outfitlab.project.domain.models.TripoModel;
import com.outfitlab.project.infrastructure.trippo.usecases.CheckTaskStatus;
import com.outfitlab.project.infrastructure.trippo.usecases.GetImageResource;
import com.outfitlab.project.infrastructure.trippo.usecases.SaveFilesFromTask;
import com.outfitlab.project.infrastructure.trippo.usecases.UploadAndProcessImageToModel;
import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.outfitlab.project.domain.repositories.TripoModelRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TrippoService {

    private final TripoModelRepository tripoModelRepository;
    private final UploadAndProcessImageToModel uploadAndProcessImageToModel;
    private final CheckTaskStatus checkTaskStatus;
    private final SaveFilesFromTask saveFilesFromTask;
    private final GetImageResource getImageResource;

    public TripoModel uploadAndProcessImage(MultipartFile imageFile) throws Exception {
        return uploadAndProcessImageToModel.uploadAndProcessImageToModel(imageFile);
    }
    public TripoModel getModelByTaskId(String taskId) {
        return tripoModelRepository.findByTaskId(taskId)
                .orElseThrow(() -> new IllegalStateException("Modelo no encontrado"));
    }
    public Map<String, String> checkTaskStatus(String taskId) throws JsonProcessingException, InterruptedException {
        return checkTaskStatus.checkTaskStatus(taskId);
    }
   public Map<String, String> saveFilesFromTask(Map<String, String> taskResponse) throws IOException {
        return saveFilesFromTask.saveFilesFromTask(taskResponse);
   }
   public ByteArrayResource getImageResource(MultipartFile imageFile) throws IOException {
        return getImageResource.getImageResource(imageFile);
   }

}
