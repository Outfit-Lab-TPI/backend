package com.outfitlab.project.domain.useCases.tripo;

import com.outfitlab.project.domain.interfaces.repositories.UploadImageRepository;
import org.springframework.web.multipart.MultipartFile;

public class SaveImage {

    private final UploadImageRepository iAwsRepository;

    public SaveImage(UploadImageRepository awsRepository) {
        this.iAwsRepository = awsRepository;
    }

    public String execute(MultipartFile imageFile, String folder) {
        return this.iAwsRepository.uploadFile(imageFile, folder); //"models_images"
    }
}
