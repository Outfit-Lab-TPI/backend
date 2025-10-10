package com.outfitlab.project.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.outfitlab.project.domain.exceptions.ImageInvalidFormatException;
import com.outfitlab.project.domain.repositories.TripoModelRepository;
import com.outfitlab.project.s3.S3Service;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrippoServiceTest {

    private TrippoService trippoServiceMock = mock(TrippoService.class);
    private MinioStorageService minioStorageService = mock(MinioStorageService.class);
    private S3Service s3Service = mock(S3Service.class);
    private TripoModelRepository trippoModelRepository = mock(TripoModelRepository.class);
    private TrippoService trippoService = new TrippoService(s3Service, minioStorageService, trippoModelRepository);

    public MockMultipartFile giveFakeFile(){
        byte[] content = "fake image content".getBytes();
        MockMultipartFile mockFile = new MockMultipartFile(
                "image",
                "foto.svg",
                "image/jpeg",
                content
        );
        return mockFile;
    }

    @Test
    public void givenValidImageWhenUploadImageThenResultSuccess() throws IOException {
        MockMultipartFile mockFile = this.giveFakeFile();

        Map<String, String> uploadResult = new HashMap<>();
        uploadResult.put("fileExtension", "jpg");
        uploadResult.put("imageToken", "tk_123456789");

        when(trippoServiceMock.uploadImageToTrippo(mockFile)).thenReturn(uploadResult);
        assertEquals(uploadResult, trippoServiceMock.uploadImageToTrippo(mockFile));
    }

    @Test
    public void givenImageWhenUploadImageThenReturnNewResource() throws IOException {
        MockMultipartFile mockFile = this.giveFakeFile();
        assertNotNull(trippoService.getImageResource(mockFile));
    }

    @Test
    public void givenInvalidImageWhenUploadImageThenReturnIOException() throws IOException {
        MockMultipartFile mockFile = this.giveFakeFile();
        when(trippoServiceMock.getImageResource(mockFile)).thenThrow(IOException.class);
        assertThrows(IOException.class, ()-> trippoServiceMock.getImageResource(mockFile));
    }

    @Test
    public void givenInvalidImageWhenUploadImageThenThrowException() throws IOException {
        MockMultipartFile mockFile = this.giveFakeFile();
        when(trippoServiceMock.uploadImageToTrippo(mockFile)).thenThrow(ImageInvalidFormatException.class);
        assertThrows(ImageInvalidFormatException.class, () -> trippoServiceMock.uploadImageToTrippo(mockFile));
    }

    @Test
    public void givenInvalidUploadDataWhenGenerateImageToModelTrippoThenThrowJsonProcessingException() throws JsonProcessingException {
        Map<String, String> uploadData = new HashMap<>();
        uploadData.put("fileExtension", "png");
        uploadData.put("imageToken", "tk_123456789");

        when(trippoServiceMock.generateImageToModelTrippo(uploadData)).thenThrow(JsonProcessingException.class);
        assertThrows(JsonProcessingException.class, () -> trippoServiceMock.generateImageToModelTrippo(uploadData));
    }

    @Test
    public void givenValidUploadDataWhenGenerateImageToModelTrippoThenReturnTaskId() throws JsonProcessingException {
        Map<String, String> uploadData = new HashMap<>();
        uploadData.put("fileExtension", "png");
        uploadData.put("imageToken", "tk_123456789");

        when(trippoServiceMock.generateImageToModelTrippo(uploadData)).thenReturn("task_id_12345");
        assertEquals("task_id_12345", trippoServiceMock.generateImageToModelTrippo(uploadData));
    }

    @Test
    public void givenFileNameWhenCheckExtensionThenReturnExtension(){
        String fileName = "foto.png";
        assertEquals("png", this.trippoService.getFileExtension(fileName));

        fileName = "foto.jpg";
        assertEquals("jpg", this.trippoService.getFileExtension(fileName));

        fileName = "foto.jpeg";
        assertEquals("jpeg", this.trippoService.getFileExtension(fileName));

        fileName = "foto.webp";
        assertEquals("webp", this.trippoService.getFileExtension(fileName));
    }

    @Test
    public void givenFileNameNullWhenCheckExtensionThenReturnEmptyString(){
        String fileName = "";
        assertEquals("", this.trippoService.getFileExtension(fileName));

        fileName = null;
        assertEquals("", this.trippoService.getFileExtension(fileName));
    }

    @Test
    public void givenValidJpegImageWhenUploadImageThenReturnTrue() {
        String fileName = "remera.jpeg";
        assertTrue(this.trippoService.validateExtension(fileName));
    }

    @Test
    public void givenValidJpgImageWhenUploadImageThenReturnTrue() {
        String fileName = "buzo.jpg";
        assertTrue(this.trippoService.validateExtension(fileName));
    }

    @Test
    public void givenValidPngImageWhenUploadImageThenReturnTrue() {
        String fileName = "campera.png";
        assertTrue(this.trippoService.validateExtension(fileName));
    }

    @Test
    public void givenValidWebpImageWhenUploadImageThenReturnTrue() {
        String fileName = "pantalon.webp";
        assertTrue(this.trippoService.validateExtension(fileName));
    }

    @Test
    public void givenInvalidWebpImageWhenUploadImageThenReturnFalse() {
        String fileName = "pantalon.svg";
        assertFalse(this.trippoService.validateExtension(fileName));
    }
}
