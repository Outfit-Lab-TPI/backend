package com.outfitlab.project.domain.interfaces.repositories;

import org.springframework.web.multipart.MultipartFile;

public interface UploadImageRepository {

    String uploadFile(MultipartFile file, String folder);

    void deleteFile(String key);

    String getFileUrl(String key);

}
