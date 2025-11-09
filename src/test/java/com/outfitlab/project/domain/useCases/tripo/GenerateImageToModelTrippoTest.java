package com.outfitlab.project.domain.useCases.tripo;

import com.outfitlab.project.domain.exceptions.ErrorGenerateGlbException;
import com.outfitlab.project.domain.exceptions.ErrorReadJsonException;
import com.outfitlab.project.domain.interfaces.repositories.TripoRepository;
import com.outfitlab.project.infrastructure.repositories.TripoRepositoryImpl;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GenerateImageToModelTrippoTest {

    private TripoRepository tripoRepository = mock(TripoRepositoryImpl.class);
    private GenerateImageToModelTrippo generateImageToModelTrippo = new GenerateImageToModelTrippo(tripoRepository);

    @Test
    public void givenValidUploadDataWhenExecuteThenReturnSuccessResponse() throws ErrorGenerateGlbException, ErrorReadJsonException {
        Map<String, Object> uploadData = new HashMap<>();
        uploadData.put("imageUrl", "https://testeo.com/image.png");
        String idResult = "GLB_TASK_ID_14125";

        when(tripoRepository.requestGenerateGlbToTripo(uploadData)).thenReturn(idResult);

        String result = generateImageToModelTrippo.execute(uploadData);

        assertNotNull(result);
        assertEquals(idResult, result);
        verify(tripoRepository, times(1)).requestGenerateGlbToTripo(uploadData);
    }

    @Test
    public void givenValidDataWhenGenerateGlbThenThrowErrorGenerateGlbException() throws ErrorGenerateGlbException, ErrorReadJsonException {
        Map<String, Object> uploadData = new HashMap<>();
        uploadData.put("imageUrl", "https://testeo.com/image.png");

        when(tripoRepository.requestGenerateGlbToTripo(uploadData))
                .thenThrow(new ErrorGenerateGlbException("Error al generar GLB"));

        assertThrows(ErrorGenerateGlbException.class, () -> generateImageToModelTrippo.execute(uploadData));
        verify(tripoRepository, times(1)).requestGenerateGlbToTripo(uploadData);
    }

    @Test
    public void givenValidDataWhenGenerateGlbThenThrowErrorReadJsonException() throws ErrorGenerateGlbException, ErrorReadJsonException {
        Map<String, Object> uploadData = new HashMap<>();
        uploadData.put("imageUrl", "https://testeo.com/image.png");

        when(tripoRepository.requestGenerateGlbToTripo(uploadData))
                .thenThrow(new ErrorReadJsonException("Error al leer JSON"));

        assertThrows(ErrorReadJsonException.class, () -> generateImageToModelTrippo.execute(uploadData));
        verify(tripoRepository, times(1)).requestGenerateGlbToTripo(uploadData);
    }
}

