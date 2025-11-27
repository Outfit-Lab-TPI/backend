package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.interfaces.repositories.UserGarmentFavoriteRepository;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class DeleteAllFavoritesRelatedToGarmentTest {

    private UserGarmentFavoriteRepository repo = mock(UserGarmentFavoriteRepository.class);

    @Test
    void executeShouldCallRepositoryWithCorrectGarmentCode() {
        DeleteAllFavoritesRelatedToGarment useCase = new DeleteAllFavoritesRelatedToGarment(repo);

        useCase.execute("ABC123");

        verify(repo, times(1)).deleteFavoritesRelatedToGarment("ABC123");
    }
}
