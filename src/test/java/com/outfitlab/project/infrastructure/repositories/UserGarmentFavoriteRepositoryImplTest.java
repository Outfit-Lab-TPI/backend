package com.outfitlab.project.infrastructure.repositories;


import com.outfitlab.project.domain.exceptions.FavoritesException;
import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.exceptions.UserGarmentFavoriteNotFoundException;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.model.UserGarmentFavoriteModel;
import com.outfitlab.project.domain.model.dto.PageDTO;
import com.outfitlab.project.infrastructure.model.PrendaEntity;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.model.UserGarmentFavoriteEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.GarmentJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserGarmentFavoriteJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class UserGarmentFavoriteRepositoryImplTest {

    @Mock
    private UserGarmentFavoriteJpaRepository favoriteJpa;

    @Mock
    private UserJpaRepository userJpa;

    @Mock
    private GarmentJpaRepository garmentJpa;

    @InjectMocks
    private UserGarmentFavoriteRepositoryImpl repository;

    @Test
    void shouldReturnFavoriteWhenFavoriteExists() throws Exception {
        String email = "user@mail.com";
        String code = "P001";

        UserEntity user = givenUser(email);
        PrendaEntity garment = givenGarment(code);
        UserGarmentFavoriteEntity fav = givenFavorite(user, garment);

        givenFindFavoriteReturns(code, email, fav);

        UserGarmentFavoriteModel result =
                repository.findByGarmentCodeAndUserEmail(code, email);

        assertThat(result.getGarment().getGarmentCode()).isEqualTo(code);
    }

    @Test
    void shouldThrowExceptionWhenFavoriteNotFound() {
        String email = "user@mail.com";
        String code = "X999";

        givenFindFavoriteReturns(code, email, null);

        assertThatThrownBy(() ->
                whenFindByGarmentCodeAndUserEmail(code, email)
        )
                .isInstanceOf(UserGarmentFavoriteNotFoundException.class)
                .hasMessageContaining(code);
    }

    @Test
    void shouldAddFavoriteWhenUserAndGarmentExist() throws Exception {
        String email = "user@mail.com";
        String code = "P123";

        UserEntity user = givenUser(email);
        PrendaEntity garment = givenGarment(code);
        UserGarmentFavoriteEntity fav = givenFavorite(user, garment);

        givenUserExists(email, user);
        givenGarmentExists(code, garment);
        givenSaveReturns(fav);

        UserGarmentFavoriteEntity result =
                repository.addToFavorite(code, email);

        assertThat(result.getGarment().getGarmentCode()).isEqualTo(code);
    }

    @Test
    void shouldThrowUserNotFoundWhenAddingFavorite() {
        String email = "no@mail.com";
        String code = "P111";

        givenUserExists(email, null);

        assertThatThrownBy(() ->
                whenAddToFavorite(code, email)
        )
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(email);
    }

    @Test
    void shouldThrowGarmentNotFoundWhenAddingFavorite() {
        String email = "user@mail.com";
        String code = "P404";

        givenUserExists(email, givenUser(email));
        givenGarmentExists(code, null);

        assertThatThrownBy(() ->
                whenAddToFavorite(code, email)
        )
                .isInstanceOf(GarmentNotFoundException.class)
                .hasMessageContaining(code);
    }

    @Test
    void shouldDeleteFavoriteWhenExists() throws Exception {
        String email = "user@mail.com";
        String code = "P001";

        UserEntity user = givenUser(email);
        PrendaEntity garment = givenGarment(code);
        UserGarmentFavoriteEntity fav = givenFavorite(user, garment);

        givenUserExists(email, user);
        givenGarmentExists(code, garment);
        given(favoriteJpa.findByGarmentAndUser(garment, user)).willReturn(fav);

        whenDeleteFromFavorites(code, email);

        verify(favoriteJpa).delete(fav);
    }

    @Test
    void shouldThrowUserNotFoundWhenDeletingFavorite() {
        String email = "x@mail.com";
        String code = "P001";

        givenUserExists(email, null);

        assertThatThrownBy(() ->
                whenDeleteFromFavorites(code, email)
        )
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(email);
    }

    @Test
    void shouldThrowGarmentNotFoundWhenDeletingFavorite() {
        String email = "user@mail.com";
        String code = "PXYZ";

        givenUserExists(email, givenUser(email));
        givenGarmentExists(code, null);

        assertThatThrownBy(() ->
                whenDeleteFromFavorites(code, email)
        )
                .isInstanceOf(GarmentNotFoundException.class)
                .hasMessageContaining(code);
    }

    @Test
    void shouldReturnFavoritesPageWhenExists() throws Exception {
        String email = "user@mail.com";

        UserEntity user = givenUser(email);
        givenUserExists(email, user);

        PrendaEntity garment = givenGarment("P1");
        UserGarmentFavoriteEntity fav = givenFavorite(user, garment);

        Page<UserGarmentFavoriteEntity> page =
                new PageImpl<>(List.of(fav));

        givenFavoritesPage(email, page);

        PageDTO<PrendaModel> dto =
                repository.getGarmentsFavoritesForUserByEmail(email, 0);

        assertThat(dto.getContent()).hasSize(1);
    }

    @Test
    void shouldThrowFavoritesExceptionWhenNoFavorites() {
        String email = "user@mail.com";

        givenUserExists(email, givenUser(email));

        Page<UserGarmentFavoriteEntity> emptyPage =
                new PageImpl<>(List.of());

        givenFavoritesPage(email, emptyPage);

        assertThatThrownBy(() ->
                whenGetFavorites(email, 0)
        )
                .isInstanceOf(FavoritesException.class);
    }

    @Test
    void shouldThrowUserNotFoundWhenGettingFavorites() {
        String email = "x@mail.com";
        givenUserExists(email, null);

        assertThatThrownBy(() ->
                whenGetFavorites(email, 0)
        )
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void shouldDeleteFavoritesRelatedToGarment() {
        String code = "P001";

        whenDeleteFavoritesRelated(code);

        verify(favoriteJpa).deleteAllByGarmentCode(code);
    }

    private UserEntity givenUser(String email) {
        return new UserEntity("user@mail.com");
    }

    private PrendaEntity givenGarment(String code) {
        PrendaEntity p = new PrendaEntity();
        p.setGarmentCode(code);
        return p;
    }

    private UserGarmentFavoriteEntity givenFavorite(UserEntity u, PrendaEntity g) {
        return new UserGarmentFavoriteEntity(u, g);
    }

    private void givenUserExists(String email, UserEntity user) {
        given(userJpa.findByEmail(email)).willReturn(user);
    }

    private void givenGarmentExists(String code, PrendaEntity garment) {
        given(garmentJpa.findByGarmentCode(code)).willReturn(garment);
    }

    private void givenFindFavoriteReturns(String code, String email, UserGarmentFavoriteEntity fav) {
        given(favoriteJpa.findByGarment_GarmentCodeAndUser_Email(code, email)).willReturn(fav);
    }

    private void givenFavoritesPage(String email, Page<UserGarmentFavoriteEntity> page) {
        given(favoriteJpa.findFavoritesByUserEmail(eq(email), any())).willReturn(page);
    }

    private void givenSaveReturns(UserGarmentFavoriteEntity fav) {
        given(favoriteJpa.save(any())).willReturn(fav);
    }

    private void whenFindByGarmentCodeAndUserEmail(String code, String email)
            throws UserGarmentFavoriteNotFoundException {
        repository.findByGarmentCodeAndUserEmail(code, email);
    }

    private void whenAddToFavorite(String code, String email)
            throws Exception {
        repository.addToFavorite(code, email);
    }

    private void whenDeleteFromFavorites(String code, String email)
            throws Exception {
        repository.deleteFromFavorites(code, email);
    }

    private void whenGetFavorites(String email, int page)
            throws Exception {
        repository.getGarmentsFavoritesForUserByEmail(email, page);
    }

    private void whenDeleteFavoritesRelated(String code) {
        repository.deleteFavoritesRelatedToGarment(code);
    }
}