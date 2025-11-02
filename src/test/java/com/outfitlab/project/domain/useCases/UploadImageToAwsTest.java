package com.outfitlab.project.domain.useCases;

import com.outfitlab.project.domain.interfaces.repositories.UploadImageRepository;
import com.outfitlab.project.domain.useCases.tripo.SaveImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UploadImageToAwsTest {

    private UploadImageRepository awsRepositoryMock;
    private SaveImage uploadImageToAws;

    @BeforeEach
    public void setUp() {
        awsRepositoryMock = mock(UploadImageRepository.class);
        uploadImageToAws = new SaveImage(awsRepositoryMock);
    }

    @Test
    public void ejecutarDeberiaSubirArchivoYDevolverRuta_cuandoArchivoEsValido() {
        MultipartFile file = new MockMultipartFile("file", "imagen.png", "image/png", new byte[]{1,2,3});
        String rutaMock = "https://aws.com/models_images/imagen.png";

        when(awsRepositoryMock.uploadFile(file, "models_images")).thenReturn(rutaMock);

        String resultado = uploadImageToAws.execute(file);

        assertNotNull(resultado);
        assertEquals(rutaMock, resultado);
        verify(awsRepositoryMock, times(1)).uploadFile(file, "models_images");
    }

    @Test
    public void ejecutarDeberiaDevolverNull_cuandoRepositorioDevuelveNull() {
        MultipartFile file = new MockMultipartFile("file", "imagen.png", "image/png", new byte[]{});

        when(awsRepositoryMock.uploadFile(file, "models_images")).thenReturn(null);

        String resultado = uploadImageToAws.execute(file);

        assertNull(resultado);
        verify(awsRepositoryMock, times(1)).uploadFile(file, "models_images");
    }
}
