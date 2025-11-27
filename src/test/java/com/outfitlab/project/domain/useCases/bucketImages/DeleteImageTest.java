package com.outfitlab.project.domain.useCases.bucketImages;

import com.outfitlab.project.domain.interfaces.repositories.UploadImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class DeleteImageTest {

    private UploadImageRepository uploadImageRepository = mock(UploadImageRepository.class);
    private DeleteImage deleteImage;

    private final String IMAGE_URL = "https://mybucket.s3.aws.com/images/12345/photo.jpg";

    @BeforeEach
    void setUp() {
        deleteImage = new DeleteImage(uploadImageRepository);
    }


    @Test
    public void shouldCallRepositoryToDeleteFileWithCorrectUrl() {
        whenExecuteDeleteImage(IMAGE_URL);

        thenRepositoryDeleteFileWasCalled(IMAGE_URL);
    }


    //privadoss
    private void whenExecuteDeleteImage(String imageUrl) {
        deleteImage.execute(imageUrl);
    }

    private void thenRepositoryDeleteFileWasCalled(String expectedUrl) {
        verify(uploadImageRepository, times(1)).deleteFile(expectedUrl);
    }
}