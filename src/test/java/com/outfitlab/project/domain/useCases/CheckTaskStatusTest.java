package com.outfitlab.project.domain.useCases;

import com.outfitlab.project.domain.exceptions.ErrorGlbGenerateTimeExpiredException;
import com.outfitlab.project.domain.exceptions.ErrorReadJsonException;
import com.outfitlab.project.domain.exceptions.ErrorWhenSleepException;
import com.outfitlab.project.domain.useCases.tripo.*;
import com.outfitlab.project.domain.interfaces.repositories.ITripoRepository;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CheckTaskStatusTest {

    private ITripoRepository tripoRepositoryMock;
    private CheckTaskStatus checkTaskStatus;

    @Before
    public void setUp() {
        tripoRepositoryMock = mock(ITripoRepository.class);
        checkTaskStatus = new CheckTaskStatus(tripoRepositoryMock);
    }

    @Test
    public void ejecutarDeberiaDevolverStatus_cuandoRepositorioDevuelveStatus() throws ErrorGlbGenerateTimeExpiredException, ErrorWhenSleepException, ErrorReadJsonException {
        String taskId = "TASK123";
        String statusMock = "COMPLETED";

        when(tripoRepositoryMock.peticionStatusGlbTripo(taskId)).thenReturn(statusMock);

        String resultado = checkTaskStatus.execute(taskId);

        assertNotNull(resultado);
        assertEquals("COMPLETED", resultado);

        verify(tripoRepositoryMock, times(1)).peticionStatusGlbTripo(taskId);
    }

    @Test(expected = ErrorGlbGenerateTimeExpiredException.class)
    public void ejecutarDeberiaLanzarErrorGlbGenerateTimeExpiredException_cuandoRepositorioLanzaErrorGlbGenerateTimeExpiredException() throws ErrorGlbGenerateTimeExpiredException, ErrorWhenSleepException, ErrorReadJsonException {
        String taskId = "TASK123";

        when(tripoRepositoryMock.peticionStatusGlbTripo(taskId)).thenThrow(new ErrorGlbGenerateTimeExpiredException(""));

        checkTaskStatus.execute(taskId);
    }

    @Test(expected = ErrorWhenSleepException.class)
    public void ejecutarDeberiaLanzarErrorWhenSleepException_cuandoRepositorioLanzaErrorWhenSleepException() throws ErrorGlbGenerateTimeExpiredException, ErrorWhenSleepException, ErrorReadJsonException {
        String taskId = "TASK123";

        when(tripoRepositoryMock.peticionStatusGlbTripo(taskId)).thenThrow(new ErrorWhenSleepException(""));

        checkTaskStatus.execute(taskId);
    }

    @Test(expected = ErrorReadJsonException.class)
    public void ejecutarDeberiaLanzarErrorReadJsonException_cuandoRepositorioLanzaErrorReadJsonException() throws ErrorGlbGenerateTimeExpiredException, ErrorWhenSleepException, ErrorReadJsonException {
        String taskId = "TASK123";

        when(tripoRepositoryMock.peticionStatusGlbTripo(taskId)).thenThrow(new ErrorReadJsonException(""));

        checkTaskStatus.execute(taskId);
    }
}

