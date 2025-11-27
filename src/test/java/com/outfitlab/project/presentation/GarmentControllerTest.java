package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.model.dto.GarmentDTO;
import com.outfitlab.project.domain.model.dto.PageDTO;
import com.outfitlab.project.domain.useCases.bucketImages.DeleteImage;
import com.outfitlab.project.domain.useCases.bucketImages.SaveImage;
import com.outfitlab.project.domain.useCases.combination.DeleteAllCombinationRelatedToGarment;
import com.outfitlab.project.domain.useCases.combinationAttempt.DeleteAllCombinationAttempsRelatedToCombinationsRelatedToGarment;
import com.outfitlab.project.domain.useCases.garment.*;
import com.outfitlab.project.domain.useCases.recomendations.CreateSugerenciasByGarmentsCode;
import com.outfitlab.project.domain.useCases.recomendations.DeleteAllPrendaOcacionRelatedToGarment;
import com.outfitlab.project.domain.useCases.recomendations.DeleteGarmentRecomentationsRelatedToGarment;
import com.outfitlab.project.presentation.dto.GarmentRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GarmentControllerTest {

    private GetGarmentsByType getGarmentsByType;
    private AddGarmentToFavorite addGarmentToFavorite;
    private DeleteGarmentFromFavorite deleteGarmentFromFavorite;
    private GetGarmentsFavoritesForUserByEmail getGarmentsFavoritesForUserByEmail;
    private CreateGarment createGarment;
    private SaveImage saveImage;
    private DeleteGarment deleteGarment;
    private GetGarmentByCode getGarmentByCode;
    private DeleteImage deleteImage;
    private UpdateGarment updateGarment;
    private DeleteGarmentRecomentationsRelatedToGarment deleteGarmentRecomentationsRelatedToGarment;
    private DeleteAllFavoritesRelatedToGarment deleteAllFavoritesRelatedToGarment;
    private DeleteAllPrendaOcacionRelatedToGarment deleteAllPrendaOcacionRelatedToGarment;
    private DeleteAllCombinationRelatedToGarment deleteAllCombinationRelatedToGarment;
    private DeleteAllCombinationAttempsRelatedToCombinationsRelatedToGarment deleteAllCombinationAttempsRelatedToCombinationsRelatedToGarment;
    private CreateSugerenciasByGarmentsCode createSugerenciasByGarmentsCode;

    private GarmentController controller;
    private UserDetails mockUser;

    @BeforeEach
    void setUp() {
        getGarmentsByType = mock(GetGarmentsByType.class);
        addGarmentToFavorite = mock(AddGarmentToFavorite.class);
        deleteGarmentFromFavorite = mock(DeleteGarmentFromFavorite.class);
        getGarmentsFavoritesForUserByEmail = mock(GetGarmentsFavoritesForUserByEmail.class);
        createGarment = mock(CreateGarment.class);
        saveImage = mock(SaveImage.class);
        deleteGarment = mock(DeleteGarment.class);
        getGarmentByCode = mock(GetGarmentByCode.class);
        deleteImage = mock(DeleteImage.class);
        updateGarment = mock(UpdateGarment.class);
        deleteGarmentRecomentationsRelatedToGarment = mock(DeleteGarmentRecomentationsRelatedToGarment.class);
        deleteAllFavoritesRelatedToGarment = mock(DeleteAllFavoritesRelatedToGarment.class);
        deleteAllPrendaOcacionRelatedToGarment = mock(DeleteAllPrendaOcacionRelatedToGarment.class);
        deleteAllCombinationRelatedToGarment = mock(DeleteAllCombinationRelatedToGarment.class);
        deleteAllCombinationAttempsRelatedToCombinationsRelatedToGarment = mock(
                DeleteAllCombinationAttempsRelatedToCombinationsRelatedToGarment.class);
        createSugerenciasByGarmentsCode = mock(CreateSugerenciasByGarmentsCode.class);

        controller = new GarmentController(
                getGarmentsByType, addGarmentToFavorite, deleteGarmentFromFavorite,
                getGarmentsFavoritesForUserByEmail, createGarment, saveImage, deleteGarment,
                null, getGarmentByCode, deleteImage, updateGarment,
                deleteGarmentRecomentationsRelatedToGarment, deleteAllFavoritesRelatedToGarment,
                deleteAllPrendaOcacionRelatedToGarment, deleteAllCombinationRelatedToGarment,
                deleteAllCombinationAttempsRelatedToCombinationsRelatedToGarment,
                createSugerenciasByGarmentsCode);

        mockUser = mock(UserDetails.class);
    }

    // ========== getGarmentsByType Tests ==========

    @Test
    void givenValidTypeWhenGetGarmentsByTypeThenReturnOk() {
        Page<PrendaModel> garments = givenGarmentsExistForType("superior");

        ResponseEntity<?> response = whenCallGetGarmentsByType(0, "superior");

        thenResponseOkWithGarments(response);
        thenVerifyGetGarmentsByTypeCalled("superior", 0);
    }

    @Test
    void givenInvalidTypeWhenGetGarmentsByTypeThenReturn404() {
        givenGarmentsNotFoundForType("invalid");

        ResponseEntity<?> response = whenCallGetGarmentsByType(0, "invalid");

        thenResponseNotFound(response, "Prendas no encontradas");
    }

    // ========== getAllGarments Tests ==========

    @Test
    void givenValidPageWhenGetAllGarmentsThenReturnOk() {
        givenGarmentsExistForType("superior");
        givenGarmentsExistForType("inferior");

        ResponseEntity<?> response = whenCallGetAllGarments(0);

        thenResponseOk(response);
    }

    // ========== addGarmentToFavorite Tests ==========

    @Test
    void givenValidGarmentCodeWhenAddGarmentToFavoriteThenReturnOk() throws Exception {
        String garmentCode = "nike-shirt-001";
        givenFavoriteAdded("Favorito agregado");

        ResponseEntity<?> response = whenCallAddGarmentToFavorite(garmentCode);

        thenResponseOk(response);
        thenVerifyAddGarmentToFavoriteCalled(garmentCode, "german@gmail.com");
    }

    @Test
    void givenGarmentNotFoundWhenAddGarmentToFavoriteThenReturn404() throws Exception {
        String garmentCode = "invalid-code";
        givenGarmentNotFoundForFavorite(garmentCode);

        ResponseEntity<?> response = whenCallAddGarmentToFavorite(garmentCode);

        thenResponseNotFound(response, "Prenda no encontrada");
    }

    @Test
    void givenFavoriteAlreadyExistsWhenAddGarmentToFavoriteThenReturn404() throws Exception {
        String garmentCode = "nike-shirt-001";
        givenFavoriteAlreadyExists(garmentCode);

        ResponseEntity<?> response = whenCallAddGarmentToFavorite(garmentCode);

        thenResponseNotFound(response, "Favorito ya existe");
    }

    // ========== deleteGarmentFromFavorite Tests ==========

    @Test
    void givenValidGarmentCodeWhenDeleteGarmentFromFavoriteThenReturnOk() throws Exception {
        String garmentCode = "nike-shirt-001";
        givenFavoriteDeleted("Favorito eliminado");

        ResponseEntity<?> response = whenCallDeleteGarmentFromFavorite(garmentCode);

        thenResponseOk(response);
        thenVerifyDeleteGarmentFromFavoriteCalled(garmentCode, "german@gmail.com");
    }

    @Test
    void givenFavoriteNotFoundWhenDeleteGarmentFromFavoriteThenReturn404() throws Exception {
        String garmentCode = "invalid-code";
        givenFavoriteNotFoundForDelete(garmentCode);

        ResponseEntity<?> response = whenCallDeleteGarmentFromFavorite(garmentCode);

        thenResponseNotFound(response, "Favorito no encontrado");
    }

    // ========== getFavorites Tests ==========

    @Test
    void givenValidPageWhenGetFavoritesThenReturnOk() throws Exception {
        givenFavoritesExist();

        ResponseEntity<?> response = whenCallGetFavorites(0);

        thenResponseOk(response);
    }

    @Test
    void givenNegativePageWhenGetFavoritesThenReturn404() throws Exception {
        givenNegativePageError();

        ResponseEntity<?> response = whenCallGetFavorites(-1);

        thenResponseNotFound(response, "Página menor que cero");
    }

    // ========== newGarment Tests ==========

    @Test
    void givenValidRequestWhenNewGarmentThenReturnOk() {
        GarmentRequestDTO request = givenValidGarmentRequest();
        givenImageSaved("http://image.url/garment.jpg");
        givenGarmentCreated();
        givenSugerenciasCreated();

        ResponseEntity<?> response = whenCallNewGarment(request);

        thenResponseOkWithMessage(response, "Prenda creada correctamente.");
        thenVerifyCreateGarmentCalled();
    }

    @Test
    void givenInvalidBrandWhenNewGarmentThenReturn404() {
        GarmentRequestDTO request = givenValidGarmentRequest();
        givenImageSaved("http://image.url/garment.jpg");
        givenBrandNotFound();

        ResponseEntity<?> response = whenCallNewGarment(request);

        thenResponseNotFound(response, "Marca no encontrada");
    }

    // ========== updateGarment Tests ==========

    @Test
    void givenValidRequestWhenUpdateGarmentThenReturnOk() {
        String garmentCode = "nike-shirt-001";
        GarmentRequestDTO request = givenValidGarmentRequestWithImage();
        PrendaModel existingGarment = givenGarmentExists(garmentCode);
        givenImageSaved("http://image.url/new-garment.jpg");
        givenGarmentUpdated();
        givenSugerenciasCreated();

        ResponseEntity<?> response = whenCallUpdateGarment(garmentCode, request);

        thenResponseOkWithMessage(response, "Prenda actualizada correctamente.");
        thenVerifyUpdateGarmentCalled();
    }

    @Test
    void givenInvalidGarmentCodeWhenUpdateGarmentThenReturn404() {
        String garmentCode = "invalid-code";
        GarmentRequestDTO request = givenValidGarmentRequest();
        givenGarmentNotFound(garmentCode);

        ResponseEntity<?> response = whenCallUpdateGarment(garmentCode, request);

        thenResponseNotFound(response, "Prenda no encontrada");
    }

    // ========== deleteGarment Tests ==========

    @Test
    void givenValidGarmentCodeWhenDeleteGarmentThenReturnOk() {
        String garmentCode = "nike-shirt-001";
        PrendaModel garment = givenGarmentExistsForDelete(garmentCode);
        givenAllRelatedRecordsDeleted();

        ResponseEntity<?> response = whenCallDeleteGarment(garmentCode);

        thenResponseOkWithMessage(response, "Prenda eliminada correctamente.");
        thenVerifyDeleteGarmentCalled();
    }

    @Test
    void givenInvalidGarmentCodeWhenDeleteGarmentThenReturn404() {
        String garmentCode = "invalid-code";
        givenGarmentNotFound(garmentCode);

        ResponseEntity<?> response = whenCallDeleteGarment(garmentCode);

        thenResponseNotFound(response, "Prenda no encontrada");
    }

    // ========== GIVEN Methods ==========

    private Page<PrendaModel> givenGarmentsExistForType(String type) {
        PrendaModel garment1 = mock(PrendaModel.class);
        PrendaModel garment2 = mock(PrendaModel.class);

        // Mock BrandModel for each garment
        com.outfitlab.project.domain.model.BrandModel brand1 = mock(
                com.outfitlab.project.domain.model.BrandModel.class);
        com.outfitlab.project.domain.model.BrandModel brand2 = mock(
                com.outfitlab.project.domain.model.BrandModel.class);
        when(brand1.getNombre()).thenReturn("Nike");
        when(brand2.getNombre()).thenReturn("Adidas");
        when(garment1.getMarca()).thenReturn(brand1);
        when(garment2.getMarca()).thenReturn(brand2);

        // Mock ColorModel for each garment
        com.outfitlab.project.domain.model.ColorModel color1 = mock(
                com.outfitlab.project.domain.model.ColorModel.class);
        com.outfitlab.project.domain.model.ColorModel color2 = mock(
                com.outfitlab.project.domain.model.ColorModel.class);
        when(color1.getNombre()).thenReturn("Rojo");
        when(color2.getNombre()).thenReturn("Azul");
        when(garment1.getColor()).thenReturn(color1);
        when(garment2.getColor()).thenReturn(color2);

        // Mock ClimaModel for each garment
        com.outfitlab.project.domain.model.ClimaModel clima1 = mock(
                com.outfitlab.project.domain.model.ClimaModel.class);
        com.outfitlab.project.domain.model.ClimaModel clima2 = mock(
                com.outfitlab.project.domain.model.ClimaModel.class);
        when(clima1.getNombre()).thenReturn("Calido");
        when(clima2.getNombre()).thenReturn("Frio");
        when(garment1.getClimaAdecuado()).thenReturn(clima1);
        when(garment2.getClimaAdecuado()).thenReturn(clima2);

        Page<PrendaModel> page = new PageImpl<>(Arrays.asList(garment1, garment2), PageRequest.of(0, 10), 2);
        when(getGarmentsByType.execute(type, 0)).thenReturn(page);
        return page;
    }

    private void givenGarmentsNotFoundForType(String type) {
        when(getGarmentsByType.execute(type, 0))
                .thenThrow(new GarmentNotFoundException("Prendas no encontradas"));
    }

    private void givenFavoriteAdded(String message) throws Exception {
        when(addGarmentToFavorite.execute(anyString(), anyString())).thenReturn(message);
    }

    private void givenGarmentNotFoundForFavorite(String garmentCode) throws Exception {
        when(addGarmentToFavorite.execute(eq(garmentCode), anyString()))
                .thenThrow(new GarmentNotFoundException("Prenda no encontrada"));
    }

    private void givenFavoriteAlreadyExists(String garmentCode) throws Exception {
        when(addGarmentToFavorite.execute(eq(garmentCode), anyString()))
                .thenThrow(new UserGarmentFavoriteAlreadyExistsException("Favorito ya existe"));
    }

    private void givenFavoriteDeleted(String message) throws Exception {
        when(deleteGarmentFromFavorite.execute(anyString(), anyString())).thenReturn(message);
    }

    private void givenFavoriteNotFoundForDelete(String garmentCode) throws Exception {
        when(deleteGarmentFromFavorite.execute(eq(garmentCode), anyString()))
                .thenThrow(new UserGarmentFavoriteNotFoundException("Favorito no encontrado"));
    }

    private void givenFavoritesExist() throws Exception {
        PageDTO<PrendaModel> favorites = new PageDTO<>(
                Arrays.asList(mock(PrendaModel.class), mock(PrendaModel.class)),
                0, 10, 2, 1, true);
        when(getGarmentsFavoritesForUserByEmail.execute(anyString(), anyInt())).thenReturn(favorites);
    }

    private void givenNegativePageError() throws Exception {
        when(getGarmentsFavoritesForUserByEmail.execute(anyString(), eq(-1)))
                .thenThrow(new PageLessThanZeroException("Página menor que cero"));
    }

    private GarmentRequestDTO givenValidGarmentRequest() {
        GarmentRequestDTO request = new GarmentRequestDTO();
        request.setNombre("Nike Shirt");
        request.setTipo("superior");
        request.setColorNombre("Rojo");
        request.setCodigoMarca("nike");
        request.setClimaNombre("Calido");
        request.setOcasionesNombres(Arrays.asList("Casual", "Deporte"));
        request.setGenero("Masculino");
        request.setSugerencias(Arrays.asList("sug1", "sug2"));
        request.setImagen(mock(MultipartFile.class));
        return request;
    }

    private GarmentRequestDTO givenValidGarmentRequestWithImage() {
        GarmentRequestDTO request = givenValidGarmentRequest();
        request.setImagen(mock(MultipartFile.class));
        request.setEvento("Casual"); // Add evento field
        return request;
    }

    private void givenImageSaved(String url) {
        when(saveImage.execute(any(MultipartFile.class), anyString())).thenReturn(url);
    }

    private void givenGarmentCreated() {
        doNothing().when(createGarment).execute(
                anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyList(), anyString());
    }

    private void givenSugerenciasCreated() {
        doNothing().when(createSugerenciasByGarmentsCode).execute(anyString(), anyString(), anyList());
    }

    private void givenBrandNotFound() {
        doThrow(new BrandsNotFoundException("Marca no encontrada"))
                .when(createGarment).execute(
                        anyString(), anyString(), anyString(), anyString(),
                        anyString(), anyString(), anyList(), anyString());
    }

    private PrendaModel givenGarmentExists(String garmentCode) {
        PrendaModel garment = mock(PrendaModel.class);
        when(garment.getImagenUrl()).thenReturn("http://old-image.url/garment.jpg");
        when(garment.getMarca()).thenReturn(mock(com.outfitlab.project.domain.model.BrandModel.class));
        when(garment.getMarca().getCodigoMarca()).thenReturn("nike");
        when(getGarmentByCode.execute(garmentCode)).thenReturn(garment);
        return garment;
    }

    private void givenGarmentNotFound(String garmentCode) {
        when(getGarmentByCode.execute(garmentCode))
                .thenThrow(new GarmentNotFoundException("Prenda no encontrada"));
    }

    private void givenGarmentUpdated() {
        doNothing().when(updateGarment).execute(
                anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyList(), anyString());
    }

    private PrendaModel givenGarmentExistsForDelete(String garmentCode) {
        PrendaModel garment = mock(PrendaModel.class);
        when(garment.getImagenUrl()).thenReturn("http://image.url/garment.jpg");
        when(garment.getGarmentCode()).thenReturn(garmentCode);
        when(garment.getMarca()).thenReturn(mock(com.outfitlab.project.domain.model.BrandModel.class));
        when(garment.getMarca().getCodigoMarca()).thenReturn("nike");
        when(getGarmentByCode.execute(garmentCode)).thenReturn(garment);
        return garment;
    }

    private void givenAllRelatedRecordsDeleted() {
        doNothing().when(deleteGarmentRecomentationsRelatedToGarment).execute(anyString());
        doNothing().when(deleteAllFavoritesRelatedToGarment).execute(anyString());
        doNothing().when(deleteAllPrendaOcacionRelatedToGarment).execute(anyString());
        doNothing().when(deleteAllCombinationAttempsRelatedToCombinationsRelatedToGarment).execute(anyString());
        doNothing().when(deleteAllCombinationRelatedToGarment).execute(anyString());
        doNothing().when(deleteGarment).execute(any(PrendaModel.class), anyString());
    }

    // ========== WHEN Methods ==========

    private ResponseEntity<?> whenCallGetGarmentsByType(int page, String type) {
        return controller.getGarmentsByType(page, type);
    }

    private ResponseEntity<?> whenCallGetAllGarments(int page) {
        return controller.getAllGarments(page);
    }

    private ResponseEntity<?> whenCallAddGarmentToFavorite(String garmentCode) {
        return controller.addGarmentToFavorite(garmentCode);
    }

    private ResponseEntity<?> whenCallDeleteGarmentFromFavorite(String garmentCode) {
        return controller.deleteGarmentFromFavorite(garmentCode);
    }

    private ResponseEntity<?> whenCallGetFavorites(int page) {
        return controller.getFavorites(page);
    }

    private ResponseEntity<?> whenCallNewGarment(GarmentRequestDTO request) {
        return controller.newGarment(request, mockUser);
    }

    private ResponseEntity<?> whenCallUpdateGarment(String garmentCode, GarmentRequestDTO request) {
        return controller.updateGarment(garmentCode, request, mockUser);
    }

    private ResponseEntity<?> whenCallDeleteGarment(String garmentCode) {
        return controller.deleteGarment(garmentCode, mockUser);
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

    private void thenResponseOkWithGarments(ResponseEntity<?> response) {
        assertEquals(200, response.getStatusCode().value());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        assertNotNull(body.get("content"));
    }

    private void thenResponseNotFound(ResponseEntity<?> response, String expectedMessage) {
        assertEquals(404, response.getStatusCode().value());
        assertEquals(expectedMessage, response.getBody());
    }

    private void thenVerifyGetGarmentsByTypeCalled(String type, int page) {
        verify(getGarmentsByType, times(1)).execute(type, page);
    }

    private void thenVerifyAddGarmentToFavoriteCalled(String garmentCode, String email) throws Exception {
        verify(addGarmentToFavorite, times(1)).execute(garmentCode, email);
    }

    private void thenVerifyDeleteGarmentFromFavoriteCalled(String garmentCode, String email) throws Exception {
        verify(deleteGarmentFromFavorite, times(1)).execute(garmentCode, email);
    }

    private void thenVerifyCreateGarmentCalled() {
        verify(createGarment, times(1)).execute(
                anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyList(), anyString());
    }

    private void thenVerifyUpdateGarmentCalled() {
        verify(updateGarment, times(1)).execute(
                anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyList(), anyString());
    }

    private void thenVerifyDeleteGarmentCalled() {
        verify(deleteGarment, times(1)).execute(any(PrendaModel.class), anyString());
    }
}
