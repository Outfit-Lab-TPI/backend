package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.models.TripoModel;
import com.outfitlab.project.infrastructure.trippo.TrippoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TrippoControllerTest {

    @Mock
    private TrippoService trippoService;

    @InjectMocks
    private TrippoController trippoController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(trippoController).build();
    }

    @Test
    void givenValidImageWhenUploadImageThenReturnOk() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "dummy data".getBytes());

        TripoModel model = new TripoModel(
                "task123",
                null,
                "test.jpg",
                "jpg",
                "path/to/image.jpg",
                TripoModel.ModelStatus.PENDING
        );
        model.setId(1L);

        when(trippoService.uploadAndProcessImage(any())).thenReturn(model);

        mockMvc.perform(multipart("/api/trippo/upload/image")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.taskId").value("task123"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.originalFilename").value("test.jpg"))
                .andExpect(jsonPath("$.fileExtension").value("jpg"))
                .andExpect(jsonPath("$.minioImagePath").value("path/to/image.jpg"))
                .andExpect(jsonPath("$.message").value("Imagen subida exitosamente. El modelo 3D se está generando."));

        verify(trippoService, times(1)).uploadAndProcessImage(any());
    }

    @Test
    void givenEmptyImageWhenUploadImageThenReturnBadRequest() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "image", "", "image/jpeg", new byte[0]);

        when(trippoService.uploadAndProcessImage(any()))
                .thenThrow(new IllegalArgumentException("Archivo vacío"));

        mockMvc.perform(multipart("/api/trippo/upload/image")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Archivo vacío"));
    }

    @Test
    void givenTaskIdWhenGetModelStatusThenReturnOk() throws Exception {
        TripoModel model = new TripoModel(
                "task123",
                null,
                "test.jpg",
                "jpg",
                "path/to/image.jpg",
                TripoModel.ModelStatus.PENDING
        );
        model.setId(1L);

        when(trippoService.getModelByTaskId("task123")).thenReturn(model);

        mockMvc.perform(get("/api/trippo/models/task123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.taskId").value("task123"));
    }

    @Test
    void givenInvalidTaskIdWhenGetModelStatusThenReturnNotFound() throws Exception {
        when(trippoService.getModelByTaskId("invalid"))
                .thenThrow(new IllegalStateException("Modelo no encontrado"));

        mockMvc.perform(get("/api/trippo/models/invalid"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Modelo no encontrado"));
    }
}