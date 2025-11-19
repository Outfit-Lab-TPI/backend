package com.outfitlab.project.domain.useCases.bucketImages;

import com.outfitlab.project.domain.interfaces.repositories.UploadImageRepository;

public class DeleteImage {

    private final UploadImageRepository iAwsRepository;

    public DeleteImage(UploadImageRepository iAwsRepository) {
        this.iAwsRepository = iAwsRepository;
    }

    public void execute(String imageUrl) {
        this.iAwsRepository.deleteFile(imageUrl);
    }
}
