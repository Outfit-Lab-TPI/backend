package com.outfitlab.project.s3;

class S3ServiceTest {
/*
    @Mock
    private S3Client s3ClientMock;

    @InjectMocks
    private AwsRepositoryImpl s3Service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(s3Service, "region", "sa-east-1");
        ReflectionTestUtils.setField(s3Service, "bucketName", "test-bucket");
    }

    @Test
    void givenValidFileWhenUploadThenReturnUrl() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "data".getBytes(StandardCharsets.UTF_8)
        );

        // Mock del putObject
        when(s3ClientMock.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(PutObjectResponse.builder().build());

        String url = s3Service.uploadFile(file, "model_images");

        assertTrue(url.contains("https://test-bucket.s3.sa-east-1.amazonaws.com/model_images/"));

        // Verificamos que el método se llamó exactamente 1 vez
        verify(s3ClientMock, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    void givenKeyWhenDeleteThenS3DeleteCalled() {
        when(s3ClientMock.deleteObject(any(DeleteObjectRequest.class)))
                .thenReturn(DeleteObjectResponse.builder().build());

        s3Service.deleteFile("model_images/file.jpg");

        verify(s3ClientMock, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }

    @Test
    void givenKeyWhenGetUrlThenReturnExpectedUrl() {
        String url = s3Service.getFileUrl("model_images/file.jpg");
        assertEquals("https://test-bucket.s3.sa-east-1.amazonaws.com/model_images/file.jpg", url);
    }*/
}