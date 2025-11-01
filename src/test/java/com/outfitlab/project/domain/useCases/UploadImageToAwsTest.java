package com.outfitlab.project.domain.useCases;

import com.outfitlab.project.domain.interfaces.repositories.IAwsRepository;
import com.outfitlab.project.domain.useCases.tripo.UploadImageToAws;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UploadImageToAwsTest {

    private IAwsRepository awsRepositoryMock;
    private UploadImageToAws uploadImageToAws;

    @BeforeEach
    public void setUp() {
        awsRepositoryMock = mock(IAwsRepository.class);
        uploadImageToAws = new UploadImageToAws(awsRepositoryMock);
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
