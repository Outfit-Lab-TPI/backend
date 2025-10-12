package com.outfitlab.project.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.outfitlab.project.domain.models.TripoModel;
import com.outfitlab.project.domain.exceptions.ImageInvalidFormatException;
import com.outfitlab.project.domain.repositories.TripoModelRepository;
import com.outfitlab.project.s3.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrippoServiceTest {

    private S3Service s3ServiceMock;
    private TripoModelRepository tripoModelRepositoryMock;
    private RestTemplate restTemplateMock;
    private TrippoService trippoService;

    @BeforeEach
    void setup() {
        s3ServiceMock = mock(S3Service.class);
        tripoModelRepositoryMock = mock(TripoModelRepository.class);
        restTemplateMock = mock(RestTemplate.class);

        trippoService = new TrippoService(s3ServiceMock, tripoModelRepositoryMock, restTemplateMock);
    }

    private MockMultipartFile createFakeFile(String name, String contentType) {
        return new MockMultipartFile("image", name, contentType, "fake content".getBytes());
    }

    // ------------------- VALIDACIÓN DE EXTENSIONES -------------------

    @Test
    void validateExtensionWorksCorrectly() {
        assertTrue(trippoService.validateExtension("foto.jpg"));
        assertTrue(trippoService.validateExtension("foto.jpeg"));
        assertTrue(trippoService.validateExtension("foto.png"));
        assertTrue(trippoService.validateExtension("foto.webp"));
        assertFalse(trippoService.validateExtension("foto.svg"));
    }

    @Test
    void getFileExtensionWorksCorrectly() {
        assertEquals("jpg", trippoService.getFileExtension("foto.jpg"));
        assertEquals("png", trippoService.getFileExtension("foto.png"));
        assertEquals("webp", trippoService.getFileExtension("foto.webp"));
        assertEquals("", trippoService.getFileExtension(null));
        assertEquals("", trippoService.getFileExtension(""));
    }

    // ------------------- UPLOAD DE IMAGEN -------------------

    @Test
    void givenInvalidExtensionWhenUploadThenThrows() {
        MockMultipartFile file = createFakeFile("file.svg", "image/svg+xml");
        assertThrows(ImageInvalidFormatException.class, () -> trippoService.requestUploadImageApiTripo(file));
    }

    @Test
    void givenValidImageWhenUploadThenS3Called() throws IOException {
        MockMultipartFile file = createFakeFile("foto.jpg", "image/jpeg");

        // Simulamos que S3 devuelve una URL
        when(s3ServiceMock.uploadFile(file, "models_images")).thenReturn("https://s3.fake/foto.jpg");

        // Mock del RestTemplate para la subida a Trippo
        String fakeResponse = "{\"data\":{\"image_token\":\"tk_12345\"}}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(fakeResponse, HttpStatus.OK);
        when(restTemplateMock.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);

        Map<String, String> result = trippoService.requestUploadImageApiTripo(file);

        assertEquals("foto.jpg", result.get("originalFilename"));
        assertEquals("jpg", result.get("fileExtension"));
        assertEquals("https://s3.fake/foto.jpg", result.get("minioImagePath"));
        assertEquals("tk_12345", result.get("imageToken"));

        verify(s3ServiceMock, times(1)).uploadFile(file, "models_images");
        verify(restTemplateMock, times(1)).postForEntity(anyString(), any(), eq(String.class));
    }

    @Test
    void getImageResourceReturnsByteArrayResource() throws IOException {
        MockMultipartFile file = createFakeFile("foto.jpg", "image/jpeg");
        ByteArrayResource resource = trippoService.getImageResource(file);

        assertEquals(file.getOriginalFilename(), resource.getFilename());
        assertArrayEquals(file.getBytes(), resource.getByteArray());
    }

    // ------------------- GENERAR MODELO -------------------

    @Test
    void generateImageToModelTrippoSavesModel() throws JsonProcessingException {
        Map<String, String> uploadData = new HashMap<>();
        uploadData.put("fileExtension", "jpg");
        uploadData.put("originalFilename", "foto.jpg");
        uploadData.put("imageToken", "tk_12345");
        uploadData.put("minioImagePath", "https://s3.fake/foto.jpg");

        // Simulamos el response de RestTemplate
        String fakeTaskResponse = "{\"data\":{\"task_id\":\"task_12345\"}}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(fakeTaskResponse, HttpStatus.OK);
        when(restTemplateMock.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);

        trippoService.generateImageToModelTrippo(uploadData);

        // Verificamos que se haya llamado al repository para guardar
        verify(tripoModelRepositoryMock, times(1)).save(any(TripoModel.class));
    }
}
