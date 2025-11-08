package com.outfitlab.project.domain.useCases;

import com.outfitlab.project.domain.exceptions.ErrorGlbGenerateTimeExpiredException;
import com.outfitlab.project.domain.exceptions.ErrorReadJsonException;
import com.outfitlab.project.domain.exceptions.ErrorWhenSleepException;
import com.outfitlab.project.domain.interfaces.repositories.TripoRepository;
import com.outfitlab.project.domain.useCases.tripo.CheckTaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CheckTaskStatusTest {

    private TripoRepository tripoRepositoryMock;
    private CheckTaskStatus checkTaskStatus;

    @BeforeEach
    public void setUp() {
        tripoRepositoryMock = mock(TripoRepository.class);
        checkTaskStatus = new CheckTaskStatus(tripoRepositoryMock);
    }

    /*@Test
    public void ejecutarDeberiaDevolverStatus_cuandoRepositorioDevuelveStatus() throws ErrorGlbGenerateTimeExpiredException, ErrorWhenSleepException, ErrorReadJsonException {
        String taskId = "TASK123";
        String statusMock = "COMPLETED";

        when(tripoRepositoryMock.requestStatusGlbTripo(taskId)).thenReturn(statusMock);

        String resultado = checkTaskStatus.execute(taskId);

        assertNotNull(resultado);
        assertEquals("COMPLETED", resultado);

        verify(tripoRepositoryMock, times(1)).requestStatusGlbTripo(taskId);
    }

    @Test
    public void ejecutarDeberiaLanzarErrorGlbGenerateTimeExpiredException_cuandoRepositorioLanzaErrorGlbGenerateTimeExpiredException() throws ErrorGlbGenerateTimeExpiredException, ErrorWhenSleepException, ErrorReadJsonException {
        String taskId = "TASK123";

        when(tripoRepositoryMock.requestStatusGlbTripo(taskId))
                .thenThrow(new ErrorGlbGenerateTimeExpiredException(""));

        assertThrows(ErrorGlbGenerateTimeExpiredException.class, () -> {
            checkTaskStatus.execute(taskId);
        });
    }

    @Test
    public void ejecutarDeberiaLanzarErrorWhenSleepException_cuandoRepositorioLanzaErrorWhenSleepException() throws ErrorGlbGenerateTimeExpiredException, ErrorWhenSleepException, ErrorReadJsonException {
        String taskId = "TASK123";

        when(tripoRepositoryMock.requestStatusGlbTripo(taskId))
                .thenThrow(new ErrorWhenSleepException(""));

        assertThrows(ErrorWhenSleepException.class, () -> {
            checkTaskStatus.execute(taskId);
        });
    }

    @Test
    public void ejecutarDeberiaLanzarErrorReadJsonException_cuandoRepositorioLanzaErrorReadJsonException() throws ErrorGlbGenerateTimeExpiredException, ErrorWhenSleepException, ErrorReadJsonException {
        String taskId = "TASK123";

        when(tripoRepositoryMock.requestStatusGlbTripo(taskId))
                .thenThrow(new ErrorReadJsonException(""));

        assertThrows(ErrorReadJsonException.class, () -> {
            checkTaskStatus.execute(taskId);
        });
    }*/
}
