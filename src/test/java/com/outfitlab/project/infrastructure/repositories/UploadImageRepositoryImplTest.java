package com.outfitlab.project.infrastructure.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class UploadImageRepositoryImplTest {

    @Mock
    private S3Client s3Client;

    private UploadImageRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new UploadImageRepositoryImpl(s3Client, "test-bucket", "sa-east-1");
    }

    @Test
    void shouldUploadFileSuccessfully() throws Exception {
        MockMultipartFile file = givenMultipartFile("test.txt", "Hello world".getBytes());

        String url = whenUploadFile(file, "folder");

        thenPutObjectShouldBeCalled();
        thenUrlShouldBeCorrect(url, "folder");
    }

    @Test
    void shouldThrowWhenUploadFails() throws Exception {
        MockMultipartFile file = givenMultipartFile("fail.txt", "data".getBytes());
        doThrow(new RuntimeException("AWS error"))
                .when(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));

        assertThatThrownBy(() -> whenUploadFile(file, "folder"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error al subir archivo a S3");
    }

    @Test
    void shouldDeleteFileSuccessfully() {
        String key = "folder/file.txt";
        String url = repository.getFileUrl(key);

        whenDeleteFile(url);

        thenDeleteObjectShouldBeCalled(key);
    }

    @Test
    void shouldThrowWhenDeleteFails() {
        doThrow(new RuntimeException("AWS delete error"))
                .when(s3Client).deleteObject(any(DeleteObjectRequest.class));

        assertThatThrownBy(() -> repository.deleteFile(repository.getFileUrl("fail.txt")))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error al eliminar archivo de S3");
    }

    @Test
    void shouldGetFileUrlCorrectly() {
        String key = "folder/file.txt";

        String url = repository.getFileUrl(key);

        assertThat(url)
                .isEqualTo("https://sa-east-1.s3.test-bucket.amazonaws.com/folder/file.txt");
    }

    private MockMultipartFile givenMultipartFile(String filename, byte[] content) {
        return new MockMultipartFile("file", filename, "text/plain", content);
    }

    private String whenUploadFile(MockMultipartFile file, String folder) {
        return repository.uploadFile(file, folder);
    }

    private void whenDeleteFile(String url) {
        repository.deleteFile(url);
    }

    private void thenPutObjectShouldBeCalled() {
        verify(s3Client, times(1))
                .putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    private void thenUrlShouldBeCorrect(String url, String folder) {
        assertThat(url).startsWith("https://sa-east-1.s3.test-bucket.amazonaws.com/" + folder + "/");
        assertThat(url).endsWith(".txt");
    }

    private void thenDeleteObjectShouldBeCalled(String key) {
        verify(s3Client).deleteObject(argThat((DeleteObjectRequest req) -> req.key().equals(key)));
    }
}