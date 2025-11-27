package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.PasswordIsNotTheSame;
import com.outfitlab.project.domain.exceptions.UserAlreadyExistsException;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.domain.model.dto.LoginDTO;
import com.outfitlab.project.domain.model.dto.RegisterDTO;
import com.outfitlab.project.domain.useCases.brand.CreateBrand;
import com.outfitlab.project.domain.useCases.bucketImages.DeleteImage;
import com.outfitlab.project.domain.useCases.bucketImages.SaveImage;
import com.outfitlab.project.domain.useCases.subscription.AssignFreePlanToUser;
import com.outfitlab.project.domain.useCases.user.*;
import com.outfitlab.project.presentation.dto.EditProfileRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private RegisterUser registerUser;
    private LoginUser loginUser;
    private GetAllUsers getAllUsers;
    private DesactivateUser desactivateUser;
    private ActivateUser activateUser;
    private ConvertToAdmin convertToAdmin;
    private ConvertToUser convertToUser;
    private CreateBrand createBrand;
    private UpdateBrandUser updateBrandUser;
    private SaveImage saveImage;
    private GetUserByEmail getUserByEmail;
    private UpdateUser updateUser;
    private DeleteImage deleteImage;
    private AssignFreePlanToUser assignFreePlanToUser;
    private UserProfile userProfile;
    private RefreshToken refreshToken;

    private UserController controller;

    @BeforeEach
    void setUp() {
        registerUser = mock(RegisterUser.class);
        loginUser = mock(LoginUser.class);
        getAllUsers = mock(GetAllUsers.class);
        desactivateUser = mock(DesactivateUser.class);
        activateUser = mock(ActivateUser.class);
        convertToAdmin = mock(ConvertToAdmin.class);
        convertToUser = mock(ConvertToUser.class);
        createBrand = mock(CreateBrand.class);
        updateBrandUser = mock(UpdateBrandUser.class);
        saveImage = mock(SaveImage.class);
        getUserByEmail = mock(GetUserByEmail.class);
        updateUser = mock(UpdateUser.class);
        deleteImage = mock(DeleteImage.class);
        assignFreePlanToUser = mock(AssignFreePlanToUser.class);
        userProfile = mock(UserProfile.class);
        refreshToken = mock(RefreshToken.class);

        controller = new UserController(
                registerUser, loginUser, getAllUsers, desactivateUser, activateUser,
                convertToAdmin, convertToUser, createBrand, updateBrandUser, saveImage,
                getUserByEmail, updateUser, deleteImage, assignFreePlanToUser,
                userProfile, refreshToken);
    }

    // ========== registerUser Tests ==========

    @Test
    void givenValidRequestWhenRegisterUserThenReturnCreated() {
        RegisterDTO request = givenValidRegisterRequest();
        UserModel newUser = givenUserRegistered("test@example.com", "Test User");
        givenFreePlanAssigned();

        ResponseEntity<?> response = whenCallRegisterUser(request);

        thenResponseCreated(response);
        thenResponseContainsUserData(response, "test@example.com", "Test User");
        thenVerifyRegisterUserCalled();
        thenVerifyAssignFreePlanCalled("test@example.com", false);
    }

    @Test
    void givenExistingEmailWhenRegisterUserThenReturnBadRequest() {
        RegisterDTO request = givenValidRegisterRequest();
        givenUserAlreadyExists();

        ResponseEntity<?> response = whenCallRegisterUser(request);

        thenResponseBadRequest(response);
    }

    // ========== registerbrandAndUser Tests ==========

    @Test
    void givenValidBrandRequestWhenRegisterBrandAndUserThenReturnCreated() {
        RegisterDTO request = givenValidBrandRegisterRequest();
        UserModel newUser = givenUserRegistered("brand@example.com", "Brand User");
        givenFreePlanAssigned();
        givenBrandCreated("brand-code");
        givenBrandUpdatedInUser();
        givenImageSaved("http://logo.url/brand.png");

        ResponseEntity<?> response = whenCallRegisterBrandAndUser(request);

        thenResponseCreated(response);
        thenVerifyCreateBrandCalled();
        thenVerifyUpdateBrandUserCalled();
    }

    // ========== loginUser Tests ==========

    @Test
    void givenValidCredentialsWhenLoginUserThenReturnOk() {
        LoginDTO loginDTO = givenValidLoginRequest();
        ResponseEntity<?> loginResponse = givenLoginSuccessful();

        ResponseEntity<?> response = whenCallLoginUser(loginDTO);

        thenResponseOk(response);
        thenVerifyLoginUserCalled();
    }

    @Test
    void givenInvalidCredentialsWhenLoginUserThenReturnUnauthorized() {
        LoginDTO loginDTO = givenValidLoginRequest();
        givenLoginFailed();

        ResponseEntity<?> response = whenCallLoginUser(loginDTO);

        thenResponseUnauthorized(response);
    }

    // ========== refreshToken Tests ==========

    @Test
    void givenValidRefreshTokenWhenRefreshTokenThenReturnOk() {
        String refreshTokenValue = "valid-refresh-token";
        ResponseEntity<?> tokenResponse = givenRefreshTokenSuccessful();

        ResponseEntity<?> response = whenCallRefreshToken(refreshTokenValue);

        thenResponseOk(response);
        thenVerifyRefreshTokenCalled(refreshTokenValue);
    }

    @Test
    void givenInvalidRefreshTokenWhenRefreshTokenThenReturnUnauthorized() {
        String refreshTokenValue = "invalid-token";
        givenRefreshTokenFailed();

        ResponseEntity<?> response = whenCallRefreshToken(refreshTokenValue);

        thenResponseUnauthorized(response);
    }

    // ========== getAuthUserProfile Tests ==========

    @Test
    void whenGetAuthUserProfileThenReturnOk() {
        UserModel user = givenUserProfileExists();

        ResponseEntity<?> response = whenCallGetAuthUserProfile();

        thenResponseOk(response);
        thenVerifyUserProfileCalled();
    }

    @Test
    void givenUserNotFoundWhenGetAuthUserProfileThenReturnUnauthorized() {
        givenUserProfileNotFound();

        ResponseEntity<?> response = whenCallGetAuthUserProfile();

        thenResponseUnauthorized(response);
    }

    // ========== getAllUsers Tests ==========

    @Test
    void whenGetAllUsersThenReturnOk() {
        List<UserModel> users = givenUsersExist();

        ResponseEntity<?> response = whenCallGetAllUsers();

        thenResponseOk(response);
        thenVerifyGetAllUsersCalled();
    }

    @Test
    void givenNoUsersWhenGetAllUsersThenReturn404() {
        givenNoUsersFound();

        ResponseEntity<?> response = whenCallGetAllUsers();

        thenResponseNotFound(response, "No hay usuarios");
    }

    // ========== desactivateUser Tests ==========

    @Test
    void givenValidEmailWhenDesactivateUserThenReturnOk() {
        String email = "user@example.com";
        givenUserDesactivated("Usuario desactivado");

        ResponseEntity<?> response = whenCallDesactivateUser(email);

        thenResponseOk(response);
        thenVerifyDesactivateUserCalled(email);
    }

    @Test
    void givenInvalidEmailWhenDesactivateUserThenReturn404() {
        String email = "invalid@example.com";
        givenUserNotFoundForDesactivate(email);

        ResponseEntity<?> response = whenCallDesactivateUser(email);

        thenResponseNotFound(response, "Usuario no encontrado");
    }

    // ========== activateUser Tests ==========

    @Test
    void givenValidEmailWhenActivateUserThenReturnOk() {
        String email = "user@example.com";
        givenUserActivated("Usuario activado");

        ResponseEntity<?> response = whenCallActivateUser(email);

        thenResponseOk(response);
        thenVerifyActivateUserCalled(email);
    }

    // ========== convertToAdmin Tests ==========

    @Test
    void givenValidEmailWhenConvertToAdminThenReturnOk() {
        String email = "user@example.com";
        givenUserConvertedToAdmin("Usuario convertido a admin");

        ResponseEntity<?> response = whenCallConvertToAdmin(email);

        thenResponseOk(response);
        thenVerifyConvertToAdminCalled(email);
    }

    // ========== convertToUser Tests ==========

    @Test
    void givenValidEmailWhenConvertToUserThenReturnOk() {
        String email = "admin@example.com";
        givenUserConvertedToUser("Admin convertido a usuario");

        ResponseEntity<?> response = whenCallConvertToUser(email);

        thenResponseOk(response);
        thenVerifyConvertToUserCalled(email);
    }

    // ========== updateUser Tests ==========

    @Test
    void givenValidRequestWhenUpdateUserThenReturnOk() {
        String oldEmail = "old@example.com";
        EditProfileRequestDTO request = givenValidEditProfileRequest();
        UserModel updatedUser = givenUserUpdated();
        givenUserImageUrl("http://old-image.url/user.jpg");
        givenImageSaved("http://new-image.url/user.jpg"); // Add image save mock

        ResponseEntity<?> response = whenCallUpdateUser(oldEmail, request);

        thenResponseOk(response);
        thenResponseContainsMessage(response, "Perfil actualizado.");
        thenVerifyUpdateUserCalled();
    }

    @Test
    void givenPasswordMismatchWhenUpdateUserThenReturn404() {
        String oldEmail = "user@example.com";
        EditProfileRequestDTO request = givenValidEditProfileRequest();
        givenPasswordMismatch();
        givenImageSaved("http://new-image.url/user.jpg"); // Add image save mock

        ResponseEntity<?> response = whenCallUpdateUser(oldEmail, request);

        thenResponseNotFound(response, "Las contraseñas no coinciden");
    }

    // ========== GIVEN Methods ==========

    private RegisterDTO givenValidRegisterRequest() {
        RegisterDTO request = new RegisterDTO();
        request.setEmail("test@example.com");
        request.setName("Test");
        request.setLastName("User");
        request.setPassword("Password123");
        return request;
    }

    private RegisterDTO givenValidBrandRegisterRequest() {
        RegisterDTO request = givenValidRegisterRequest();
        request.setEmail("brand@example.com");
        request.setBrandName("Test Brand");
        request.setUrlSite("http://brand.com");
        request.setLogoBrand(mock(MultipartFile.class));
        return request;
    }

    private UserModel givenUserRegistered(String email, String name) {
        UserModel user = mock(UserModel.class);
        when(user.getEmail()).thenReturn(email);
        when(user.getName()).thenReturn(name);
        when(registerUser.execute(any(RegisterDTO.class))).thenReturn(user);
        return user;
    }

    private void givenUserAlreadyExists() {
        when(registerUser.execute(any(RegisterDTO.class)))
                .thenThrow(new UserAlreadyExistsException("El usuario ya existe"));
    }

    private void givenFreePlanAssigned() {
        doNothing().when(assignFreePlanToUser).execute(anyString(), anyBoolean());
    }

    private void givenBrandCreated(String brandCode) {
        when(createBrand.execute(anyString(), anyString(), anyString())).thenReturn(brandCode);
    }

    private void givenBrandUpdatedInUser() {
        doNothing().when(updateBrandUser).execute(anyString(), anyString());
    }

    private void givenImageSaved(String url) {
        when(saveImage.execute(any(MultipartFile.class), anyString())).thenReturn(url);
    }

    private LoginDTO givenValidLoginRequest() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("Password123");
        return loginDTO;
    }

    private ResponseEntity<?> givenLoginSuccessful() {
        ResponseEntity<?> response = ResponseEntity.ok("Login successful");
        doReturn(response).when(loginUser).execute(any(LoginDTO.class));
        return response;
    }

    private void givenLoginFailed() {
        when(loginUser.execute(any(LoginDTO.class)))
                .thenThrow(new UserNotFoundException("Usuario no encontrado"));
    }

    private ResponseEntity<?> givenRefreshTokenSuccessful() {
        ResponseEntity<?> response = ResponseEntity.ok("Token refreshed");
        doReturn(response).when(refreshToken).execute(anyString());
        return response;
    }

    private void givenRefreshTokenFailed() {
        when(refreshToken.execute(anyString()))
                .thenThrow(new UserNotFoundException("Token inválido"));
    }

    private UserModel givenUserProfileExists() {
        UserModel user = mock(UserModel.class);
        when(userProfile.execute()).thenReturn(user);
        return user;
    }

    private void givenUserProfileNotFound() {
        when(userProfile.execute()).thenThrow(new UserNotFoundException("Usuario no encontrado"));
    }

    private List<UserModel> givenUsersExist() {
        UserModel user1 = mock(UserModel.class);
        UserModel user2 = mock(UserModel.class);
        List<UserModel> users = Arrays.asList(user1, user2);
        doReturn(users).when(getAllUsers).execute();
        return users;
    }

    private void givenNoUsersFound() {
        when(getAllUsers.execute()).thenThrow(new UserNotFoundException("No hay usuarios"));
    }

    private void givenUserDesactivated(String message) {
        when(desactivateUser.execute(anyString())).thenReturn(message);
    }

    private void givenUserNotFoundForDesactivate(String email) {
        when(desactivateUser.execute(email))
                .thenThrow(new UserNotFoundException("Usuario no encontrado"));
    }

    private void givenUserActivated(String message) {
        when(activateUser.execute(anyString())).thenReturn(message);
    }

    private void givenUserConvertedToAdmin(String message) {
        when(convertToAdmin.execute(anyString())).thenReturn(message);
    }

    private void givenUserConvertedToUser(String message) {
        when(convertToUser.execute(anyString())).thenReturn(message);
    }

    private EditProfileRequestDTO givenValidEditProfileRequest() {
        EditProfileRequestDTO request = new EditProfileRequestDTO();
        request.setName("Updated");
        request.setLastname("User");
        request.setEmail("updated@example.com");
        request.setPassword("NewPassword123");
        request.setConfirmPassword("NewPassword123");
        request.setUserImg(mock(MultipartFile.class));
        return request;
    }

    private UserModel givenUserUpdated() {
        UserModel user = mock(UserModel.class);
        when(updateUser.execute(anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString())).thenReturn(user);
        return user;
    }

    private void givenUserImageUrl(String url) {
        UserModel user = mock(UserModel.class);
        when(user.getUserImg()).thenReturn(url);
        when(getUserByEmail.execute(anyString())).thenReturn(user);
    }

    private void givenPasswordMismatch() {
        // Mock getUserByEmail to return a user with image URL
        UserModel existingUser = mock(UserModel.class);
        when(existingUser.getUserImg()).thenReturn("http://old-image.url/user.jpg");
        when(getUserByEmail.execute(anyString())).thenReturn(existingUser);

        // Mock updateUser to throw exception
        when(updateUser.execute(anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString()))
                .thenThrow(new PasswordIsNotTheSame("Las contraseñas no coinciden"));
    }

    // ========== WHEN Methods ==========

    private ResponseEntity<?> whenCallRegisterUser(RegisterDTO request) {
        return controller.registerUser(request);
    }

    private ResponseEntity<?> whenCallRegisterBrandAndUser(RegisterDTO request) {
        return controller.registerbrandAndUser(request);
    }

    private ResponseEntity<?> whenCallLoginUser(LoginDTO loginDTO) {
        return controller.loginUser(loginDTO);
    }

    private ResponseEntity<?> whenCallRefreshToken(String refreshTokenValue) {
        Map<String, String> request = Map.of("refresh_token", refreshTokenValue);
        return controller.refreshToken(request);
    }

    private ResponseEntity<?> whenCallGetAuthUserProfile() {
        return controller.getAuthUserProfile();
    }

    private ResponseEntity<?> whenCallGetAllUsers() {
        return controller.getAllUsers();
    }

    private ResponseEntity<?> whenCallDesactivateUser(String email) {
        return controller.desactivateUser(email);
    }

    private ResponseEntity<?> whenCallActivateUser(String email) {
        return controller.activateUser(email);
    }

    private ResponseEntity<?> whenCallConvertToAdmin(String email) {
        return controller.convertToAdmin(email);
    }

    private ResponseEntity<?> whenCallConvertToUser(String email) {
        return controller.convertToUser(email);
    }

    private ResponseEntity<?> whenCallUpdateUser(String oldEmail, EditProfileRequestDTO request) {
        return controller.updateUser(oldEmail, request);
    }

    // ========== THEN Methods ==========

    private void thenResponseOk(ResponseEntity<?> response) {
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    private void thenResponseCreated(ResponseEntity<?> response) {
        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    private void thenResponseBadRequest(ResponseEntity<?> response) {
        assertEquals(400, response.getStatusCode().value());
    }

    private void thenResponseUnauthorized(ResponseEntity<?> response) {
        assertEquals(401, response.getStatusCode().value());
    }

    private void thenResponseNotFound(ResponseEntity<?> response, String expectedMessage) {
        assertEquals(404, response.getStatusCode().value());
        assertEquals(expectedMessage, response.getBody());
    }

    private void thenResponseContainsUserData(ResponseEntity<?> response, String email, String name) {
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        assertEquals(email, body.get("email"));
        assertEquals(name, body.get("name"));
    }

    private void thenResponseContainsMessage(ResponseEntity<?> response, String expectedMessage) {
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        assertEquals(expectedMessage, body.get("message"));
    }

    private void thenVerifyRegisterUserCalled() {
        verify(registerUser, times(1)).execute(any(RegisterDTO.class));
    }

    private void thenVerifyAssignFreePlanCalled(String email, boolean isBrand) {
        verify(assignFreePlanToUser, times(1)).execute(email, isBrand);
    }

    private void thenVerifyCreateBrandCalled() {
        verify(createBrand, times(1)).execute(anyString(), anyString(), anyString());
    }

    private void thenVerifyUpdateBrandUserCalled() {
        verify(updateBrandUser, times(1)).execute(anyString(), anyString());
    }

    private void thenVerifyLoginUserCalled() {
        verify(loginUser, times(1)).execute(any(LoginDTO.class));
    }

    private void thenVerifyRefreshTokenCalled(String token) {
        verify(refreshToken, times(1)).execute(token);
    }

    private void thenVerifyUserProfileCalled() {
        verify(userProfile, times(1)).execute();
    }

    private void thenVerifyGetAllUsersCalled() {
        verify(getAllUsers, times(1)).execute();
    }

    private void thenVerifyDesactivateUserCalled(String email) {
        verify(desactivateUser, times(1)).execute(email);
    }

    private void thenVerifyActivateUserCalled(String email) {
        verify(activateUser, times(1)).execute(email);
    }

    private void thenVerifyConvertToAdminCalled(String email) {
        verify(convertToAdmin, times(1)).execute(email);
    }

    private void thenVerifyConvertToUserCalled(String email) {
        verify(convertToUser, times(1)).execute(email);
    }

    private void thenVerifyUpdateUserCalled() {
        verify(updateUser, times(1)).execute(
                anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString());
    }
}
