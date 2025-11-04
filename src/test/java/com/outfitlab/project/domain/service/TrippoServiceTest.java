package com.outfitlab.project.domain.service;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.model.TripoModel;
import com.outfitlab.project.domain.useCases.tripo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrippoServiceTest {

    private UploadImageToTripo uploadImageToTripoMock;
    private SaveImage uploadImageToAwsMock;
    private GenerateImageToModelTrippo generateImageToModelTrippoMock;
    private SaveTripoModel saveTripoModelMock;
    private CheckTaskStatus checkTaskStatusMock;
    private UpdateTripoModel updateTripoModelMock;
    private FindTripoModelByTaskid findTripoModelByTaskidMock;
    private TrippoService trippoService;

    @BeforeEach
    public void setUp() {
        uploadImageToTripoMock = mock(UploadImageToTripo.class);
        uploadImageToAwsMock = mock(SaveImage.class);
        generateImageToModelTrippoMock = mock(GenerateImageToModelTrippo.class);
        saveTripoModelMock = mock(SaveTripoModel.class);
        checkTaskStatusMock = mock(CheckTaskStatus.class);
        updateTripoModelMock = mock(UpdateTripoModel.class);
        findTripoModelByTaskidMock = mock(FindTripoModelByTaskid.class);

        trippoService = new TrippoService(
                uploadImageToTripoMock, uploadImageToAwsMock, generateImageToModelTrippoMock,
                saveTripoModelMock, checkTaskStatusMock, updateTripoModelMock, findTripoModelByTaskidMock
        );
    }

    @Test
    public void procesarYEnviarATripoDeberiaEjecutarFlujoCompleto_cuandoArchivoValido() throws Exception, ErrorGenerateGlbException, ErrorGlbGenerateTimeExpiredException, FileEmptyException, ErrorTripoEntityNotFoundException, ErrorWhenSleepException, ErroBytesException, ErrorReadJsonException, ErrorUploadImageToTripoException {
        MultipartFile file = new MockMultipartFile("file", "imagen.png", "image/png", new byte[]{1,2,3});

        Map<String, String> uploadData = new HashMap<>();
        uploadData.put("originalFilename", "imagen.png");
        uploadData.put("fileExtension", "png");
        uploadData.put("imageToken", "token123");

        when(uploadImageToTripoMock.execute(file)).thenReturn(uploadData);
        when(uploadImageToAwsMock.execute(file)).thenReturn("minioPath");
        when(generateImageToModelTrippoMock.execute(anyMap())).thenReturn("task123");
        doReturn(new TripoModel()).when(saveTripoModelMock).execute(any(TripoModel.class));
        when(checkTaskStatusMock.execute("task123")).thenReturn("urlGlb");
        when(updateTripoModelMock.execute(any(TripoModel.class))).thenAnswer(i -> i.getArguments()[0]);

        TripoModel resultado = trippoService.procesarYEnviarATripo(file);

        assertNotNull(resultado);
        assertEquals(TripoModel.ModelStatus.COMPLETED, resultado.getStatus());
        assertEquals("task123", resultado.getTaskId());
    }

    @Test
    public void procesarYEnviarATripoDeberiaLanzarFileEmptyException_cuandoArchivoVacio() {
        MultipartFile file = new MockMultipartFile("file", new byte[]{});

        FileEmptyException ex = assertThrows(FileEmptyException.class,
                () -> trippoService.procesarYEnviarATripo(file));

        assertEquals("Archivo vacío", ex.getMessage());
    }

    @Test
    public void procesarYEnviarATripoDeberiaLanzarErroBytesException_cuandoUploadImageToTripoFalla() throws Exception, ErroBytesException, ErrorReadJsonException, ErrorUploadImageToTripoException {
        MultipartFile file = new MockMultipartFile("file", "imagen.png", "image/png", new byte[]{1});
        when(uploadImageToTripoMock.execute(file)).thenThrow(new ErroBytesException("Error en bytes"));

        ErroBytesException ex = assertThrows(ErroBytesException.class,
                () -> trippoService.procesarYEnviarATripo(file));

        assertEquals("Error en bytes", ex.getMessage());
    }

    @Test
    public void procesarYEnviarATripoDeberiaLanzarErrorReadJsonException_cuandoUploadImageToTripoFalla() throws Exception, ErroBytesException, ErrorReadJsonException, ErrorUploadImageToTripoException {
        MultipartFile file = new MockMultipartFile("file", "imagen.png", "image/png", new byte[]{1});
        when(uploadImageToTripoMock.execute(file)).thenThrow(new ErrorReadJsonException("Error leyendo JSON"));

        ErrorReadJsonException ex = assertThrows(ErrorReadJsonException.class,
                () -> trippoService.procesarYEnviarATripo(file));

        assertEquals("Error leyendo JSON", ex.getMessage());
    }

    @Test
    public void procesarYEnviarATripoDeberiaLanzarErrorUploadImageToTripo_cuandoUploadImageToTripoFalla() throws Exception, ErroBytesException, ErrorReadJsonException, ErrorUploadImageToTripoException {
        MultipartFile file = new MockMultipartFile("file", "imagen.png", "image/png", new byte[]{1});
        when(uploadImageToTripoMock.execute(file)).thenThrow(new ErrorUploadImageToTripoException("Error subida"));

        ErrorUploadImageToTripoException ex = assertThrows(ErrorUploadImageToTripoException.class,
                () -> trippoService.procesarYEnviarATripo(file));

        assertEquals("Error subida", ex.getMessage());
    }

    @Test
    public void procesarYEnviarATripoDeberiaLanzarErrorGenerateGlbException_cuandoGenerateImageFalla() throws Exception, ErrorGenerateGlbException, ErrorReadJsonException, ErroBytesException, ErrorUploadImageToTripoException {
        MultipartFile file = new MockMultipartFile("file", "imagen.png", "image/png", new byte[]{1});
        Map<String, String> uploadData = new HashMap<>();
        uploadData.put("originalFilename", "imagen.png");
        uploadData.put("fileExtension", "png");
        uploadData.put("imageToken", "token123");
        when(uploadImageToTripoMock.execute(file)).thenReturn(uploadData);
        when(uploadImageToAwsMock.execute(file)).thenReturn("minioPath");
        when(generateImageToModelTrippoMock.execute(anyMap())).thenThrow(new ErrorGenerateGlbException("Error GLB"));

        ErrorGenerateGlbException ex = assertThrows(ErrorGenerateGlbException.class,
                () -> trippoService.procesarYEnviarATripo(file));

        assertEquals("Error GLB", ex.getMessage());
    }

    @Test
    public void procesarYEnviarATripoDeberiaLanzarErrorGlbGenerateTimeExpiredException_cuandoCheckTaskStatusFalla() throws Exception, ErrorGlbGenerateTimeExpiredException, ErrorWhenSleepException, ErrorReadJsonException, ErrorGenerateGlbException, ErroBytesException, ErrorUploadImageToTripoException {
        MultipartFile file = new MockMultipartFile("file", "imagen.png", "image/png", new byte[]{1});
        Map<String, String> uploadData = new HashMap<>();
        uploadData.put("originalFilename", "imagen.png");
        uploadData.put("fileExtension", "png");
        uploadData.put("imageToken", "token123");
        when(uploadImageToTripoMock.execute(file)).thenReturn(uploadData);
        when(uploadImageToAwsMock.execute(file)).thenReturn("minioPath");
        when(generateImageToModelTrippoMock.execute(anyMap())).thenReturn("task123");
        doReturn(new TripoModel()).when(saveTripoModelMock).execute(any(TripoModel.class));
        when(checkTaskStatusMock.execute("task123")).thenThrow(new ErrorGlbGenerateTimeExpiredException("Tiempo expirado"));

        ErrorGlbGenerateTimeExpiredException ex = assertThrows(ErrorGlbGenerateTimeExpiredException.class,
                () -> trippoService.procesarYEnviarATripo(file));

        assertEquals("Tiempo expirado", ex.getMessage());
    }

    @Test
    public void procesarYEnviarATripoDeberiaLanzarErrorWhenSleepException_cuandoCheckTaskStatusFalla() throws Exception, ErrorGlbGenerateTimeExpiredException, ErrorWhenSleepException, ErrorReadJsonException, ErrorGenerateGlbException, ErroBytesException, ErrorUploadImageToTripoException {
        MultipartFile file = new MockMultipartFile("file", "imagen.png", "image/png", new byte[]{1});
        Map<String, String> uploadData = new HashMap<>();
        uploadData.put("originalFilename", "imagen.png");
        uploadData.put("fileExtension", "png");
        uploadData.put("imageToken", "token123");
        when(uploadImageToTripoMock.execute(file)).thenReturn(uploadData);
        when(uploadImageToAwsMock.execute(file)).thenReturn("minioPath");
        when(generateImageToModelTrippoMock.execute(anyMap())).thenReturn("task123");
        doReturn(new TripoModel()).when(saveTripoModelMock).execute(any(TripoModel.class));
        when(checkTaskStatusMock.execute("task123")).thenThrow(new ErrorWhenSleepException("Sleep error"));

        ErrorWhenSleepException ex = assertThrows(ErrorWhenSleepException.class,
                () -> trippoService.procesarYEnviarATripo(file));

        assertEquals("Sleep error", ex.getMessage());
    }

    @Test
    public void procesarYEnviarATripoDeberiaLanzarErrorTripoEntityNotFound_cuandoUpdateTripoFalla() throws Exception, ErroBytesException, ErrorReadJsonException, ErrorUploadImageToTripoException, ErrorGenerateGlbException, ErrorGlbGenerateTimeExpiredException, ErrorWhenSleepException, ErrorTripoEntityNotFoundException {
        MultipartFile file = new MockMultipartFile("file", "imagen.png", "image/png", new byte[]{1});
        Map<String, String> uploadData = new HashMap<>();
        uploadData.put("originalFilename", "imagen.png");
        uploadData.put("fileExtension", "png");
        uploadData.put("imageToken", "token123");
        when(uploadImageToTripoMock.execute(file)).thenReturn(uploadData);
        when(uploadImageToAwsMock.execute(file)).thenReturn("minioPath");
        when(generateImageToModelTrippoMock.execute(anyMap())).thenReturn("task123");
        doReturn(new TripoModel()).when(saveTripoModelMock).execute(any(TripoModel.class));
        when(checkTaskStatusMock.execute("task123")).thenReturn("urlGlb");
        when(updateTripoModelMock.execute(any(TripoModel.class))).thenThrow(new ErrorTripoEntityNotFoundException("No encontrado"));

        ErrorTripoEntityNotFoundException ex = assertThrows(ErrorTripoEntityNotFoundException.class,
                () -> trippoService.procesarYEnviarATripo(file));

        assertEquals("No encontrado", ex.getMessage());
    }

    @Test
    public void buscarPorTaskidDeberiaDevolverTripoModel_cuandoExiste() {
        String taskId = "task123";
        TripoModel model = new TripoModel();
        when(findTripoModelByTaskidMock.execute(taskId)).thenReturn(model);

        TripoModel resultado = trippoService.buscarPorTaskid(taskId);

        assertNotNull(resultado);
        assertEquals(model, resultado);
    }

    @Test
    public void buscarPorTaskidDeberiaDevolverNull_cuandoNoExiste() {
        String taskId = "task404";
        when(findTripoModelByTaskidMock.execute(taskId)).thenReturn(null);

        TripoModel resultado = trippoService.buscarPorTaskid(taskId);

        assertNull(resultado);
    }
}
