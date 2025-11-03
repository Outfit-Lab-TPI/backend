package com.outfitlab.project.presentation;

class TrippoControllerTest {
/*
    @Mock
    private TrippoControllerService trippoControllerService;

    @InjectMocks
    private TrippoController trippoController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(trippoController).build();
    }
/*
    @Test
    void givenValidImageWhenUploadImageThenReturnOk() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "dummy data".getBytes());

        TripoEntity model = TripoEntity.builder()
                .id(1L)
                .taskId("task123")
                .status(TripoEntity.ModelStatus.PENDING)
                .originalFilename("test.jpg")
                .fileExtension("jpg")
                .minioImagePath("path/to/image.jpg")
                .build();

        when(trippoControllerService.uploadAndProcessImage(any())).thenReturn(model);

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

        verify(trippoControllerService, times(1)).uploadAndProcessImage(any());
    }

    @Test
    void givenEmptyImageWhenUploadImageThenReturnBadRequest() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "image", "", "image/jpeg", new byte[0]);

        when(trippoControllerService.uploadAndProcessImage(any()))
                .thenThrow(new IllegalArgumentException("Archivo vacío"));

        mockMvc.perform(multipart("/api/trippo/upload/image")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Archivo vacío"));
    }

    @Test
    void givenTaskIdWhenGetModelStatusThenReturnOk() throws Exception {
        TripoEntity model = TripoEntity.builder()
                .id(1L)
                .taskId("task123")
                .status(TripoEntity.ModelStatus.PENDING)
                .originalFilename("test.jpg")
                .fileExtension("jpg")
                .minioImagePath("path/to/image.jpg")
                .build();

        when(trippoControllerService.getModelByTaskId("task123")).thenReturn(model);

        mockMvc.perform(get("/api/trippo/models/task123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.taskId").value("task123"));
    }

    @Test
    void givenInvalidTaskIdWhenGetModelStatusThenReturnNotFound() throws Exception {
        when(trippoControllerService.getModelByTaskId("invalid"))
                .thenThrow(new IllegalStateException("Modelo no encontrado"));

        mockMvc.perform(get("/api/trippo/models/invalid"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Modelo no encontrado"));
    }*/
}