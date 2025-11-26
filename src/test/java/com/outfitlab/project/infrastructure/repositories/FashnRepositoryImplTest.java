package com.outfitlab.project.infrastructure.repositories;

import static org.junit.jupiter.api.Assertions.*;

import com.outfitlab.project.domain.exceptions.FashnApiException;
import com.outfitlab.project.domain.exceptions.PredictionFailedException;
import com.outfitlab.project.domain.exceptions.PredictionTimeoutException;
import com.outfitlab.project.presentation.dto.FashnResponse;
import com.outfitlab.project.presentation.dto.StatusResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
@ActiveProfiles("test")
class FashnRepositoryImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private FashnRepositoryImpl repository;

    @Test
    void shouldReturnIdWhenCombineOk() throws Exception {
        HttpEntity<Map<String, Object>> expectedEntity = givenHttpEntityForCombine();
        givenFashnRunResponse("ABC123");

        String result = whenCombine("garment.jpg", "tops", "MAN");

        thenShouldReturn(result, "ABC123");
    }

    @Test
    void shouldThrowExceptionWhenRunReturnsNonOK() throws Exception {
        HttpEntity<Map<String, Object>> expectedEntity = givenHttpEntityForCombine();
        givenRunReturnsStatus(HttpStatus.BAD_REQUEST);

        assertThrows(FashnApiException.class,
                () -> whenCombine("img.jpg", "tops", "MAN"));
    }

    @Test
    void shouldReturnImageUrlWhenPollingCompletes() throws Exception {
        givenStatusSequenceCompleted("https://final-image.com/result.jpg");

        String result = whenPollStatus("XYZ999");

        thenShouldReturn(result, "https://final-image.com/result.jpg");
    }

    @Test
    void shouldThrowTimeoutWhenPollingExceedsAttempts() {
        givenStatusSequenceNeverCompletes();

        assertThrows(PredictionTimeoutException.class,
                () -> whenPollStatus("XYZ999"));
    }

    @Test
    void shouldThrowPredictionFailedWhenStatusFailed() {
        givenStatusFailed();

        assertThrows(PredictionFailedException.class,
                () -> whenPollStatus("XYZ999"));
    }

    private HttpEntity<Map<String, Object>> givenHttpEntityForCombine() throws Exception {
        UserDetails mockUser = mock(UserDetails.class);
        return new HttpEntity<>(Map.of(), new HttpHeaders());
    }

    private void givenFashnRunResponse(String id) {
        FashnResponse resp = new FashnResponse();
        resp.setId(id);

        ResponseEntity<FashnResponse> entity =
                new ResponseEntity<>(resp, HttpStatus.OK);

        when(restTemplate.exchange(
                contains("/run"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(FashnResponse.class)
        )).thenReturn(entity);
    }

    private void givenRunReturnsStatus(HttpStatus status) {
        ResponseEntity<FashnResponse> entity =
                new ResponseEntity<>(null, status);

        when(restTemplate.exchange(
                contains("/run"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(FashnResponse.class)
        )).thenReturn(entity);
    }

    private void givenStatusSequenceCompleted(String finalUrl) {
        StatusResponse first = new StatusResponse("XYZ999", "processing", null, null);
        StatusResponse second = new StatusResponse("XYZ999", "completed", List.of(finalUrl), null);

        when(restTemplate.exchange(
                contains("/status"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(StatusResponse.class),
                anyString()
        )).thenReturn(
                new ResponseEntity<>(first, HttpStatus.OK),
                new ResponseEntity<>(second, HttpStatus.OK)
        );
    }

    private void givenStatusSequenceNeverCompletes() {
        StatusResponse ongoing = new StatusResponse("ABC123","processing", null, null);

        ResponseEntity<StatusResponse> res =
                new ResponseEntity<>(ongoing, HttpStatus.OK);

        when(restTemplate.exchange(
                contains("/status"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(StatusResponse.class),
                anyString()
        )).thenReturn(res);
    }

    private void givenStatusFailed() {
        StatusResponse failed = new StatusResponse("ABC123", "failed", null, null);

        ResponseEntity<StatusResponse> entity =
                new ResponseEntity<>(failed, HttpStatus.OK);

        when(restTemplate.exchange(
                contains("/status"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(StatusResponse.class),
                anyString()
        )).thenReturn(entity);
    }

    private String whenCombine(String garment, String category, String avatar) throws Exception {
        UserDetails mockUser = mock(UserDetails.class);
        return repository.combine(garment, category, avatar, mockUser);
    }

    private String whenPollStatus(String id) throws Exception {
        return repository.pollStatus(id);
    }

    private void thenShouldReturn(String actual, String expected) {
        assertThat(actual).isEqualTo(expected);
    }

}