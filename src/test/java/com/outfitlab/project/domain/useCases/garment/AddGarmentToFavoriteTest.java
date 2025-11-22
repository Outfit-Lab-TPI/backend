package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.interfaces.repositories.UserGarmentFavoriteRepository;
import com.outfitlab.project.infrastructure.model.UserGarmentFavoriteEntity;
import com.outfitlab.project.infrastructure.repositories.UserGarmentFavoriteRepositoryImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AddGarmentToFavoriteTest {

        private UserGarmentFavoriteRepository userGarmentFavoriteRepository = mock(
                        UserGarmentFavoriteRepositoryImpl.class);
        private AddGarmentToFavorite addGarmentToFavorite = new AddGarmentToFavorite(userGarmentFavoriteRepository);

        @Test
        public void givenValidGarmentAndUserWhenExecuteThenAddToFavoritesSuccessfully()
                        throws Exception, UserGarmentFavoriteNotFoundException, FavoritesException {
                String garmentCode = "G001";
                String userEmail = "test@aaaa.com";
                UserGarmentFavoriteEntity fav = new UserGarmentFavoriteEntity();

                when(userGarmentFavoriteRepository.findByGarmentCodeAndUserEmail(garmentCode, userEmail))
                                .thenThrow(new UserGarmentFavoriteNotFoundException("No está en favoritos"));
                when(userGarmentFavoriteRepository.addToFavorite(garmentCode, userEmail)).thenReturn(fav);

                String result = addGarmentToFavorite.execute(garmentCode, userEmail);

                assertNotNull(result);
                assertEquals("Prenda añadida a favoritos.", result);
        }

        @Test
        public void givenExistingFavoriteWhenExecuteThenThrowUserGarmentFavoriteAlreadyExistsException()
                        throws UserGarmentFavoriteNotFoundException {
                String garmentCode = "G001";
                String userEmail = "test@aaaa.com";

                when(userGarmentFavoriteRepository.findByGarmentCodeAndUserEmail(garmentCode, userEmail))
                                .thenReturn(null);

                assertThrows(UserGarmentFavoriteAlreadyExistsException.class,
                                () -> addGarmentToFavorite.execute(garmentCode, userEmail));
        }

        @Test
        public void givenGarmentOrUserNotFoundWhenExecuteThenThrowCorrespondingException()
                        throws UserNotFoundException, UserGarmentFavoriteNotFoundException, GarmentNotFoundException {
                String garmentCode = "G001";
                String userEmail = "test@aaaa.com";

                when(userGarmentFavoriteRepository.findByGarmentCodeAndUserEmail(garmentCode, userEmail))
                                .thenThrow(new UserGarmentFavoriteNotFoundException("No está en favoritos"));
                when(userGarmentFavoriteRepository.addToFavorite(garmentCode, userEmail))
                                .thenThrow(new UserNotFoundException("Usuario no encontrado"));

                assertThrows(UserNotFoundException.class, () -> addGarmentToFavorite.execute(garmentCode, userEmail));
        }

        @Test
        public void givenFavoriteAdditionFailsWhenExecuteThenThrowFavoritesException()
                        throws Exception, UserGarmentFavoriteNotFoundException {
                String garmentCode = "G001";
                String userEmail = "test@aaa.com";

                when(userGarmentFavoriteRepository.findByGarmentCodeAndUserEmail(garmentCode, userEmail))
                                .thenThrow(new UserGarmentFavoriteNotFoundException("No está en favoritos"));
                when(userGarmentFavoriteRepository.addToFavorite(garmentCode, userEmail)).thenReturn(null);

                assertThrows(FavoritesException.class, () -> addGarmentToFavorite.execute(garmentCode, userEmail));
        }
}
