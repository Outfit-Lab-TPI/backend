package com.outfitlab.project.domain.service;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.model.TripoModel;
import com.outfitlab.project.domain.useCases.tripo.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TrippoServiceTest {

    private UploadImageToTripo uploadImageToTripoMock;
    private UploadImageToAws uploadImageToAwsMock;
    private GenerateImageToModelTrippo generateImageToModelTrippoMock;
    private SaveTripoModel saveTripoModelMock;
    private CheckTaskStatus checkTaskStatusMock;
    private UpdateTripoModel updateTripoModelMock;
    private FindTripoModelByTaskid findTripoModelByTaskidMock;
    private TrippoService trippoService;

    @Before
    public void setUp() {
        uploadImageToTripoMock = mock(UploadImageToTripo.class);
        uploadImageToAwsMock = mock(UploadImageToAws.class);
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
    public void procesarYEnviarATripoDeberiaEjecutarFlujoCompleto_cuandoArchivoValido() throws ErroBytesException, ErrorReadJsonException, ErrorUploadImageToTripo, ErrorGenerateGlbException, ErrorGlbGenerateTimeExpiredException, ErrorWhenSleepException, ErrorTripoEntityNotFound {
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

        TripoModel resultado = null;
        try {
            resultado = trippoService.procesarYEnviarATripo(file);
        } catch (Exception e) {
            fail("No se esperaba excepción: " + e.getMessage());
        } catch (FileEmptyException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(resultado);
        assertEquals(TripoModel.ModelStatus.COMPLETED, resultado.getStatus());
        assertEquals("task123", resultado.getTaskId());
    }

    @Test
    public void procesarYEnviarATripoDeberiaLanzarFileEmptyException_cuandoArchivoVacio() throws ErroBytesException, ErrorReadJsonException, ErrorUploadImageToTripo, ErrorGenerateGlbException, ErrorGlbGenerateTimeExpiredException, ErrorWhenSleepException, ErrorTripoEntityNotFound {
        MultipartFile file = new MockMultipartFile("file", new byte[]{});

        try {
            trippoService.procesarYEnviarATripo(file);
            fail("Se esperaba FileEmptyException");
        } catch (FileEmptyException e) {
            assertEquals("Archivo vacío", e.getMessage());
        } catch (Exception e) {
            fail("Excepción inesperada: " + e.getMessage());
        }
    }

    @Test
    public void procesarYEnviarATripoDeberiaLanzarErroBytesException_cuandoUploadImageToTripoFalla() throws ErroBytesException, ErrorReadJsonException, ErrorUploadImageToTripo, ErrorGenerateGlbException, ErrorGlbGenerateTimeExpiredException, ErrorWhenSleepException, ErrorTripoEntityNotFound  {
        MultipartFile file = new MockMultipartFile("file", "imagen.png", "image/png", new byte[]{1});
        when(uploadImageToTripoMock.execute(file)).thenThrow(new ErroBytesException("Error en bytes"));

        try {
            trippoService.procesarYEnviarATripo(file);
            fail("Se esperaba ErroBytesException");
        } catch (ErroBytesException e) {
            assertEquals("Error en bytes", e.getMessage());
        } catch (Exception e) {
            fail("Excepción inesperada: " + e.getMessage());
        } catch (FileEmptyException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void procesarYEnviarATripoDeberiaLanzarErrorReadJsonException_cuandoUploadImageToTripoFalla() throws ErroBytesException, ErrorReadJsonException, ErrorUploadImageToTripo, ErrorGenerateGlbException, ErrorGlbGenerateTimeExpiredException, ErrorWhenSleepException, ErrorTripoEntityNotFound  {
        MultipartFile file = new MockMultipartFile("file", "imagen.png", "image/png", new byte[]{1});
        when(uploadImageToTripoMock.execute(file)).thenThrow(new ErrorReadJsonException("Error leyendo JSON"));

        try {
            trippoService.procesarYEnviarATripo(file);
            fail("Se esperaba ErrorReadJsonException");
        } catch (ErrorReadJsonException e) {
            assertEquals("Error leyendo JSON", e.getMessage());
        } catch (Exception e) {
            fail("Excepción inesperada: " + e.getMessage());
        } catch (FileEmptyException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void procesarYEnviarATripoDeberiaLanzarErrorUploadImageToTripo_cuandoUploadImageToTripoFalla() throws ErroBytesException, ErrorReadJsonException, ErrorUploadImageToTripo, ErrorGenerateGlbException, ErrorGlbGenerateTimeExpiredException, ErrorWhenSleepException, ErrorTripoEntityNotFound  {
        MultipartFile file = new MockMultipartFile("file", "imagen.png", "image/png", new byte[]{1});
        when(uploadImageToTripoMock.execute(file)).thenThrow(new ErrorUploadImageToTripo("Error subida"));

        try {
            trippoService.procesarYEnviarATripo(file);
            fail("Se esperaba ErrorUploadImageToTripo");
        } catch (ErrorUploadImageToTripo e) {
            assertEquals("Error subida", e.getMessage());
        } catch (Exception e) {
            fail("Excepción inesperada: " + e.getMessage());
        } catch (FileEmptyException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void procesarYEnviarATripoDeberiaLanzarErrorGenerateGlbException_cuandoGenerateImageFalla() throws ErroBytesException, ErrorReadJsonException, ErrorUploadImageToTripo, ErrorGenerateGlbException, ErrorGlbGenerateTimeExpiredException, ErrorWhenSleepException, ErrorTripoEntityNotFound  {
        MultipartFile file = new MockMultipartFile("file", "imagen.png", "image/png", new byte[]{1});
        Map<String, String> uploadData = new HashMap<>();
        uploadData.put("originalFilename", "imagen.png");
        uploadData.put("fileExtension", "png");
        uploadData.put("imageToken", "token123");
        when(uploadImageToTripoMock.execute(file)).thenReturn(uploadData);
        when(uploadImageToAwsMock.execute(file)).thenReturn("minioPath");
        when(generateImageToModelTrippoMock.execute(anyMap())).thenThrow(new ErrorGenerateGlbException("Error GLB"));

        try {
            trippoService.procesarYEnviarATripo(file);
            fail("Se esperaba ErrorGenerateGlbException");
        } catch (ErrorGenerateGlbException e) {
            assertEquals("Error GLB", e.getMessage());
        } catch (Exception e) {
            fail("Excepción inesperada: " + e.getMessage());
        } catch (FileEmptyException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void procesarYEnviarATripoDeberiaLanzarErrorGlbGenerateTimeExpiredException_cuandoCheckTaskStatusFalla() throws ErroBytesException, ErrorReadJsonException, ErrorUploadImageToTripo, ErrorGenerateGlbException, ErrorGlbGenerateTimeExpiredException, ErrorWhenSleepException, ErrorTripoEntityNotFound  {
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

        try {
            trippoService.procesarYEnviarATripo(file);
            fail("Se esperaba ErrorGlbGenerateTimeExpiredException");
        } catch (ErrorGlbGenerateTimeExpiredException e) {
            assertEquals("Tiempo expirado", e.getMessage());
        } catch (Exception e) {
            fail("Excepción inesperada: " + e.getMessage());
        } catch (FileEmptyException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void procesarYEnviarATripoDeberiaLanzarErrorWhenSleepException_cuandoCheckTaskStatusFalla() throws ErroBytesException, ErrorReadJsonException, ErrorUploadImageToTripo, ErrorGenerateGlbException, ErrorGlbGenerateTimeExpiredException, ErrorWhenSleepException, ErrorTripoEntityNotFound  {
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

        try {
            trippoService.procesarYEnviarATripo(file);
            fail("Se esperaba ErrorWhenSleepException");
        } catch (ErrorWhenSleepException e) {
            assertEquals("Sleep error", e.getMessage());
        } catch (Exception e) {
            fail("Excepción inesperada: " + e.getMessage());
        } catch (FileEmptyException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void procesarYEnviarATripoDeberiaLanzarErrorTripoEntityNotFound_cuandoUpdateTripoFalla() throws ErroBytesException, ErrorReadJsonException, ErrorUploadImageToTripo, ErrorGenerateGlbException, ErrorGlbGenerateTimeExpiredException, ErrorWhenSleepException, ErrorTripoEntityNotFound  {
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
        when(updateTripoModelMock.execute(any(TripoModel.class))).thenThrow(new ErrorTripoEntityNotFound("No encontrado"));

        try {
            trippoService.procesarYEnviarATripo(file);
            fail("Se esperaba ErrorTripoEntityNotFound");
        } catch (ErrorTripoEntityNotFound e) {
            assertEquals("No encontrado", e.getMessage());
        } catch (Exception e) {
            fail("Excepción inesperada: " + e.getMessage());
        } catch (FileEmptyException e) {
            throw new RuntimeException(e);
        }
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

