package com.outfitlab.project.domain.useCases.combinationFavorite;

import com.outfitlab.project.domain.exceptions.FavoritesException;
import com.outfitlab.project.domain.exceptions.PageLessThanZeroException;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserCombinationFavoriteRepository;
import com.outfitlab.project.domain.model.UserCombinationFavoriteModel;
import com.outfitlab.project.domain.model.dto.PageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetCombinationFavoritesForUserByEmailTest {

    private UserCombinationFavoriteRepository userCombinationFavoriteRepository = mock(UserCombinationFavoriteRepository.class);
    private GetCombinationFavoritesForUserByEmail getCombinationFavoritesForUserByEmail;

    private final String VALID_EMAIL = "user@test.com";
    private final int VALID_PAGE = 2;
    private final int INVALID_PAGE = -1;
    private final String NULL_EMAIL = null;
    private final String EMPTY_EMAIL = "";

    private PageDTO<UserCombinationFavoriteModel> mockPageDTO;

    @BeforeEach
    void setUp() throws FavoritesException, UserNotFoundException {
        mockPageDTO = createMockPageDTO(3);
        getCombinationFavoritesForUserByEmail = new GetCombinationFavoritesForUserByEmail(userCombinationFavoriteRepository);
    }


    @Test
    public void shouldReturnFavoritesPageWhenEmailAndPageAreValid() throws Exception {
        givenRepositoryReturnsFavoritesPage(VALID_EMAIL, VALID_PAGE, mockPageDTO);

        PageDTO<UserCombinationFavoriteModel> result = whenExecuteGetFavorites(VALID_EMAIL, VALID_PAGE);

        thenResultMatchesExpectedPage(result, mockPageDTO);
        thenRepositoryWasCalledOnce(VALID_EMAIL, VALID_PAGE);
    }

    @Test
    public void shouldThrowPageLessThanZeroExceptionWhenPageIsNegative() {
        assertThrows(PageLessThanZeroException.class,
                () -> whenExecuteGetFavorites(VALID_EMAIL, INVALID_PAGE),
                "Debe lanzar PageLessThanZeroException para página negativa.");

        thenRepositoryWasNeverCalled();
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenUserEmailIsNull() {
        givenRepositoryThrowsRuntimeException(NULL_EMAIL, VALID_PAGE);

        assertThrows(RuntimeException.class,
                () -> whenExecuteGetFavorites(NULL_EMAIL, VALID_PAGE),
                "Debe propagar RuntimeException si el email es null y el repositorio falla.");

        thenRepositoryWasCalledOnce(NULL_EMAIL, VALID_PAGE);
    }

    @Test
    public void shouldReturnFavoritesPageWhenUserEmailIsEmpty() throws Exception {
        givenRepositoryReturnsFavoritesPage(EMPTY_EMAIL, VALID_PAGE, mockPageDTO);

        PageDTO<UserCombinationFavoriteModel> result = whenExecuteGetFavorites(EMPTY_EMAIL, VALID_PAGE);

        thenResultMatchesExpectedPage(result, mockPageDTO);
        thenRepositoryWasCalledOnce(EMPTY_EMAIL, VALID_PAGE);
    }

    @Test
    public void shouldPropagateUserNotFoundException() throws Exception {
        givenRepositoryThrowsUserNotFound(VALID_EMAIL, VALID_PAGE);

        assertThrows(UserNotFoundException.class,
                () -> whenExecuteGetFavorites(VALID_EMAIL, VALID_PAGE),
                "Debe propagar UserNotFoundException.");

        thenRepositoryWasCalledOnce(VALID_EMAIL, VALID_PAGE);
    }

    @Test
    public void shouldPropagateFavoritesException() throws Exception {
        givenRepositoryThrowsFavoritesException(VALID_EMAIL, VALID_PAGE);

        assertThrows(FavoritesException.class,
                () -> whenExecuteGetFavorites(VALID_EMAIL, VALID_PAGE),
                "Debe propagar FavoritesException.");

        thenRepositoryWasCalledOnce(VALID_EMAIL, VALID_PAGE);
    }


    //privadosss
    private PageDTO<UserCombinationFavoriteModel> createMockPageDTO(int size) {
        PageDTO<UserCombinationFavoriteModel> pageDTO = new PageDTO<>();
        List<UserCombinationFavoriteModel> content = Collections.nCopies(size, mock(UserCombinationFavoriteModel.class));
        pageDTO.setContent(content);
        return pageDTO;
    }

    private void givenRepositoryReturnsFavoritesPage(String email, int page, PageDTO<UserCombinationFavoriteModel> response) throws UserNotFoundException, FavoritesException {
        when(userCombinationFavoriteRepository.getCombinationFavoritesForUserByEmail(email, page)).thenReturn(response);
    }

    private void givenRepositoryThrowsUserNotFound(String email, int page) throws UserNotFoundException, FavoritesException {
        doThrow(UserNotFoundException.class).when(userCombinationFavoriteRepository).getCombinationFavoritesForUserByEmail(email, page);
    }

    private void givenRepositoryThrowsFavoritesException(String email, int page) throws UserNotFoundException, FavoritesException {
        doThrow(FavoritesException.class).when(userCombinationFavoriteRepository).getCombinationFavoritesForUserByEmail(email, page);
    }

    private void givenRepositoryThrowsRuntimeException(String email, int page) {
        doThrow(RuntimeException.class).when(userCombinationFavoriteRepository).getCombinationFavoritesForUserByEmail(email, page);
    }

    private PageDTO<UserCombinationFavoriteModel> whenExecuteGetFavorites(String userEmail, int page) throws PageLessThanZeroException, UserNotFoundException, FavoritesException {
        return getCombinationFavoritesForUserByEmail.execute(userEmail, page);
    }

    private void thenResultMatchesExpectedPage(PageDTO<UserCombinationFavoriteModel> actual, PageDTO<UserCombinationFavoriteModel> expected) {
        assertNotNull(actual, "La página resultante no debe ser nula.");
        assertEquals(expected, actual, "El resultado debe coincidir con la página simulada.");
        assertEquals(expected.getContent().size(), actual.getContent().size(), "El tamaño del contenido debe coincidir.");
    }

    private void thenRepositoryWasCalledOnce(String userEmail, int page) {
        verify(userCombinationFavoriteRepository, times(1)).getCombinationFavoritesForUserByEmail(userEmail, page);
    }

    private void thenRepositoryWasNeverCalled() {
        verify(userCombinationFavoriteRepository, never()).getCombinationFavoritesForUserByEmail(anyString(), anyInt());
    }
}