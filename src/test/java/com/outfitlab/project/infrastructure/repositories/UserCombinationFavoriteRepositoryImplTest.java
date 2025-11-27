package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.exceptions.FavoritesException;
import com.outfitlab.project.domain.exceptions.UserCombinationFavoriteNotFoundException;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.model.UserCombinationFavoriteModel;
import com.outfitlab.project.domain.model.dto.PageDTO;
import com.outfitlab.project.infrastructure.model.UserCombinationFavoriteEntity;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserCombinationFavoriteJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class UserCombinationFavoriteRepositoryImplTest {
    @Mock
    private UserCombinationFavoriteJpaRepository favoriteJpaRepository;

    @Mock
    private UserJpaRepository userJpaRepository;

    @InjectMocks
    private UserCombinationFavoriteRepositoryImpl repository;

    private static final String EMAIL = "test@mail.com";
    private static final String URL = "https://combination.com/1";

    @Test
    void shouldReturnFavoriteWhenExists() throws Exception {
        UserCombinationFavoriteEntity entity = givenFavoriteEntity();
        givenFavoriteExists(URL, EMAIL, entity);

        UserCombinationFavoriteModel result = whenFindByCombinationAndEmail();

        thenFavoriteShouldMatch(result, URL);
    }

    @Test
    void shouldThrowExceptionWhenFavoriteDoesNotExist() {
        givenFavoriteDoesNotExist(URL, EMAIL);

        assertThatThrownBy(() -> whenFindByCombinationAndEmail())
                .isInstanceOf(UserCombinationFavoriteNotFoundException.class)
                .hasMessageContaining(URL);
    }

    @Test
    void shouldAddToFavoritesWhenUserExists() throws Exception {
        UserEntity user = givenUser();
        givenUserExists(user);
        UserCombinationFavoriteEntity saved = givenSavedFavorite(user);

        givenFavoriteSaved(saved);

        UserCombinationFavoriteEntity result = whenAddToFavorites();

        assertThat(result.getCombinationUrl()).isEqualTo(URL);
    }

    @Test
    void shouldThrowExceptionWhenAddingFavoriteForNonExistingUser() {
        givenUserDoesNotExist();

        assertThatThrownBy(() -> whenAddToFavorites())
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(EMAIL);
    }

    @Test
    void shouldDeleteFavoriteWhenUserExists() throws Exception {
        UserEntity user = givenUser();
        givenUserExists(user);

        UserCombinationFavoriteEntity favorite = givenFavoriteEntity();
        givenFavoriteExists(URL, EMAIL, favorite);

        whenDeleteFavorite();

        verify(favoriteJpaRepository).delete(favorite);
    }

    @Test
    void shouldThrowExceptionWhenDeletingFavoriteWithNonExistingUser() {
        givenUserDoesNotExist();

        assertThatThrownBy(this::whenDeleteFavorite)
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void shouldReturnPageDTOWhenUserHasFavorites() throws Exception {
        UserEntity user = givenUser();
        givenUserExists(user);

        Page<UserCombinationFavoriteEntity> page = givenFavoritePage(List.of(givenFavoriteEntity()));

        givenFavoritePageExists(page);

        PageDTO<UserCombinationFavoriteModel> result = whenGetFavoritesPage();

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void shouldThrowExceptionWhenUserHasNoFavorites() throws Exception {
        UserEntity user = givenUser();
        givenUserExists(user);

        givenFavoritePageExists(givenFavoritePage(List.of())); // empty page

        assertThatThrownBy(this::whenGetFavoritesPage)
                .isInstanceOf(FavoritesException.class);
    }

    private UserCombinationFavoriteEntity givenFavoriteEntity() {
        return new UserCombinationFavoriteEntity(new UserEntity(), URL);
    }

    private UserCombinationFavoriteEntity givenSavedFavorite(UserEntity user) {
        return new UserCombinationFavoriteEntity(user, URL);
    }

    private UserEntity givenUser() {
        UserEntity user = new UserEntity();
        user.setEmail(EMAIL);
        return user;
    }

    private void givenUserExists(UserEntity user) {
        when(userJpaRepository.findByEmail(EMAIL)).thenReturn(user);
    }

    private void givenUserDoesNotExist() {
        when(userJpaRepository.findByEmail(EMAIL)).thenReturn(null);
    }

    private void givenFavoriteExists(String url, String email, UserCombinationFavoriteEntity entity) {
        when(favoriteJpaRepository.findBycombinationUrlAndUser_Email(url, email))
                .thenReturn(entity);
    }

    private void givenFavoriteDoesNotExist(String url, String email) {
        when(favoriteJpaRepository.findBycombinationUrlAndUser_Email(url, email))
                .thenReturn(null);
    }

    private void givenFavoriteSaved(UserCombinationFavoriteEntity saved) {
        when(favoriteJpaRepository.save(any())).thenReturn(saved);
    }

    private Page<UserCombinationFavoriteEntity> givenFavoritePage(List<UserCombinationFavoriteEntity> list) {
        return new PageImpl<>(list, PageRequest.of(0, 10), list.size());
    }

    private void givenFavoritePageExists(Page<UserCombinationFavoriteEntity> page) {
        when(favoriteJpaRepository.findByUser_Email(eq(EMAIL), any(PageRequest.class)))
                .thenReturn(page);
    }

    private UserCombinationFavoriteModel whenFindByCombinationAndEmail() throws Exception {
        return repository.findByCombinationUrlAndUserEmail(URL, EMAIL);
    }

    private UserCombinationFavoriteEntity whenAddToFavorites() throws Exception {
        return repository.addToFavorite(URL, EMAIL);
    }

    private void whenDeleteFavorite() throws Exception {
        repository.deleteFromFavorites(URL, EMAIL);
    }

    private PageDTO<UserCombinationFavoriteModel> whenGetFavoritesPage() throws Exception {
        return repository.getCombinationFavoritesForUserByEmail(EMAIL, 0);
    }

    private void thenFavoriteShouldMatch(UserCombinationFavoriteModel model, String url) {
        assertThat(model.getCombinationUrl()).isEqualTo(url);
    }
}
