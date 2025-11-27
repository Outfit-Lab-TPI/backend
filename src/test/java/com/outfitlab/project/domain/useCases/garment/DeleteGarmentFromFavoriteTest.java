package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.interfaces.repositories.UserGarmentFavoriteRepository;
import com.outfitlab.project.domain.model.UserGarmentFavoriteModel;
import com.outfitlab.project.infrastructure.repositories.UserGarmentFavoriteRepositoryImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DeleteGarmentFromFavoriteTest {

    private UserGarmentFavoriteRepository userGarmentFavoriteRepository =
            mock(UserGarmentFavoriteRepositoryImpl.class);

    private DeleteGarmentFromFavorite deleteGarmentFromFavorite =
            new DeleteGarmentFromFavorite(userGarmentFavoriteRepository);

    @Test
    public void givenExistingFavoriteWhenExecuteThenDeleteSuccessfully() throws Exception {

        String garmentCode = givenGarmentCode();
        String userEmail = givenUserEmail();
        UserGarmentFavoriteModel fav = givenFavoriteModel();

        givenFavoriteExists(garmentCode, userEmail, fav);
        givenDeleteSuccess(garmentCode, userEmail);

        String result = whenExecuteDeleteFavorite(garmentCode, userEmail);

        thenFavoriteDeletedSuccessfully(result);
    }

    @Test
    public void givenNonexistentFavoriteWhenExecuteThenThrowUserGarmentFavoriteNotFoundException(){
        String garmentCode = givenGarmentCode();
        String userEmail = givenUserEmail();

        givenFavoriteDoesNotExist(garmentCode, userEmail);

        thenThrowFavoriteNotFound(garmentCode, userEmail);
    }

    @Test
    public void givenUserNotFoundWhenExecuteThenThrowUserNotFoundException() {

        String garmentCode = givenGarmentCode();
        String userEmail = givenUserEmail();
        UserGarmentFavoriteModel fav = givenFavoriteModel();

        givenFavoriteExists(garmentCode, userEmail, fav);
        givenDeleteThrowsUserNotFound(garmentCode, userEmail);

        thenThrowUserNotFound(garmentCode, userEmail);
    }

    @Test
    public void givenGarmentNotFoundWhenExecuteThenThrowGarmentNotFoundException()
            throws Exception {

        String garmentCode = givenGarmentCode();
        String userEmail = givenUserEmail();
        UserGarmentFavoriteModel fav = givenFavoriteModel();

        givenFavoriteExists(garmentCode, userEmail, fav);
        givenDeleteThrowsGarmentNotFound(garmentCode, userEmail);

        thenThrowGarmentNotFound(garmentCode, userEmail);
    }


    //privadoss  ----------------------
    private void givenDeleteThrowsGarmentNotFound(String garment, String user)
            throws Exception {
        doThrow(new GarmentNotFoundException("Prenda no encontrada"))
                .when(userGarmentFavoriteRepository).deleteFromFavorites(garment, user);
    }

    private void thenThrowGarmentNotFound(String garment, String user) {
        assertThrows(
                GarmentNotFoundException.class,
                () -> deleteGarmentFromFavorite.execute(garment, user)
        );
    }

    private String givenGarmentCode() {
        return "G001";
    }

    private UserGarmentFavoriteModel givenFavoriteModel() {
        return new UserGarmentFavoriteModel();
    }
    private String givenUserEmail() {
        return "user@sasas.com";
    }


    private void givenFavoriteExists(String garment, String user, UserGarmentFavoriteModel fav)
            throws UserGarmentFavoriteNotFoundException {
        when(userGarmentFavoriteRepository.findByGarmentCodeAndUserEmail(garment, user))
                .thenReturn(fav);
    }

    private void givenDeleteSuccess(String garment, String user) throws Exception {
        doNothing().when(userGarmentFavoriteRepository)
                .deleteFromFavorites(garment, user);
    }

    private String whenExecuteDeleteFavorite(String garment, String user) throws Exception {
        return deleteGarmentFromFavorite.execute(garment, user);
    }

    private void thenFavoriteDeletedSuccessfully(String result) {
        assertNotNull(result);
        assertEquals("Prenda eliminada de favoritos.", result);
    }


    private void givenFavoriteDoesNotExist(String garment, String user)
            throws UserGarmentFavoriteNotFoundException {
        when(userGarmentFavoriteRepository.findByGarmentCodeAndUserEmail(garment, user))
                .thenThrow(new UserGarmentFavoriteNotFoundException("No está en favoritos"));
    }

    private void thenThrowFavoriteNotFound(String garment, String user) {
        assertThrows(
                UserGarmentFavoriteNotFoundException.class,
                () -> deleteGarmentFromFavorite.execute(garment, user)
        );
    }

    private void givenDeleteThrowsUserNotFound(String garment, String user){
        doThrow(new UserNotFoundException("Usuario no encontrado"))
                .when(userGarmentFavoriteRepository).deleteFromFavorites(garment, user);
    }

    private void thenThrowUserNotFound(String garment, String user) {
        assertThrows(
                UserNotFoundException.class,
                () -> deleteGarmentFromFavorite.execute(garment, user)
        );
    }

}
