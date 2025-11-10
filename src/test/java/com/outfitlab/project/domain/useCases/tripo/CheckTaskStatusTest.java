package com.outfitlab.project.domain.useCases.tripo;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.interfaces.repositories.TripoRepository;
import com.outfitlab.project.infrastructure.repositories.TripoRepositoryImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CheckTaskStatusTest {

    private TripoRepository tripoRepository = mock(TripoRepositoryImpl.class);
    private CheckTaskStatus checkTaskStatus = new CheckTaskStatus(tripoRepository);

    @Test
    public void givenValidTaskIdWhenExecuteThenReturnStatus() throws ErrorGlbGenerateTimeExpiredException, ErrorGenerateGlbException, ErrorWhenSleepException, ErrorReadJsonException {
        String taskId = "12345";
        String expectedStatus = "COMPLETED";

        when(tripoRepository.requestStatusGlbTripo(taskId)).thenReturn(expectedStatus);

        String result = checkTaskStatus.execute(taskId);
        assertEquals(expectedStatus, result);
        verify(tripoRepository, times(1)).requestStatusGlbTripo(taskId);
    }

    @Test
    public void givenRepositoryThrowTimeExpiredExceptionWhenExecuteThenPropagate() throws ErrorGlbGenerateTimeExpiredException, ErrorGenerateGlbException, ErrorWhenSleepException, ErrorReadJsonException {
        String taskId = "12345";

        when(tripoRepository.requestStatusGlbTripo(taskId))
                .thenThrow(new ErrorGlbGenerateTimeExpiredException("Tiempo expirado"));

        assertThrows(ErrorGlbGenerateTimeExpiredException.class, () -> checkTaskStatus.execute(taskId));
        verify(tripoRepository, times(1)).requestStatusGlbTripo(taskId);
    }

    @Test
    public void givenRepositoryWhenExecuteThenThrowSleepException() throws ErrorGlbGenerateTimeExpiredException, ErrorGenerateGlbException, ErrorWhenSleepException, ErrorReadJsonException {
        String taskId = "12345";

        when(tripoRepository.requestStatusGlbTripo(taskId)).thenThrow(new ErrorWhenSleepException("Error al esperar el hilo"));

        assertThrows(ErrorWhenSleepException.class, () -> checkTaskStatus.execute(taskId));
        verify(tripoRepository, times(1)).requestStatusGlbTripo(taskId);
    }

    @Test
    public void givenRepositoryThrowReadJsonExceptionWhenExecuteThenPropagate() throws ErrorGlbGenerateTimeExpiredException, ErrorGenerateGlbException, ErrorWhenSleepException, ErrorReadJsonException {
        String taskId = "12345";

        when(tripoRepository.requestStatusGlbTripo(taskId)).thenThrow(new ErrorReadJsonException("Error leyendo JSON"));

        assertThrows(ErrorReadJsonException.class, () -> checkTaskStatus.execute(taskId));
        verify(tripoRepository, times(1)).requestStatusGlbTripo(taskId);
    }

    @Test
    public void givenRepositoryThrowGenerateGlbExceptionWhenExecuteThenPropagate() throws ErrorGlbGenerateTimeExpiredException, ErrorGenerateGlbException, ErrorWhenSleepException, ErrorReadJsonException {
        String taskId = "12345";

        when(tripoRepository.requestStatusGlbTripo(taskId)).thenThrow(new ErrorGenerateGlbException("Error generando GLB"));

        assertThrows(ErrorGenerateGlbException.class, () -> checkTaskStatus.execute(taskId));
        verify(tripoRepository, times(1)).requestStatusGlbTripo(taskId);
    }
}

