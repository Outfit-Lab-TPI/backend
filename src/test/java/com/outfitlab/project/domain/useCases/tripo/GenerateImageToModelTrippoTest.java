package com.outfitlab.project.domain.useCases.tripo;

import com.outfitlab.project.domain.exceptions.ErrorGenerateGlbException;
import com.outfitlab.project.domain.exceptions.ErrorReadJsonException;
import com.outfitlab.project.domain.interfaces.repositories.TripoRepository;
import com.outfitlab.project.infrastructure.repositories.TripoRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GenerateImageToModelTrippoTest {

    private TripoRepository tripoRepository = mock(TripoRepositoryImpl.class);
    private GenerateImageToModelTrippo generateImageToModelTrippo;

    private final String IMAGE_URL = "https://testeo.com/image.png";
    private final String SUCCESS_TASK_ID = "GLB_TASK_ID_14125";
    private final String KEY_IMAGE_URL = "imageUrl";
    private Map<String, Object> validUploadData;

    @BeforeEach
    void setUp() {
        generateImageToModelTrippo = new GenerateImageToModelTrippo(tripoRepository);
        validUploadData = new HashMap<>();
        validUploadData.put(KEY_IMAGE_URL, IMAGE_URL);
    }


    @Test
    public void shouldReturnTaskIdWhenGlbGenerationIsSuccessful() throws Exception {
        givenRepositoryReturnsTaskId(validUploadData, SUCCESS_TASK_ID);

        String result = whenExecuteGenerateGlb(validUploadData);

        thenReturnedResultIsCorrect(result, SUCCESS_TASK_ID);
        thenRepositoryWasCalledOnce(validUploadData);
    }

    @Test
    public void shouldPropagateErrorGenerateGlbExceptionWhenTripoFails() {
        givenRepositoryThrowsException(validUploadData, new ErrorGenerateGlbException("Error al generar GLB"));

        thenExecutionThrowsException(validUploadData, ErrorGenerateGlbException.class);
    }

    @Test
    public void shouldPropagateErrorReadJsonExceptionWhenApiResponseIsInvalid() {
        givenRepositoryThrowsException(validUploadData, new ErrorReadJsonException("Error al leer JSON"));

        thenExecutionThrowsException(validUploadData, ErrorReadJsonException.class);
    }


    //privadoss
    private void givenRepositoryReturnsTaskId(Map<String, Object> data, String taskId) throws ErrorGenerateGlbException, ErrorReadJsonException {
        when(tripoRepository.requestGenerateGlbToTripo(data)).thenReturn(taskId);
    }

    private void givenRepositoryThrowsException(Map<String, Object> data, Exception exception) {
        try {
            doThrow(exception).when(tripoRepository).requestGenerateGlbToTripo(data);
        } catch (ErrorGenerateGlbException | ErrorReadJsonException e) {
        }
    }


    private String whenExecuteGenerateGlb(Map<String, Object> data) throws ErrorGenerateGlbException, ErrorReadJsonException {
        return generateImageToModelTrippo.execute(data);
    }

    private void thenReturnedResultIsCorrect(String result, String expectedTaskId) {
        assertNotNull(result, "El resultado no debe ser nulo.");
        assertEquals(expectedTaskId, result, "El ID de tarea devuelto debe coincidir con el esperado.");
    }

    private void thenExecutionThrowsException(Map<String, Object> data, Class<? extends Exception> expectedException) {
        assertThrows(expectedException, () -> generateImageToModelTrippo.execute(data));

        thenRepositoryWasCalledOnce(data);
    }

    private void thenRepositoryWasCalledOnce(Map<String, Object> data) {
        verify(tripoRepository, times(1)).requestGenerateGlbToTripo(data);
    }
}