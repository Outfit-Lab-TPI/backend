package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.interfaces.repositories.UserGarmentFavoriteRepository;
import com.outfitlab.project.infrastructure.model.UserGarmentFavoriteEntity;
import com.outfitlab.project.infrastructure.repositories.UserGarmentFavoriteRepositoryImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AddGarmentToFavoriteTest {

        private UserGarmentFavoriteRepository userGarmentFavoriteRepository = mock(UserGarmentFavoriteRepositoryImpl.class);

        private AddGarmentToFavorite addGarmentToFavorite = new AddGarmentToFavorite(userGarmentFavoriteRepository);

        @Test
        public void givenValidGarmentAndUserWhenExecuteThenAddToFavoritesSuccessfully(){

                String garmentCode = givenGarmentCode();
                String userEmail = givenUserEmail();
                UserGarmentFavoriteEntity fav = givenFavoriteEntity();

                givenFavoriteNotExisting(garmentCode, userEmail);
                givenAddFavoriteReturns(garmentCode, userEmail, fav);

                String result = whenExecuteAddFavorite(garmentCode, userEmail);

                thenFavoriteAddedSuccessfully(result);
        }


        @Test
        public void givenExistingFavoriteWhenExecuteThenThrowUserGarmentFavoriteAlreadyExistsException() {

                String garmentCode = givenGarmentCode();
                String userEmail = givenUserEmail();
                givenFavoriteAlreadyExists(garmentCode, userEmail);

                thenThrowAlreadyExists(garmentCode, userEmail);
        }

        @Test
        public void givenGarmentOrUserNotFoundWhenExecuteThenThrowCorrespondingException(){

                String garmentCode = givenGarmentCode();
                String userEmail = givenUserEmail();

                givenFavoriteNotExisting(garmentCode, userEmail);
                givenAddFavoriteThrowsUserNotFound(garmentCode, userEmail);

                thenThrowUserNotFound(garmentCode, userEmail);
        }


        @Test
        public void givenFavoriteAdditionFailsWhenExecuteThenThrowFavoritesException() {

                String garmentCode = givenGarmentCode();
                String userEmail = givenUserEmail();

                givenFavoriteNotExisting(garmentCode, userEmail);
                givenAddFavoriteReturnsNull(garmentCode, userEmail);

                thenThrowFavoritesException(garmentCode, userEmail);
        }


// privados ---------------
        private UserGarmentFavoriteEntity givenFavoriteEntity() {
                return new UserGarmentFavoriteEntity();
        }

        private void givenFavoriteNotExisting(String garment, String user)
                throws UserGarmentFavoriteNotFoundException {
                when(userGarmentFavoriteRepository.findByGarmentCodeAndUserEmail(garment, user))
                        .thenThrow(new UserGarmentFavoriteNotFoundException("No está en favoritos"));
        }

        private void givenAddFavoriteReturns(String garment, String user, UserGarmentFavoriteEntity entity)
                throws FavoritesException {
                when(userGarmentFavoriteRepository.addToFavorite(garment, user))
                        .thenReturn(entity);
        }

        private String whenExecuteAddFavorite(String garment, String user) throws FavoritesException {
                return addGarmentToFavorite.execute(garment, user);
        }

        private void thenFavoriteAddedSuccessfully(String result) {
                assertNotNull(result);
                assertEquals("Prenda añadida a favoritos.", result);
        }

        private void givenFavoriteAlreadyExists(String garment, String user)
                throws UserGarmentFavoriteNotFoundException {
                when(userGarmentFavoriteRepository.findByGarmentCodeAndUserEmail(garment, user))
                        .thenReturn(null);
        }

        private void thenThrowAlreadyExists(String garment, String user) {
                assertThrows(
                        UserGarmentFavoriteAlreadyExistsException.class,
                        () -> addGarmentToFavorite.execute(garment, user)
                );
        }

        private void givenAddFavoriteThrowsUserNotFound(String garment, String user)
                throws UserNotFoundException, FavoritesException {
                when(userGarmentFavoriteRepository.addToFavorite(garment, user))
                        .thenThrow(new UserNotFoundException("Usuario no encontrado"));
        }

        private void thenThrowUserNotFound(String garment, String user) {
                assertThrows(
                        UserNotFoundException.class,
                        () -> addGarmentToFavorite.execute(garment, user)
                );
        }

        private void givenAddFavoriteReturnsNull(String garment, String user)
                throws FavoritesException {
                when(userGarmentFavoriteRepository.addToFavorite(garment, user))
                        .thenReturn(null);
        }

        private void thenThrowFavoritesException(String garment, String user) {
                assertThrows(
                        FavoritesException.class,
                        () -> addGarmentToFavorite.execute(garment, user)
                );
        }

        private String givenGarmentCode() {
                return "G001";
        }

        private String givenUserEmail() {
                return "test@aaaa.com";
        }
}
