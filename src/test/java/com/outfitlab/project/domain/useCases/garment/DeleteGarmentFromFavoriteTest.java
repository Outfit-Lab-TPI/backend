package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.interfaces.repositories.UserGarmentFavoriteRepository;
import com.outfitlab.project.domain.model.UserGarmentFavoriteModel;
import com.outfitlab.project.infrastructure.repositories.UserGarmentFavoriteRepositoryImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DeleteGarmentFromFavoriteTest {

    private UserGarmentFavoriteRepository userGarmentFavoriteRepository = mock(UserGarmentFavoriteRepositoryImpl.class);
    private DeleteGarmentFromFavorite deleteGarmentFromFavorite = new DeleteGarmentFromFavorite(userGarmentFavoriteRepository);

    @Test
    void givenExistingFavoriteWhenExecuteThenDeleteSuccessfully() throws Exception, UserGarmentFavoriteNotFoundException {
        String garmentCode = "G001";
        String userEmail = "user@sasas.com";
        UserGarmentFavoriteModel fav = new UserGarmentFavoriteModel();

        when(userGarmentFavoriteRepository.findByGarmentCodeAndUserEmail(garmentCode, userEmail))
                .thenReturn(fav);

        doNothing().when(userGarmentFavoriteRepository).deleteFromFavorites(garmentCode, userEmail);

        String result = deleteGarmentFromFavorite.execute(garmentCode, userEmail);

        assertNotNull(result);
        assertEquals("Prenda eliminada de favoritos.", result);
    }

    @Test
    void givenNonexistentFavoriteWhenExecuteThenThrowUserGarmentFavoriteNotFoundException() throws Exception, UserGarmentFavoriteNotFoundException {
        String garmentCode = "G001";
        String userEmail = "user@asasa.com";

        when(userGarmentFavoriteRepository.findByGarmentCodeAndUserEmail(garmentCode, userEmail))
                .thenThrow(new UserGarmentFavoriteNotFoundException("No está en favoritos"));

        assertThrows(UserGarmentFavoriteNotFoundException.class, () ->
                deleteGarmentFromFavorite.execute(garmentCode, userEmail)
        );
    }

    @Test
    void givenUserNotFoundWhenExecuteThenThrowUserNotFoundException() throws Exception, UserGarmentFavoriteNotFoundException {
        String garmentCode = "G001";
        String userEmail = "user@asas.com";
        UserGarmentFavoriteModel fav = new UserGarmentFavoriteModel();

        when(userGarmentFavoriteRepository.findByGarmentCodeAndUserEmail(garmentCode, userEmail))
                .thenReturn(fav);
        doThrow(new UserNotFoundException("Usuario no encontrado"))
                .when(userGarmentFavoriteRepository).deleteFromFavorites(garmentCode, userEmail);

        assertThrows(UserNotFoundException.class, () ->
                deleteGarmentFromFavorite.execute(garmentCode, userEmail)
        );
    }

    @Test
    void givenGarmentNotFoundWhenExecuteThenThrowGarmentNotFoundException() throws Exception, UserGarmentFavoriteNotFoundException {
        String garmentCode = "G001";
        String userEmail = "user@asasa.com";
        UserGarmentFavoriteModel fav = new UserGarmentFavoriteModel();

        when(userGarmentFavoriteRepository.findByGarmentCodeAndUserEmail(garmentCode, userEmail))
                .thenReturn(fav);
        doThrow(new GarmentNotFoundException("Prenda no encontrada"))
                .when(userGarmentFavoriteRepository).deleteFromFavorites(garmentCode, userEmail);

        assertThrows(GarmentNotFoundException.class, () ->
                deleteGarmentFromFavorite.execute(garmentCode, userEmail)
        );
    }
}

