package com.outfitlab.project.infrastructure;

import com.outfitlab.project.domain.entities.TripoModel;
import com.outfitlab.project.domain.repositories.TripoModelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrippoControllerServiceTest {

    @Mock
    private TrippoService trippoService;

    @Mock
    private TripoModelRepository tripoModelRepository;

    @InjectMocks
    private TrippoControllerService controllerService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidImageWhenUploadAndProcessThenReturnSavedModel() throws Exception {

        MockMultipartFile imageFile = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "data".getBytes(StandardCharsets.UTF_8)
        );

        Map<String, String> uploadData = new HashMap<>();
        uploadData.put("minioImagePath", "path/to/image.jpg");

        TripoModel modelFromDb = new TripoModel();
        modelFromDb.setTaskId("1234");

        TripoModel savedModel = new TripoModel();
        savedModel.setTaskId("1234");
        savedModel.setMinioImagePath("path/to/image.jpg");

        when(trippoService.uploadImageToTrippo(imageFile)).thenReturn(uploadData);
        when(trippoService.generateImageToModelTrippo(uploadData)).thenReturn("1234");
        when(tripoModelRepository.findByTaskId("1234")).thenReturn(Optional.of(modelFromDb));
        when(tripoModelRepository.save(any(TripoModel.class))).thenReturn(savedModel);

        TripoModel result = controllerService.uploadAndProcessImage(imageFile);

        assertEquals("1234", result.getTaskId());
        assertEquals("path/to/image.jpg", result.getMinioImagePath());
        verify(trippoService, times(1)).uploadImageToTrippo(imageFile);
        verify(trippoService, times(1)).generateImageToModelTrippo(uploadData);
        verify(tripoModelRepository, times(1)).findByTaskId("1234");
        verify(tripoModelRepository, times(1)).save(any(TripoModel.class));
    }

    @Test
    void givenEmptyImageWhenUploadAndProcessThenThrowException() {
        MockMultipartFile emptyFile = new MockMultipartFile("image", new byte[0]);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                controllerService.uploadAndProcessImage(emptyFile)
        );

        assertEquals("Archivo vacío", exception.getMessage());
    }

    @Test
    void givenNonExistingTaskIdWhenGetModelByTaskIdThenThrowException() {
        when(tripoModelRepository.findByTaskId("9999")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalStateException.class, () ->
                controllerService.getModelByTaskId("9999")
        );

        assertEquals("Modelo no encontrado", exception.getMessage());
    }

    @Test
    void givenExistingTaskIdWhenGetModelByTaskIdThenReturnModel() {
        TripoModel model = new TripoModel();
        model.setTaskId("1234");

        when(tripoModelRepository.findByTaskId("1234")).thenReturn(Optional.of(model));

        TripoModel result = controllerService.getModelByTaskId("1234");

        assertEquals("1234", result.getTaskId());
    }
}