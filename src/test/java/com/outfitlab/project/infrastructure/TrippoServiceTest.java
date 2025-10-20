package com.outfitlab.project.infrastructure.trippo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.outfitlab.project.domain.models.TripoModel;
import com.outfitlab.project.domain.repositories.TripoModelRepository;
import com.outfitlab.project.infrastructure.trippo.usecases.CheckTaskStatus;
import com.outfitlab.project.infrastructure.trippo.usecases.GetImageResource;
import com.outfitlab.project.infrastructure.trippo.usecases.SaveFilesFromTask;
import com.outfitlab.project.infrastructure.trippo.usecases.UploadAndProcessImageToModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrippoServiceTest {

    private TripoModelRepository tripoModelRepository;
    private UploadAndProcessImageToModel uploadAndProcessImageToModel;
    private CheckTaskStatus checkTaskStatus;
    private SaveFilesFromTask saveFilesFromTask;
    private GetImageResource getImageResource;

    private TrippoService trippoService;

    @BeforeEach
    void setup() {
        tripoModelRepository = mock(TripoModelRepository.class);
        uploadAndProcessImageToModel = mock(UploadAndProcessImageToModel.class);
        checkTaskStatus = mock(CheckTaskStatus.class);
        saveFilesFromTask = mock(SaveFilesFromTask.class);
        getImageResource = mock(GetImageResource.class);

        trippoService = new TrippoService(
                tripoModelRepository,
                uploadAndProcessImageToModel,
                checkTaskStatus,
                saveFilesFromTask,
                getImageResource
        );
    }

    // ------------------- uploadAndProcessImage -------------------

    @Test
    void uploadAndProcessImageDelegatesToUseCase() throws Exception {
        MultipartFile file = new MockMultipartFile("image", "foto.jpg", "image/jpeg", "fake content".getBytes());
        TripoModel mockModel = new TripoModel();
        when(uploadAndProcessImageToModel.uploadAndProcessImageToModel(file)).thenReturn(mockModel);

        TripoModel result = trippoService.uploadAndProcessImage(file);

        assertEquals(mockModel, result);
        verify(uploadAndProcessImageToModel, times(1)).uploadAndProcessImageToModel(file);
    }

    // ------------------- getModelByTaskId -------------------

    @Test
    void getModelByTaskIdReturnsModelIfExists() {
        TripoModel mockModel = new TripoModel();
        when(tripoModelRepository.findByTaskId("task123")).thenReturn(Optional.of(mockModel));

        TripoModel result = trippoService.getModelByTaskId("task123");

        assertEquals(mockModel, result);
        verify(tripoModelRepository, times(1)).findByTaskId("task123");
    }

    @Test
    void getModelByTaskIdThrowsIfNotFound() {
        when(tripoModelRepository.findByTaskId("missing")).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> trippoService.getModelByTaskId("missing"));
    }

    // ------------------- checkTaskStatus -------------------

    @Test
    void checkTaskStatusDelegatesToUseCase() throws JsonProcessingException, InterruptedException {
        Map<String, String> expected = new HashMap<>();
        expected.put("status", "completed");

        when(checkTaskStatus.checkTaskStatus("task123")).thenReturn(expected);

        Map<String, String> result = trippoService.checkTaskStatus("task123");

        assertEquals(expected, result);
        verify(checkTaskStatus, times(1)).checkTaskStatus("task123");
    }

    // ------------------- saveFilesFromTask -------------------

    @Test
    void saveFilesFromTaskDelegatesToUseCase() throws IOException {
        Map<String, String> taskResponse = Map.of("taskId", "123");
        Map<String, String> expected = Map.of("resultUrl", "https://s3.fake/model.glb");

        when(saveFilesFromTask.saveFilesFromTask(taskResponse)).thenReturn(expected);

        Map<String, String> result = trippoService.saveFilesFromTask(taskResponse);

        assertEquals(expected, result);
        verify(saveFilesFromTask, times(1)).saveFilesFromTask(taskResponse);
    }

    // ------------------- getImageResource -------------------

    @Test
    void getImageResourceDelegatesToUseCase() throws IOException {
        MultipartFile file = new MockMultipartFile("image", "foto.jpg", "image/jpeg", "fake content".getBytes());
        ByteArrayResource mockResource = new ByteArrayResource("fake content".getBytes());

        when(getImageResource.getImageResource(file)).thenReturn(mockResource);

        ByteArrayResource result = trippoService.getImageResource(file);

        assertEquals(mockResource, result);
        verify(getImageResource, times(1)).getImageResource(file);
    }
}