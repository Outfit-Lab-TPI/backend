package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.exceptions.FavoritesException;
import com.outfitlab.project.domain.exceptions.PageLessThanZeroException;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserGarmentFavoriteRepository;
import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.model.dto.PageDTO;
import com.outfitlab.project.infrastructure.repositories.UserGarmentFavoriteRepositoryImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetGarmentsFavoritesForUserByEmailTest {

    private final UserGarmentFavoriteRepository userGarmentFavoriteRepository = mock(UserGarmentFavoriteRepositoryImpl.class);
    private final GetGarmentsFavoritesForUserByEmail getGarmentsFavoritesForUserByEmail = new GetGarmentsFavoritesForUserByEmail(userGarmentFavoriteRepository);

    @Test
    public void givenValidUserAndPageWhenExecuteThenReturnFavoritesPage() throws Exception {
        String userEmail = "user@example.com";
        int page = 1;
        PageDTO<PrendaModel> expectedPage = new PageDTO<>();

        mockRepositoryReturnPage(userEmail, page, expectedPage);

        PageDTO<PrendaModel> result = getGarmentsFavoritesForUserByEmail.execute(userEmail, page);

        thenReturnFavoritePage(expectedPage, result, userEmail, page);
    }

    @Test
    public void givenNegativePageNumberWhenExecuteThenThrowPageLessThanZeroException() {
        String userEmail = "user@example.com";
        int page = -1;

        whenThrowsPageLessThanZeroException(userEmail, page);

        verify(userGarmentFavoriteRepository, never()).getGarmentsFavoritesForUserByEmail(anyString(), anyInt());
    }

    @Test
    public void givenNonexistentUserWhenExecuteThenThrowUserNotFoundException() throws Exception {
        String userEmail = "notfound@example.com";
        int page = 0;

        mockRepositoryThrow(userEmail, page, new UserNotFoundException("Usuario no encontrado"));

        whenThrowsUserNotFoundException(userEmail, page);

        verifyRepositoryCalledOnce(userEmail, page);
    }

    @Test
    public void givenRepositoryFailureWhenExecuteThenThrowFavoritesException() throws Exception {
        String userEmail = "user@example.com";
        int page = 2;

        mockRepositoryThrow(userEmail, page, new FavoritesException("Error al obtener favoritos"));
        whenThrowsFavoritesException(userEmail, page);

        verifyRepositoryCalledOnce(userEmail, page);
    }



// privadoss
    private void whenThrowsUserNotFoundException(String userEmail, int page) {
        assertThrows(UserNotFoundException.class,
                () -> getGarmentsFavoritesForUserByEmail.execute(userEmail, page));
    }

    private void whenThrowsFavoritesException(String userEmail, int page) {
        assertThrows(FavoritesException.class,
                () -> getGarmentsFavoritesForUserByEmail.execute(userEmail, page));
    }

    private void whenThrowsPageLessThanZeroException(String userEmail, int page) {
        assertThrows(PageLessThanZeroException.class, () -> getGarmentsFavoritesForUserByEmail.execute(userEmail, page));
    }

    private void mockRepositoryReturnPage(String email, int page, PageDTO<PrendaModel> response)
            throws Exception {
        when(userGarmentFavoriteRepository.getGarmentsFavoritesForUserByEmail(email, page))
                .thenReturn(response);
    }

    private void thenReturnFavoritePage(PageDTO<PrendaModel> expectedPage, PageDTO<PrendaModel> result, String userEmail, int page) {
        assertEquals(expectedPage, result);
        verifyRepositoryCalledOnce(userEmail, page);
    }

    private void mockRepositoryThrow(String email, int page, Exception exception)
            throws Exception {
        when(userGarmentFavoriteRepository.getGarmentsFavoritesForUserByEmail(email, page))
                .thenThrow(exception);
    }

    private void verifyRepositoryCalledOnce(String email, int page) {
        verify(userGarmentFavoriteRepository, times(1))
                .getGarmentsFavoritesForUserByEmail(email, page);
    }
}
