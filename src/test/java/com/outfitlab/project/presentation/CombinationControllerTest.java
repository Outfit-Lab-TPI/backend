package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.model.CombinationModel;
import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.model.UserCombinationFavoriteModel;
import com.outfitlab.project.domain.model.dto.PageDTO;
import com.outfitlab.project.domain.useCases.combination.CreateCombination;
import com.outfitlab.project.domain.useCases.combination.GetCombinationByPrendas;
import com.outfitlab.project.domain.useCases.combinationAttempt.RegisterCombinationAttempt;
import com.outfitlab.project.domain.useCases.combinationFavorite.AddCombinationToFavourite;
import com.outfitlab.project.domain.useCases.combinationFavorite.DeleteCombinationFromFavorite;
import com.outfitlab.project.domain.useCases.combinationFavorite.GetCombinationFavoritesForUserByEmail;
import com.outfitlab.project.domain.useCases.garment.GetGarmentByCode;
import com.outfitlab.project.presentation.dto.RegisterAttemptRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CombinationControllerTest {

    private AddCombinationToFavourite addCombinationToFavourite;
    private DeleteCombinationFromFavorite deleteCombinationFromFavorite;
    private GetCombinationFavoritesForUserByEmail getCombinationFavoritesForUserByEmail;
    private GetGarmentByCode getGarmentByCode;
    private GetCombinationByPrendas getCombinationByPrendas;
    private CreateCombination createCombination;
    private RegisterCombinationAttempt registerCombinationAttempt;

    private CombinationController controller;

    @BeforeEach
    void setUp() {
        addCombinationToFavourite = mock(AddCombinationToFavourite.class);
        deleteCombinationFromFavorite = mock(DeleteCombinationFromFavorite.class);
        getCombinationFavoritesForUserByEmail = mock(GetCombinationFavoritesForUserByEmail.class);
        getGarmentByCode = mock(GetGarmentByCode.class);
        getCombinationByPrendas = mock(GetCombinationByPrendas.class);
        createCombination = mock(CreateCombination.class);
        registerCombinationAttempt = mock(RegisterCombinationAttempt.class);

        controller = new CombinationController(
                addCombinationToFavourite,
                deleteCombinationFromFavorite,
                getCombinationFavoritesForUserByEmail,
                getGarmentByCode,
                getCombinationByPrendas,
                createCombination,
                registerCombinationAttempt);
    }

    // ========== registerAttempt Tests ==========

    @Test
    void givenValidRequestWithExistingCombinationWhenRegisterAttemptThenReturnOk() throws Exception {
        RegisterAttemptRequest request = givenValidRegisterAttemptRequest();
        PrendaModel sup = givenGarmentExists("sup-code", 1L);
        PrendaModel inf = givenGarmentExists("inf-code", 2L);
        CombinationModel combination = givenCombinationExists(1L, 2L);
        givenAttemptRegistered(123L);

        ResponseEntity<?> response = whenCallRegisterAttempt(request);

        thenResponseOkWithMessage(response, "Combinacion registrada con ID: 123");
        thenVerifyGarmentByCodeCalled("sup-code");
        thenVerifyGarmentByCodeCalled("inf-code");
        thenVerifyGetCombinationByPrendasCalled(1L, 2L);
        thenVerifyRegisterAttemptCalled();
    }

    @Test
    void givenValidRequestWithNewCombinationWhenRegisterAttemptThenReturnOk() throws Exception {
        RegisterAttemptRequest request = givenValidRegisterAttemptRequest();
        PrendaModel sup = givenGarmentExists("sup-code", 1L);
        PrendaModel inf = givenGarmentExists("inf-code", 2L);
        givenCombinationNotFound(1L, 2L);
        CombinationModel newCombination = givenCombinationCreated(sup, inf);
        givenAttemptRegistered(456L);

        ResponseEntity<?> response = whenCallRegisterAttempt(request);

        thenResponseOkWithMessage(response, "Combinacion registrada con ID: 456");
        thenVerifyCreateCombinationCalled(sup, inf);
    }

    @Test
    void givenInvalidGarmentCodeWhenRegisterAttemptThenReturn404() throws Exception {
        RegisterAttemptRequest request = givenValidRegisterAttemptRequest();
        givenGarmentNotFound("sup-code");

        ResponseEntity<?> response = whenCallRegisterAttempt(request);

        thenResponseNotFound(response, "Prenda no encontrada");
    }

    // ========== addCombinationToFavorite Tests ==========

    @Test
    void givenValidUrlWhenAddCombinationToFavoriteThenReturnOk() throws Exception {
        String url = givenValidCombinationUrl();
        givenAuthenticatedUser("user@test.com");
        givenFavoriteAdded("Favorito agregado");

        ResponseEntity<?> response = whenCallAddCombinationToFavorite(url);

        thenResponseOk(response);
        thenVerifyAddCombinationToFavouriteCalled(url, "user@test.com");
    }

    @Test
    void givenEmptyUrlWhenAddCombinationToFavoriteThenReturn404() {
        String url = "";

        ResponseEntity<?> response = whenCallAddCombinationToFavorite(url);

        thenResponseNotFound(response, "El parámetro:  no puede estar vacío.");
    }

    @Test
    void givenNullUrlWhenAddCombinationToFavoriteThenReturn404() {
        ResponseEntity<?> response = whenCallAddCombinationToFavorite(null);

        thenResponseNotFound(response, "El parámetro: null no puede estar vacío.");
    }

    @Test
    void givenPlanLimitExceededWhenAddCombinationToFavoriteThenReturn403() throws Exception {
        String url = givenValidCombinationUrl();
        givenAuthenticatedUser("user@test.com");
        givenPlanLimitExceeded();

        ResponseEntity<?> response = whenCallAddCombinationToFavorite(url);

        thenResponseForbidden(response);
        thenResponseContainsUpgradeRequired(response);
    }

    @Test
    void givenUserNotFoundWhenAddCombinationToFavoriteThenReturn404() throws Exception {
        String url = givenValidCombinationUrl();
        givenAuthenticatedUser("user@test.com");
        givenUserNotFound();

        ResponseEntity<?> response = whenCallAddCombinationToFavorite(url);

        thenResponseNotFound(response, "Usuario no encontrado");
    }

    @Test
    void givenFavoriteAlreadyExistsWhenAddCombinationToFavoriteThenReturn404() throws Exception {
        String url = givenValidCombinationUrl();
        givenAuthenticatedUser("user@test.com");
        givenFavoriteAlreadyExists();

        ResponseEntity<?> response = whenCallAddCombinationToFavorite(url);

        thenResponseNotFound(response, "Favorito ya existe");
    }

    // ========== deleteCombinationFromFavorite Tests ==========

    @Test
    void givenValidUrlWhenDeleteCombinationFromFavoriteThenReturnOk() throws Exception {
        String url = givenValidCombinationUrl();
        givenAuthenticatedUser("user@test.com");
        givenFavoriteDeleted("Favorito eliminado");

        ResponseEntity<?> response = whenCallDeleteCombinationFromFavorite(url);

        thenResponseOk(response);
        thenVerifyDeleteCombinationFromFavoriteCalled(url, "user@test.com");
    }

    @Test
    void givenFavoriteNotFoundWhenDeleteCombinationFromFavoriteThenReturn404() throws Exception {
        String url = givenValidCombinationUrl();
        givenAuthenticatedUser("user@test.com");
        givenFavoriteNotFoundForDelete();

        ResponseEntity<?> response = whenCallDeleteCombinationFromFavorite(url);

        thenResponseNotFound(response, "Favorito no encontrado");
    }

    // ========== getFavorites Tests ==========

    @Test
    void givenValidPageWhenGetFavoritesThenReturnOk() throws Exception {
        givenAuthenticatedUser("user@test.com");
        givenFavoritesExist();

        ResponseEntity<?> response = whenCallGetFavorites(0);

        thenResponseOk(response);
        thenVerifyGetFavoritesCalled("user@test.com", 0);
    }

    @Test
    void givenNegativePageWhenGetFavoritesThenReturn404() throws Exception {
        givenAuthenticatedUser("user@test.com");
        givenNegativePageError();

        ResponseEntity<?> response = whenCallGetFavorites(-1);

        thenResponseNotFound(response, "Página menor que cero");
    }

    @Test
    void givenNoFavoritesWhenGetFavoritesThenReturnOk() throws Exception {
        givenAuthenticatedUser("user@test.com");
        givenNoFavorites();

        ResponseEntity<?> response = whenCallGetFavorites(0);

        thenResponseOk(response);
    }

    // ========== GIVEN Methods ==========

    private RegisterAttemptRequest givenValidRegisterAttemptRequest() {
        return new RegisterAttemptRequest(
                "user@test.com",
                "sup-code",
                "inf-code",
                "http://image.url/combo.jpg");
    }

    private PrendaModel givenGarmentExists(String code, Long id) {
        PrendaModel garment = mock(PrendaModel.class);
        when(garment.getId()).thenReturn(id);
        when(getGarmentByCode.execute(code)).thenReturn(garment);
        return garment;
    }

    private void givenGarmentNotFound(String code) {
        when(getGarmentByCode.execute(code))
                .thenThrow(new GarmentNotFoundException("Prenda no encontrada"));
    }

    private CombinationModel givenCombinationExists(Long supId, Long infId) {
        CombinationModel combination = mock(CombinationModel.class);
        when(getCombinationByPrendas.execute(supId, infId)).thenReturn(combination);
        return combination;
    }

    private void givenCombinationNotFound(Long supId, Long infId) {
        when(getCombinationByPrendas.execute(supId, infId))
                .thenThrow(new CombinationNotFoundException("Combinación no encontrada"));
    }

    private CombinationModel givenCombinationCreated(PrendaModel sup, PrendaModel inf) {
        CombinationModel combination = mock(CombinationModel.class);
        when(createCombination.execute(sup, inf)).thenReturn(combination);
        return combination;
    }

    private void givenAttemptRegistered(Long attemptId) {
        when(registerCombinationAttempt.execute(anyString(), any(CombinationModel.class), anyString()))
                .thenReturn(attemptId);
    }

    private String givenValidCombinationUrl() {
        return "http://combination.url/combo123.jpg";
    }

    private void givenAuthenticatedUser(String email) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    private void givenFavoriteAdded(String message) throws Exception {
        when(addCombinationToFavourite.execute(anyString(), anyString())).thenReturn(message);
    }

    private void givenPlanLimitExceeded() throws Exception {
        PlanLimitExceededException exception = new PlanLimitExceededException(
                "Plan limit exceeded", "favorites", 5, 10);
        when(addCombinationToFavourite.execute(anyString(), anyString())).thenThrow(exception);
    }

    private void givenUserNotFound() throws Exception {
        when(addCombinationToFavourite.execute(anyString(), anyString()))
                .thenThrow(new UserNotFoundException("Usuario no encontrado"));
    }

    private void givenFavoriteAlreadyExists() throws Exception {
        when(addCombinationToFavourite.execute(anyString(), anyString()))
                .thenThrow(new UserCombinationFavoriteAlreadyExistsException("Favorito ya existe"));
    }

    private void givenFavoriteDeleted(String message) throws Exception {
        when(deleteCombinationFromFavorite.execute(anyString(), anyString())).thenReturn(message);
    }

    private void givenFavoriteNotFoundForDelete() throws Exception {
        when(deleteCombinationFromFavorite.execute(anyString(), anyString()))
                .thenThrow(new UserCombinationFavoriteNotFoundException("Favorito no encontrado"));
    }

    private void givenFavoritesExist() {
        PageDTO<UserCombinationFavoriteModel> favorites = new PageDTO<>(
                Arrays.asList(mock(UserCombinationFavoriteModel.class)),
                0, 10, 1, 1, true);
        when(getCombinationFavoritesForUserByEmail.execute(anyString(), anyInt())).thenReturn(favorites);
    }

    private void givenNegativePageError() throws Exception {
        when(getCombinationFavoritesForUserByEmail.execute(anyString(), eq(-1)))
                .thenThrow(new PageLessThanZeroException("Página menor que cero"));
    }

    private void givenNoFavorites() throws Exception {
        when(getCombinationFavoritesForUserByEmail.execute(anyString(), anyInt()))
                .thenThrow(new FavoritesException("No hay favoritos"));
    }

    // ========== WHEN Methods ==========

    private ResponseEntity<?> whenCallRegisterAttempt(RegisterAttemptRequest request) {
        return controller.registerAttempt(request);
    }

    private ResponseEntity<?> whenCallAddCombinationToFavorite(String url) {
        return controller.addCombinationToFavorite(url);
    }

    private ResponseEntity<?> whenCallDeleteCombinationFromFavorite(String url) {
        return controller.deleteCombinationFromFavorite(url);
    }

    private ResponseEntity<?> whenCallGetFavorites(int page) {
        return controller.getFavorites(page);
    }

    // ========== THEN Methods ==========

    private void thenResponseOk(ResponseEntity<?> response) {
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    private void thenResponseOkWithMessage(ResponseEntity<?> response, String expectedMessage) {
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedMessage, response.getBody());
    }

    private void thenResponseNotFound(ResponseEntity<?> response, String expectedMessage) {
        assertEquals(404, response.getStatusCode().value());
        assertEquals(expectedMessage, response.getBody());
    }

    private void thenResponseForbidden(ResponseEntity<?> response) {
        assertEquals(403, response.getStatusCode().value());
    }

    private void thenResponseContainsUpgradeRequired(ResponseEntity<?> response) {
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        assertEquals(true, body.get("upgradeRequired"));
        assertNotNull(body.get("error"));
        assertNotNull(body.get("limitType"));
    }

    private void thenVerifyGarmentByCodeCalled(String code) {
        verify(getGarmentByCode, times(1)).execute(code);
    }

    private void thenVerifyGetCombinationByPrendasCalled(Long supId, Long infId) {
        verify(getCombinationByPrendas, times(1)).execute(supId, infId);
    }

    private void thenVerifyCreateCombinationCalled(PrendaModel sup, PrendaModel inf) {
        verify(createCombination, times(1)).execute(sup, inf);
    }

    private void thenVerifyRegisterAttemptCalled() {
        verify(registerCombinationAttempt, times(1))
                .execute(anyString(), any(CombinationModel.class), anyString());
    }

    private void thenVerifyAddCombinationToFavouriteCalled(String url, String email) throws Exception {
        verify(addCombinationToFavourite, times(1)).execute(url, email);
    }

    private void thenVerifyDeleteCombinationFromFavoriteCalled(String url, String email) throws Exception {
        verify(deleteCombinationFromFavorite, times(1)).execute(url, email);
    }

    private void thenVerifyGetFavoritesCalled(String email, int page) throws Exception {
        verify(getCombinationFavoritesForUserByEmail, times(1)).execute(email, page);
    }
}
