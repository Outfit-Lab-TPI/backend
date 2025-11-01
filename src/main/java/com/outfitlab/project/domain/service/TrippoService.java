package com.outfitlab.project.domain.service;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.model.TripoModel;
import com.outfitlab.project.domain.useCases.tripo.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

public class TrippoService {

    private final UploadImageToTripo uploadImageToTripo;
    private final UploadImageToAws uploadImageToAws;
    private final GenerateImageToModelTrippo generateImageToModelTrippo;
    private final SaveTripoModel saveTripoModel;
    private final CheckTaskStatus checkTaskStatus;
    private final UpdateTripoModel updateTripoModel;
    private final FindTripoModelByTaskid findTripoModelByTaskid;

    public TrippoService(UploadImageToTripo uploadImageToTripo, UploadImageToAws uploadImageToAws, GenerateImageToModelTrippo generateImageToModelTrippo,
                         SaveTripoModel saveTripoModel, CheckTaskStatus checkTaskStatus, UpdateTripoModel updateTripoModel, FindTripoModelByTaskid findTripoModelByTaskid) {
        this.uploadImageToTripo = uploadImageToTripo;
        this.uploadImageToAws = uploadImageToAws;
        this.generateImageToModelTrippo = generateImageToModelTrippo;
        this.checkTaskStatus = checkTaskStatus;
        this.saveTripoModel = saveTripoModel;
        this.updateTripoModel = updateTripoModel;
        this.findTripoModelByTaskid = findTripoModelByTaskid;
    }

    public TripoModel procesarYEnviarATripo(MultipartFile imageFile) throws FileEmptyException, ErroBytesException, ErrorReadJsonException,
            ErrorUploadImageToTripo, ErrorGenerateGlbException, ErrorGlbGenerateTimeExpiredException, ErrorWhenSleepException, ErrorTripoEntityNotFound {

        if (imageFile.isEmpty()) throw new FileEmptyException("Archivo vacío");

        Map<String, String> uploadData = this.uploadImageToTripo.execute(imageFile);

        uploadData.put("minioImagePath", this.uploadImageToAws.execute(imageFile));

        String taskId = this.generateImageToModelTrippo.execute(uploadData);

        TripoModel tripoModel = new TripoModel(
                taskId,
                uploadData.get("imageToken"),
                uploadData.get("originalFilename"),
                uploadData.get("fileExtension"),
                uploadData.get("minioImagePath"),
                TripoModel.ModelStatus.PENDING
        );

        this.saveTripoModel.execute(tripoModel);
        System.out.println("Modelo guardado en BD con taskId: " +  taskId);

        String glbUrlGenerated = this.checkTaskStatus.execute(taskId);

        tripoModel.setStatus(TripoModel.ModelStatus.COMPLETED);

        return this.updateTripoModel.execute(tripoModel);
    }

    public TripoModel buscarPorTaskid(String taskId) {
        return this.findTripoModelByTaskid.execute(taskId);
    }
}
