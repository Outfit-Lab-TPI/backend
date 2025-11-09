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

    private UserGarmentFavoriteRepository userGarmentFavoriteRepository = mock(UserGarmentFavoriteRepositoryImpl.class);
    private GetGarmentsFavoritesForUserByEmail getGarmentsFavoritesForUserByEmail = new GetGarmentsFavoritesForUserByEmail(userGarmentFavoriteRepository);

    @Test
    void givenValidUserAndPageWhenExecuteThenReturnFavoritesPage() throws Exception, FavoritesException {
        String userEmail = "user@example.com";
        int page = 1;
        PageDTO<PrendaModel> expectedPage = new PageDTO<>();

        when(userGarmentFavoriteRepository.getGarmentsFavoritesForUserByEmail(userEmail, page)).thenReturn(expectedPage);

        PageDTO<PrendaModel> result = getGarmentsFavoritesForUserByEmail.execute(userEmail, page);

        assertNotNull(result);
        assertEquals(expectedPage, result);
        verify(userGarmentFavoriteRepository, times(1)).getGarmentsFavoritesForUserByEmail(userEmail, page);
    }

    @Test
    void givenNegativePageNumberWhenExecuteThenThrowPageLessThanZeroException() throws UserNotFoundException, FavoritesException {
        String userEmail = "user@example.com";
        int page = -1;

        assertThrows(PageLessThanZeroException.class, () -> getGarmentsFavoritesForUserByEmail.execute(userEmail, page));
        verify(userGarmentFavoriteRepository, never()).getGarmentsFavoritesForUserByEmail(anyString(), anyInt());
    }

    @Test
    void givenNonexistentUserWhenExecuteThenThrowUserNotFoundException() throws Exception, FavoritesException {
        String userEmail = "notfound@example.com";
        int page = 0;

        when(userGarmentFavoriteRepository.getGarmentsFavoritesForUserByEmail(userEmail, page)).thenThrow(new UserNotFoundException("Usuario no encontrado"));

        assertThrows(UserNotFoundException.class, () -> getGarmentsFavoritesForUserByEmail.execute(userEmail, page));
        verify(userGarmentFavoriteRepository, times(1)).getGarmentsFavoritesForUserByEmail(userEmail, page);
    }

    @Test
    void givenRepositoryFailureWhenExecuteThenThrowFavoritesException() throws Exception, FavoritesException {
        String userEmail = "user@example.com";
        int page = 2;

        when(userGarmentFavoriteRepository.getGarmentsFavoritesForUserByEmail(userEmail, page)).thenThrow(new FavoritesException("Error al obtener favoritos"));

        assertThrows(FavoritesException.class, () -> getGarmentsFavoritesForUserByEmail.execute(userEmail, page));
        verify(userGarmentFavoriteRepository, times(1)).getGarmentsFavoritesForUserByEmail(userEmail, page);
    }
}

