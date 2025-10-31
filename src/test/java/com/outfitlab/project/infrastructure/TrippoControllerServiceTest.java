package com.outfitlab.project.infrastructure;

import com.outfitlab.project.infrastructure.model.TripoEntity;
import com.outfitlab.project.domain.interfaces.repositories.ITripoRepository;
import com.outfitlab.project.domain.service.TrippoControllerService;
import com.outfitlab.project.domain.service.TrippoService;
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
    private ITripoRepository tripoModelRepository;

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

        TripoEntity modelFromDb = new TripoEntity();
        modelFromDb.setTaskId("1234");

        TripoEntity savedModel = new TripoEntity();
        savedModel.setTaskId("1234");
        savedModel.setMinioImagePath("path/to/image.jpg");

        when(trippoService.uploadImageToTrippo(imageFile)).thenReturn(uploadData);
        when(trippoService.generateImageToModelTrippo(uploadData)).thenReturn("1234");
        when(tripoModelRepository.findByTaskId("1234")).thenReturn(Optional.of(modelFromDb));
        when(tripoModelRepository.save(any(TripoEntity.class))).thenReturn(savedModel);

        TripoEntity result = controllerService.uploadAndProcessImage(imageFile);

        assertEquals("1234", result.getTaskId());
        assertEquals("path/to/image.jpg", result.getMinioImagePath());
        verify(trippoService, times(1)).uploadImageToTrippo(imageFile);
        verify(trippoService, times(1)).generateImageToModelTrippo(uploadData);
        verify(tripoModelRepository, times(1)).findByTaskId("1234");
        verify(tripoModelRepository, times(1)).save(any(TripoEntity.class));
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
        TripoEntity model = new TripoEntity();
        model.setTaskId("1234");

        when(tripoModelRepository.findByTaskId("1234")).thenReturn(Optional.of(model));

        TripoEntity result = controllerService.getModelByTaskId("1234");

        assertEquals("1234", result.getTaskId());
    }
}