package com.outfitlab.project.domain.useCases.tripo;

import com.outfitlab.project.domain.exceptions.ErrorGenerateGlbException;
import com.outfitlab.project.domain.exceptions.ErrorGlbGenerateTimeExpiredException;
import com.outfitlab.project.domain.exceptions.ErrorReadJsonException;
import com.outfitlab.project.domain.exceptions.ErrorWhenSleepException;
import com.outfitlab.project.domain.interfaces.repositories.TripoRepository;
import com.outfitlab.project.infrastructure.repositories.TripoRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CheckTaskStatusTest {

    private TripoRepository tripoRepository = mock(TripoRepositoryImpl.class);
    private CheckTaskStatus checkTaskStatus;
    private final String VALID_TASK_ID = "12345";
    private final String STATUS_COMPLETED = "COMPLETED";

    @BeforeEach
    void setUp() {
        checkTaskStatus = new CheckTaskStatus(tripoRepository);
    }


    @Test
    public void shouldReturnCompletedStatusWhenTaskIsSuccessful() throws Exception {
        givenRepositoryReturnsStatus(VALID_TASK_ID, STATUS_COMPLETED);

        String result = whenExecuteCheckTaskStatus(VALID_TASK_ID);

        thenReturnedStatusMatchesExpected(result, STATUS_COMPLETED);
        thenRepositoryWasCalledOnce(VALID_TASK_ID);
    }

    @Test
    public void shouldPropagateTimeExpiredExceptionWhenGlbGenerationTimeExpires() {
        givenRepositoryThrowsException(VALID_TASK_ID, new ErrorGlbGenerateTimeExpiredException("Tiempo expirado"));

        thenExecutionThrowsException(VALID_TASK_ID, ErrorGlbGenerateTimeExpiredException.class);
    }

    @Test
    public void shouldPropagateErrorWhenSleepExceptionWhenThreadWaitingFails() {
        givenRepositoryThrowsException(VALID_TASK_ID, new ErrorWhenSleepException("Error al esperar el hilo"));

        thenExecutionThrowsException(VALID_TASK_ID, ErrorWhenSleepException.class);
    }

    @Test
    public void shouldPropagateErrorReadJsonExceptionWhenApiResponseIsInvalid() {
        givenRepositoryThrowsException(VALID_TASK_ID, new ErrorReadJsonException("Error leyendo JSON"));

        thenExecutionThrowsException(VALID_TASK_ID, ErrorReadJsonException.class);
    }

    @Test
    public void shouldPropagateErrorGenerateGlbExceptionWhenGenerationFails() {
        givenRepositoryThrowsException(VALID_TASK_ID, new ErrorGenerateGlbException("Error generando GLB"));

        thenExecutionThrowsException(VALID_TASK_ID, ErrorGenerateGlbException.class);
    }


    private void givenRepositoryReturnsStatus(String taskId, String status) throws ErrorGenerateGlbException, ErrorGlbGenerateTimeExpiredException, ErrorWhenSleepException, ErrorReadJsonException {
        when(tripoRepository.requestStatusGlbTripo(taskId)).thenReturn(status);
    }

    private void givenRepositoryThrowsException(String taskId, Exception exception) {
        try {
            doThrow(exception).when(tripoRepository).requestStatusGlbTripo(taskId);
        } catch (Exception e) {
        }
    }

    private String whenExecuteCheckTaskStatus(String taskId) throws ErrorGlbGenerateTimeExpiredException, ErrorGenerateGlbException, ErrorWhenSleepException, ErrorReadJsonException {
        return checkTaskStatus.execute(taskId);
    }

    private void thenReturnedStatusMatchesExpected(String result, String expectedStatus) {
        assertEquals(expectedStatus, result);
    }

    private void thenExecutionThrowsException(String taskId, Class<? extends Exception> expectedException) {
        assertThrows(expectedException, () -> checkTaskStatus.execute(taskId));

        thenRepositoryWasCalledOnce(taskId);
    }

    private void thenRepositoryWasCalledOnce(String taskId) {
        verify(tripoRepository, times(1)).requestStatusGlbTripo(taskId);
    }
}