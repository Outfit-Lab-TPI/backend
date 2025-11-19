package com.outfitlab.project.domain.useCases.tripo;

import com.outfitlab.project.domain.interfaces.repositories.UploadImageRepository;
import com.outfitlab.project.infrastructure.repositories.UploadImageRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaveImageTest {

    private UploadImageRepository uploadImageRepository = mock(UploadImageRepositoryImpl.class);
    private SaveImage saveImage = new SaveImage(uploadImageRepository);

    @Test
    public void givenValidImageFileWhenExecuteThenReturnUrl() {
        MultipartFile mockFile = mock(MultipartFile.class);
        String expectedUrl = "https://aws-bucket.s3.amazonaws.com/models_images/img123.png";

        when(uploadImageRepository.uploadFile(mockFile, "models_images")).thenReturn(expectedUrl);
        String result = saveImage.execute(mockFile, "models_images");

        assertNotNull(result);
        assertEquals(expectedUrl, result);
        verify(uploadImageRepository, times(1)).uploadFile(mockFile, "models_images");
    }
}

