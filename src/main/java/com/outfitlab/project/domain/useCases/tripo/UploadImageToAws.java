package com.outfitlab.project.domain.useCases.tripo;

import com.outfitlab.project.domain.interfaces.repositories.IAwsRepository;
import org.springframework.web.multipart.MultipartFile;

public class UploadImageToAws {

    private final IAwsRepository iAwsRepository;

    public UploadImageToAws(IAwsRepository awsRepository) {
        this.iAwsRepository = awsRepository;
    }

    public String execute(MultipartFile imageFile) {
        return this.iAwsRepository.uploadFile(imageFile, "models_images");
    }
}
