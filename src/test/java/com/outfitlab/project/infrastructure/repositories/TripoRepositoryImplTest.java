package com.outfitlab.project.infrastructure.repositories;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.outfitlab.project.domain.model.TripoModel;
import com.outfitlab.project.infrastructure.model.TripoEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.TripoJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class TripoRepositoryImplTest {

    @Mock
    private ObjectMapper mapper;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private TripoJpaRepository tripoJpaRepository;

    @Spy
    @InjectMocks
    private TripoRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(repository, "tripoApiKey", "test-api-key");
    }

    private MultipartFile mockMultipartFile(String name) {
        return new MockMultipartFile(name, name, "image/png", "dummy".getBytes());
    }

    private ResponseEntity<String> mockResponse(String body) {
        return ResponseEntity.ok(body);
    }

    @Test
    void shouldReturnTripoModelWhenFindByTaskId() {
        TripoEntity entity = givenTripoEntity("task123");
        TripoModel result = whenBuscarPorTaskId("task123");

        thenTripoModelShouldHaveTaskId(result, "task123");
    }

    @Test
    void shouldReturnTaskIdWhenRequestGenerateGlbToTripo() throws Exception {
        Map<String, Object> uploadData = new HashMap<>();
        uploadData.put("imageToken", "token123");
        uploadData.put("fileExtension", "png");

        ResponseEntity<String> response = givenTaskResponse("{\"data\":{\"task_id\":\"task123\"}}");

        String taskId = whenRequestGenerateGlbToTripo(uploadData, response);

        thenTaskIdShouldBe(taskId, "task123");
    }

    @Test
    void shouldSaveTripoModel() {
        TripoModel model = givenTripoModel("task123");
        TripoEntity entity = TripoEntity.convertToEntity(model);
        doReturn(entity).when(tripoJpaRepository).save(any(TripoEntity.class));

        TripoModel result = whenSaveTripoModel(model);

        thenTripoModelShouldHaveTaskId(result, "task123");
    }

    @Test
    void shouldUpdateTripoModel() throws Exception {
        TripoModel model = givenTripoModel("task123");
        TripoEntity entity = TripoEntity.convertToEntity(model);
        doReturn(entity).when(tripoJpaRepository).findByTaskId("task123");
        doReturn(entity).when(tripoJpaRepository).save(any(TripoEntity.class));

        TripoModel result = whenUpdateTripoModel(model);

        thenTripoModelShouldHaveTaskId(result, "task123");
    }

    @Test
    void shouldReturnGlbUrlWhenStatusIsSuccess() throws Exception {
        String taskId = "task123";
        String jsonResponse = "{\"data\":{\"status\":\"success\",\"result\":{\"pbr_model\":{\"url\":\"https://glb.com/model.glb\"}}}}";
        ResponseEntity<String> response = new ResponseEntity<>(jsonResponse, HttpStatus.OK);

        doReturn(response).when(repository).requestTripoTaskStatus(eq(taskId), any(HttpEntity.class));

        String result = whenRequestStatusGlbTripo(taskId);

        thenGlbUrlShouldBe(result, "https://glb.com/model.glb");
    }

    private TripoModel whenUpdateTripoModel(TripoModel model) throws Exception {
        return repository.update(model);
    }

    private TripoEntity givenTripoEntity(String taskId) {
        TripoEntity entity = new TripoEntity();
        entity.setTaskId(taskId);
        doReturn(entity).when(tripoJpaRepository).findByTaskId(taskId);
        return entity;
    }

    private TripoModel whenBuscarPorTaskId(String taskId) {
        return repository.buscarPorTaskId(taskId);
    }

    private TripoModel givenTripoModel(String taskId) {
        TripoModel model = new TripoModel();
        model.setTaskId(taskId);
        return model;
    }

    private TripoModel whenSaveTripoModel(TripoModel model) {
        return repository.save(model);
    }

    private void thenTripoModelShouldHaveTaskId(TripoModel model, String expectedTaskId) {
        assertThat(model.getTaskId()).isEqualTo(expectedTaskId);
    }

    private ResponseEntity<String> givenUploadResponse(String body) {
        doReturn(new ResponseEntity<>(body, HttpStatus.OK))
                .when(repository)
                .generateRequestToUploadImageToTripo(any(MultipartFile.class), anyString());
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    private Map<String, Object> whenRequestUploadImagenToTripo(String url, ResponseEntity<String> response) throws Exception {
        return repository.requestUploadImagenToTripo(url);
    }

    private void thenUploadDataShouldContain(Map<String, Object> data, String token, String filename, String extension) {
        assertThat(data.get("imageToken")).isEqualTo(token);
        assertThat(data.get("originalFilename")).isEqualTo(filename);
        assertThat(data.get("fileExtension")).isEqualTo(extension);
    }

    private ResponseEntity<String> givenTaskResponse(String body) {
        ResponseEntity<String> response = new ResponseEntity<>(body, HttpStatus.OK);
        doReturn(response).when(restTemplate).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));
        return response;
    }

    private String whenRequestGenerateGlbToTripo(Map<String, Object> uploadData, ResponseEntity<String> response) throws Exception {
        return repository.requestGenerateGlbToTripo(uploadData);
    }

    private void thenTaskIdShouldBe(String actual, String expected) {
        assertThat(actual).isEqualTo(expected);
    }

    private String whenRequestStatusGlbTripo(String taskId) throws Exception {
        return repository.requestStatusGlbTripo(taskId);
    }

    private void thenGlbUrlShouldBe(String actual, String expected) {
        assertThat(actual).isEqualTo(expected);
    }
}