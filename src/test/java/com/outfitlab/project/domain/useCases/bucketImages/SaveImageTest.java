package com.outfitlab.project.domain.useCases.bucketImages;

import com.outfitlab.project.domain.interfaces.repositories.UploadImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SaveImageTest {

    private UploadImageRepository uploadImageRepository = mock(UploadImageRepository.class);
    private SaveImage saveImage;

    private final String FOLDER_NAME = "models_images";
    private final String EXPECTED_URL = "https://mybucket.s3.aws.com/models_images/image.png";
    private MultipartFile mockImageFile;

    @BeforeEach
    void setUp() {
        mockImageFile = mock(MultipartFile.class);
        saveImage = new SaveImage(uploadImageRepository);
    }


    @Test
    public void shouldCallRepositoryToUploadFileAndReturnUrl() {
        givenRepositoryReturnsUrl(mockImageFile, FOLDER_NAME, EXPECTED_URL);

        String resultUrl = whenExecuteSaveImage(mockImageFile, FOLDER_NAME);

        thenResultMatchesExpectedUrl(resultUrl, EXPECTED_URL);
        thenRepositoryUploadFileWasCalled(mockImageFile, FOLDER_NAME);
    }


    //privadosss
    private void givenRepositoryReturnsUrl(MultipartFile file, String folder, String url) {
        when(uploadImageRepository.uploadFile(file, folder)).thenReturn(url);
    }

    private String whenExecuteSaveImage(MultipartFile imageFile, String folder) {
        return saveImage.execute(imageFile, folder);
    }

    private void thenResultMatchesExpectedUrl(String actualUrl, String expectedUrl) {
        assertNotNull(actualUrl, "La URL devuelta no debe ser nula.");
        assertEquals(expectedUrl, actualUrl, "La URL devuelta debe coincidir con la URL simulada.");
    }

    private void thenRepositoryUploadFileWasCalled(MultipartFile file, String folder) {
        verify(uploadImageRepository, times(1)).uploadFile(file, folder);
    }
}