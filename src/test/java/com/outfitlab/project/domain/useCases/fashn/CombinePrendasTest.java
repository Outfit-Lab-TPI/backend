package com.outfitlab.project.domain.useCases.fashn;

import com.outfitlab.project.domain.exceptions.FashnApiException;
import com.outfitlab.project.domain.interfaces.repositories.FashnRepository;
import com.outfitlab.project.domain.model.dto.CombineRequestDTO;
import com.outfitlab.project.domain.useCases.subscription.CheckUserPlanLimit;
import com.outfitlab.project.domain.useCases.subscription.IncrementUsageCounter;
import com.outfitlab.project.infrastructure.repositories.FashnRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CombinePrendasTest {

    @Mock
    private FashnRepository fashnRepository;

    @Mock
    private CheckUserPlanLimit checkUserPlanLimit;

    @Mock
    private IncrementUsageCounter incrementUsageCounter;

    @InjectMocks
    private CombinePrendas combinePrendas;

    private static final String TOP_URL = "top-url";
    private static final String BOTTOM_URL = "bottom-url";
    private static final String TASK_ID_TOP = "taskId123";
    private static final String RESULT_TOP = "result-top-ok";
    private static final String TASK_ID_BOTTOM = "taskId456";
    private static final String RESULT_BOTTOM = "result-bottom-ok";
    private static final String COMBINED_RESULT = "combined-result";
    private static final String GENDER_MALE = "male";
    private static final String GENDER_FEMALE = "female";
    private UserDetails mockUser = mock(UserDetails.class);

    @Test
    public void shouldThrowFashnApiExceptionWhenBothTopAndBottomAreNull() {
        CombineRequestDTO request = createRequest(null, null, GENDER_MALE);

        assertThrows(FashnApiException.class, () -> combinePrendas.execute(request, mockUser));
    }

    @Test
    public void shouldReturnResultWhenOnlyTopIsProvided() throws Exception {
        CombineRequestDTO request = createRequest(TOP_URL, null, GENDER_FEMALE);
        givenCombineCallReturnsTaskAndPollReturnsResult(TOP_URL, "tops", GENDER_FEMALE, TASK_ID_TOP, RESULT_TOP);

        String result = whenExecuteCombine(request);

        thenResultIsSuccessfulAndCombineWasCalled(result, RESULT_TOP, TOP_URL, "tops", GENDER_FEMALE);
        thenPollStatusWasCalled(TASK_ID_TOP);
        thenCombineTopAndBottomWasNeverCalled();
    }

    @Test
    public void shouldReturnResultWhenOnlyBottomIsProvided() throws Exception {
        CombineRequestDTO request = createRequest(null, BOTTOM_URL, GENDER_FEMALE);
        givenCombineCallReturnsTaskAndPollReturnsResult(BOTTOM_URL, "bottoms", GENDER_FEMALE, TASK_ID_BOTTOM,
                RESULT_BOTTOM);
        String result = whenExecuteCombine(request);

        thenResultIsSuccessfulAndCombineWasCalled(result, RESULT_BOTTOM, BOTTOM_URL, "bottoms", GENDER_FEMALE);
        thenPollStatusWasCalled(TASK_ID_BOTTOM);
        thenCombineTopAndBottomWasNeverCalled();
    }

    @Test
    public void shouldReturnResultWhenBothTopAndBottomAreProvided() throws Exception {
        CombineRequestDTO request = createRequest(TOP_URL, BOTTOM_URL, GENDER_MALE);
        givenCombineTopAndBottomCallReturnsResult(TOP_URL, BOTTOM_URL, GENDER_MALE, COMBINED_RESULT);

        String result = whenExecuteCombine(request);

        thenResultIsSuccessful(result, COMBINED_RESULT);
        thenCombineTopAndBottomWasCalled(TOP_URL, BOTTOM_URL, GENDER_MALE);
        thenCombineWasNeverCalled();
        thenPollStatusWasNeverCalled();
    }

    private CombineRequestDTO createRequest(String topUrl, String bottomUrl, String gender) {
        return new CombineRequestDTO(topUrl, bottomUrl, false, gender);
    }

    // private methods -----------------------------------

    private void givenCombineCallReturnsTaskAndPollReturnsResult(String itemUrl, String category, String gender,
            String taskId, String result) throws FashnApiException {
        when(fashnRepository.combine(itemUrl, category, gender, mockUser)).thenReturn(taskId);

        when(fashnRepository.pollStatus(taskId)).thenReturn(result);
    }

    private void givenCombineTopAndBottomCallReturnsResult(String topUrl, String bottomUrl, String gender,
            String result) throws FashnApiException {
        when(fashnRepository.combineTopAndBottom(topUrl, bottomUrl, gender, mockUser)).thenReturn(result);
    }

    private String whenExecuteCombine(CombineRequestDTO request)
            throws FashnApiException, com.outfitlab.project.domain.exceptions.PlanLimitExceededException,
            com.outfitlab.project.domain.exceptions.SubscriptionNotFoundException {
        return combinePrendas.execute(request, mockUser);
    }

    private void thenResultIsSuccessful(String actual, String expected) {
        assertNotNull(actual, "El resultado no debe ser nulo.");
        assertEquals(expected, actual, "El resultado debe coincidir con el resultado simulado.");
    }

    private void thenResultIsSuccessfulAndCombineWasCalled(String actual, String expected, String itemUrl,
            String category, String gender) {
        thenResultIsSuccessful(actual, expected);
        thenCombineWasCalled(itemUrl, category, gender, 1);
    }

    private void thenCombineWasCalled(String itemUrl, String category, String gender, int times) {
        verify(fashnRepository, times(times)).combine(itemUrl, category, gender, mockUser);
    }

    private void thenPollStatusWasCalled(String taskId) throws FashnApiException {
        verify(fashnRepository, times(1)).pollStatus(taskId);
    }

    private void thenCombineTopAndBottomWasCalled(String topUrl, String bottomUrl, String gender)
            throws FashnApiException {
        verify(fashnRepository, times(1)).combineTopAndBottom(topUrl, bottomUrl, gender, mockUser);
    }

    private void thenFashnRepositoryWasNeverCalled() {
        verify(fashnRepository, never()).combine(anyString(), anyString(), anyString(), any());
        verify(fashnRepository, never()).pollStatus(anyString());
        verify(fashnRepository, never()).combineTopAndBottom(anyString(), anyString(), anyString(), any());
    }

    private void thenCombineWasNeverCalled() {
        verify(fashnRepository, never()).combine(anyString(), anyString(), anyString(), any());
    }

    private void thenPollStatusWasNeverCalled() throws FashnApiException {
        verify(fashnRepository, never()).pollStatus(anyString());
    }

    private void thenCombineTopAndBottomWasNeverCalled() throws FashnApiException {
        verify(fashnRepository, never()).combineTopAndBottom(anyString(), anyString(), anyString(), any());
    }
}