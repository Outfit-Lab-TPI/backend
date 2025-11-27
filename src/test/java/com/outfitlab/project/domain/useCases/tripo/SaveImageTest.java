package com.outfitlab.project.domain.useCases.tripo;

import com.outfitlab.project.domain.interfaces.repositories.UploadImageRepository;
import com.outfitlab.project.domain.useCases.bucketImages.SaveImage;
import com.outfitlab.project.infrastructure.repositories.UploadImageRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SaveImageTest {

    private UploadImageRepository uploadImageRepository = mock(UploadImageRepositoryImpl.class);
    private SaveImage saveImage;

    private final String FOLDER_NAME = "models_images";
    private final String EXPECTED_URL = "https://aws-bucket.s3.amazonaws.com/models_images/img123.png";
    private MultipartFile mockFile;

    @BeforeEach
    void setUp() {
        saveImage = new SaveImage(uploadImageRepository);
        mockFile = mock(MultipartFile.class);
    }


    @Test
    public void shouldReturnUrlWhenImageFileIsSuccessfullyUploaded() {
        givenRepositoryReturnsUrl(mockFile, FOLDER_NAME, EXPECTED_URL);

        String result = whenExecuteSaveImage(mockFile, FOLDER_NAME);

        thenReturnedUrlIsCorrect(result, EXPECTED_URL);
        thenRepositoryWasCalledOnce(mockFile, FOLDER_NAME);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenUploadFails() {
        givenRepositoryThrowsRuntimeException(mockFile, FOLDER_NAME);

        assertThrows(RuntimeException.class,
                () -> whenExecuteSaveImage(mockFile, FOLDER_NAME),
                "Se espera que la excepción de Runtime se propague si el repositorio falla.");

        thenRepositoryWasCalledOnce(mockFile, FOLDER_NAME);
    }


    //privadoss
    private void givenRepositoryReturnsUrl(MultipartFile file, String folder, String url) {
        when(uploadImageRepository.uploadFile(file, folder)).thenReturn(url);
    }

    private void givenRepositoryThrowsRuntimeException(MultipartFile file, String folder) {
        doThrow(new RuntimeException("Simulated upload failure")).when(uploadImageRepository).uploadFile(file, folder);
    }

    private String whenExecuteSaveImage(MultipartFile file, String folder) {
        return saveImage.execute(file, folder);
    }


    private void thenReturnedUrlIsCorrect(String result, String expectedUrl) {
        assertNotNull(result, "El resultado no debe ser nulo.");
        assertEquals(expectedUrl, result, "La URL devuelta debe coincidir con la esperada.");
    }

    private void thenRepositoryWasCalledOnce(MultipartFile file, String folder) {
        verify(uploadImageRepository, times(1)).uploadFile(file, folder);
    }
}