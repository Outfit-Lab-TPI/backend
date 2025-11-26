package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.enums.Role;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.infrastructure.model.MarcaEntity;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.BrandJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryImplTest {
    @Mock
    private UserJpaRepository userJpaRepository;
    @Mock
    private BrandJpaRepository brandJpaRepository;
    @InjectMocks
    private UserRepositoryImpl userRepository;

    @Test
    void shouldReturnUserByEmail() {
        givenExistingUserWithEmail("test@mail.com");

        UserModel result = whenFindUserByEmail("test@mail.com");

        thenReturnUserWithEmail(result, "test@mail.com");
    }

    @Test
    void shouldThrowExceptionWhenEmailDoesNotExist() {
        String email = "test15@mail.com";

        whenFindingByEmail(email, null);

        thenExceptionShouldBeThrown(email);
        verify(userJpaRepository).findByEmail(email);
        verify(userJpaRepository, never()).save(any());
    }

    @Test
    void shouldReturnUserModelWhenTokenIsValid() {
        String token = "abc123";
        givenUserWithVerificationToken(token, "test@mail.com", 1L);

        UserModel result = whenFindUserByVerificationToken(token);

        thenUserShouldHaveEmail(result, "test@mail.com");
        verify(userJpaRepository).findByVerificationToken(token);
    }


    @Test
    void shouldThrowExceptionWhenTokenIsInvalid() {
        String token = "def456";
        givenNoUserWithToken(token);

        thenExpectUserNotFoundByToken(token);

        verify(userJpaRepository).findByVerificationToken(token);
    }


    @Test
    void shouldReturnUserModelWhenIdExists() {
        Long id = 1L;
        givenUserWithId(id, "test@mail.com");

        UserModel result = whenFindById(id);

        thenUserShouldHaveId(result, id);
        thenUserShouldHaveEmail(result, "test@mail.com");
        verify(userJpaRepository).findById(id);
    }


    @Test
    void shouldThrowExceptionWhenIdDoesNotExist() {
        Long id = 99L;
        givenNoUserWithId(id);

        thenExpectUserNotFoundById(id);

        verify(userJpaRepository).findById(id);
    }

    @Test
    void shouldPersistAndReturnUserModelWhenSavingUser() {
        UserModel model = givenUserModel("save@mail.com", "Juan");
        UserEntity savedEntity = givenSavedEntity(model, 1L);

        whenSavingUserEntity(savedEntity);
        UserModel result = whenSavingUser(model);

        thenUserEmailShouldBe(result, "save@mail.com");
        thenUserIdShouldBeNull(result);
        verify(userJpaRepository).save(any(UserEntity.class));
    }

    @Test
    void shouldReturnUserListWhenUsersExist() {
        List<UserEntity> entities = givenUserEntityList();

        whenFindingAllUsers(entities);
        List<UserModel> result = whenCallingFindAll();

        thenResultShouldHaveSize(result, 2);
        thenUserEmailShouldBe(result.get(0), "u1@mail.com");
        thenUserEmailShouldBe(result.get(1), "u2@mail.com");
        verify(userJpaRepository).findAll();
    }

    @Test
    void shouldDeactivateUserWhenEmailExists() {
        String email = "test@mail.com";
        UserEntity entity = givenActiveUserEntity(email);

        whenFindingByEmail(email, entity);
        whenDeactivatingUser(email);

        thenUserShouldBeDeactivated(entity);
        verify(userJpaRepository).findByEmail(email);
        verify(userJpaRepository).save(entity);
    }

    @Test
    void shouldThrowExceptionWhenDeactivatingNonExistingUser() {
        String email = "test17@mail.com";

        whenFindingByEmail(email, null);

        thenDesactivateShouldThrowException(email);
        verify(userJpaRepository).findByEmail(email);
        verify(userJpaRepository, never()).save(any());
    }

    @Test
    void shouldActivateUserWhenEmailExists() {
        String email = "test@mail.com";
        UserEntity entity = givenInactiveAndUnapprovedUser(email);

        whenFindingByEmail(email, entity);
        whenActivatingUser(email);

        thenUserShouldBeActive(entity);
        thenUserBrandShouldBeApproved(entity);
        verify(userJpaRepository).findByEmail(email);
        verify(userJpaRepository).save(entity);
    }

    @Test
    void shouldThrowExceptionWhenActivatingNonExistingUser() {
        String email = "test17@mail.com";

        whenFindingByEmail(email, null);

        thenActivateShouldThrowException(email);
        verify(userJpaRepository).findByEmail(email);
        verify(userJpaRepository, never()).save(any());
    }

    @Test
    void shouldConvertUserToAdminWhenEmailExists() {
        String email = "test@mail.com";
        UserEntity entity = givenUserWithRole(email, Role.USER);

        whenFindingByEmail(email, entity);
        whenConvertingToAdmin(email);

        thenRoleShouldBe(entity, Role.ADMIN);
        verify(userJpaRepository).save(entity);
    }

    @Test
    void shouldConvertAdminToUserWhenEmailExists() {
        String email = "test@mail.com";
        UserEntity entity = givenUserWithRole(email, Role.ADMIN);

        whenFindingByEmail(email, entity);
        whenConvertingToUser(email);

        thenRoleShouldBe(entity, Role.USER);
        verify(userJpaRepository).save(entity);
    }

    @Test
    void shouldUpdateBrandUserCorrectly() {
        String email = "user@mail.com";
        String brandCode = "TESTBRAND";
        UserEntity user = givenUser(email);
        MarcaEntity brand = givenBrand(brandCode);

        whenFindingUserByEmail(email, user);
        whenFindingBrandByCode(brandCode, brand);
        whenSavingUser(user);

        whenUpdateBrandUser(email, brandCode);

        thenUserShouldHaveBrand(user, brand);
        thenUserShouldHaveRole(user, Role.BRAND);
        thenUserShouldBeUnapproved(user);
        verify(userJpaRepository).save(user);
    }

    @Test
    void shouldReturnEmailWhenBrandUserExists() {
        String brandCode = "TESTBRAND";
        UserEntity user = givenUser("brand@mail.com");

        whenFindingUserByBrandCode(brandCode, user);

        String email = whenGetEmailUserRelated(brandCode);

        thenEmailShouldBe(email, "brand@mail.com");
        verify(userJpaRepository).findByBrand_CodigoMarca(brandCode);
    }

    @Test
    void shouldUpdateUserCorrectlyWhenValidDataProvided() {
        UserEntity entity = givenUserWithFullData();

        whenFindingUserByEmail("old@mail.com", entity);
        whenSavingUser(entity);

        UserModel result = whenUpdateUser(
                "old@mail.com",
                "newName",
                "newLName",
                "new@mail.com",
                "newpass",
                "new.png"
        );

        thenUserShouldHaveUpdatedFields(entity, "newName", "newLName", "new@mail.com", "newpass", "new.png");
        thenEmailShouldBe(result.getEmail(), "new@mail.com");
        verify(userJpaRepository).save(entity);
    }

    @Test
    void shouldNotModifyPasswordWhenEmptyPasswordProvided() {
        UserEntity entity = givenUserWithOldPassword();

        whenFindingUserByEmail("old@mail.com", entity);
        whenSavingUser(entity);

        whenUpdateUser(
                "old@mail.com",
                "newName",
                "newLNAme",
                "new@mail.com",
                "",
                "image.png"
        );

        thenPasswordShouldBe(entity, "oldpass");
        thenEmailShouldBe(entity.getEmail(), "new@mail.com");
    }

    private void givenExistingUserWithEmail(String email) {
        UserEntity entity = new UserEntity();
        entity.setEmail(email);
        when(userJpaRepository.findByEmail(email)).thenReturn(entity);
    }

    private void givenUserDoesNotExist(String email) {
        when(userJpaRepository.findByEmail(email)).thenReturn(null);
    }

    private void givenUserWithVerificationToken(String token, String email, Long id) {
        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setEmail(email);
        entity.setVerificationToken(token);

        when(userJpaRepository.findByVerificationToken(token)).thenReturn(entity);
    }

    private void givenNoUserWithToken(String token) {
        when(userJpaRepository.findByVerificationToken(token)).thenReturn(null);
    }

    private void givenUserWithId(Long id, String email) {
        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setEmail(email);

        when(userJpaRepository.findById(id)).thenReturn(Optional.of(entity));
    }

    private void givenNoUserWithId(Long id) {
        when(userJpaRepository.findById(id)).thenReturn(Optional.empty());
    }

    private UserModel whenFindUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    private UserModel whenFindUserByVerificationToken(String token) {
        return userRepository.findUserByVerificationToken(token);
    }

    private UserModel whenFindById(Long id) {
        return userRepository.findById(id);
    }


    private void thenReturnUserWithEmail(UserModel result, String expectedEmail) {
        assertNotNull(result);
        assertEquals(expectedEmail, result.getEmail());
    }

    private void thenExpectUserNotFound(String email) {
        assertThrows(
                UserNotFoundException.class,
                () -> userRepository.findUserByEmail(email)
        );
    }

    private void thenUserShouldHaveEmail(UserModel result, String expectedEmail) {
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(expectedEmail);
    }

    private void thenUserShouldHaveId(UserModel result, Long expectedId) {
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(expectedId);
    }

    private void thenExpectUserNotFoundByToken(String token) {
        assertThatThrownBy(() -> userRepository.findUserByVerificationToken(token))
                .isInstanceOf(UserNotFoundException.class);
    }

    private void thenExpectUserNotFoundById(Long id) {
        assertThatThrownBy(() -> userRepository.findById(id))
                .isInstanceOf(UserNotFoundException.class);
    }

    private UserModel givenUserModel(String email, String name) {
        UserModel m = new UserModel();
        m.setEmail(email);
        m.setName(name);
        return m;
    }

    private UserEntity givenSavedEntity(UserModel model, Long id) {
        UserEntity e = new UserEntity(model);
        e.setId(id);
        return e;
    }

    private List<UserEntity> givenUserEntityList() {
        UserEntity u1 = new UserEntity();
        u1.setId(1L);
        u1.setEmail("u1@mail.com");

        UserEntity u2 = new UserEntity();
        u2.setId(2L);
        u2.setEmail("u2@mail.com");

        return List.of(u1, u2);
    }

    private UserEntity givenActiveUserEntity(String email) {
        UserEntity e = new UserEntity();
        e.setEmail(email);
        e.setStatus(true);
        return e;
    }

    private void whenSavingUserEntity(UserEntity saved) {
        when(userJpaRepository.save(any(UserEntity.class))).thenReturn(saved);
    }

    private UserModel whenSavingUser(UserModel model) {
        return userRepository.saveUser(model);
    }

    private void whenFindingAllUsers(List<UserEntity> entities) {
        when(userJpaRepository.findAll()).thenReturn(entities);
    }

    private List<UserModel> whenCallingFindAll() {
        return userRepository.findAll();
    }

    private void whenFindingByEmail(String email, UserEntity entity) {
        when(userJpaRepository.findByEmail(email)).thenReturn(entity);
    }

    private void whenDeactivatingUser(String email) {
        userRepository.desactivateUser(email);
    }

    private void thenUserEmailShouldBe(UserModel result, String expected) {
        assertThat(result.getEmail()).isEqualTo(expected);
    }

    private void thenUserIdShouldBeNull(UserModel result) {
        assertThat(result.getId()).isNull();
    }

    private void thenResultShouldHaveSize(List<UserModel> list, int size) {
        assertThat(list).hasSize(size);
    }

    private void thenUserShouldBeDeactivated(UserEntity entity) {
        assertThat(entity.isStatus()).isFalse();
    }

    private void thenExceptionShouldBeThrown(String email) {
        assertThatThrownBy(() -> userRepository.findUserByEmail(email))
                .isInstanceOf(UserNotFoundException.class);
    }


    private UserEntity givenInactiveAndUnapprovedUser(String email) {
        UserEntity e = new UserEntity();
        e.setEmail(email);
        e.setStatus(false);
        e.setBrandApproved(false);
        return e;
    }

    private UserEntity givenUserWithRole(String email, Role role) {
        UserEntity e = new UserEntity();
        e.setEmail(email);
        e.setRole(role);
        return e;
    }

    private void whenActivatingUser(String email) {
        userRepository.activateUser(email);
    }

    private void whenConvertingToAdmin(String email) {
        userRepository.convertToAdmin(email);
    }

    private void whenConvertingToUser(String email) {
        userRepository.convertToUser(email);
    }

    private void thenUserShouldBeActive(UserEntity entity) {
        assertThat(entity.isStatus()).isTrue();
    }

    private void thenUserBrandShouldBeApproved(UserEntity entity) {
        assertThat(entity.isBrandApproved()).isTrue();
    }

    private void thenDesactivateShouldThrowException(String email) {
        assertThatThrownBy(() -> userRepository.desactivateUser(email))
                .isInstanceOf(UserNotFoundException.class);
    }
    private void thenActivateShouldThrowException(String email) {
        assertThatThrownBy(() -> userRepository.activateUser(email))
                .isInstanceOf(UserNotFoundException.class);
    }

    private void thenRoleShouldBe(UserEntity entity, Role expected) {
        assertThat(entity.getRole()).isEqualTo(expected);
    }

    private UserEntity givenUser(String email) {
        UserEntity u = new UserEntity();
        u.setEmail(email);
        return u;
    }

    private MarcaEntity givenBrand(String code) {
        MarcaEntity b = new MarcaEntity();
        b.setCodigoMarca(code);
        return b;
    }

    private UserEntity givenUserWithFullData() {
        UserEntity e = new UserEntity();
        e.setEmail("old@mail.com");
        e.setPassword("oldpass");
        e.setUserImageUrl("old.png");
        return e;
    }

    private UserEntity givenUserWithOldPassword() {
        UserEntity e = new UserEntity();
        e.setEmail("old@mail.com");
        e.setPassword("oldpass");
        return e;
    }

    private void whenFindingUserByEmail(String email, UserEntity user) {
        when(userJpaRepository.findByEmail(email)).thenReturn(user);
    }

    private void whenFindingBrandByCode(String code, MarcaEntity brand) {
        when(brandJpaRepository.findByCodigoMarca(code)).thenReturn(brand);
    }

    private void whenFindingUserByBrandCode(String code, UserEntity user) {
        when(userJpaRepository.findByBrand_CodigoMarca(code)).thenReturn(user);
    }

    private void whenSavingUser(UserEntity user) {
        when(userJpaRepository.save(user)).thenReturn(user);
    }

    private void whenUpdateBrandUser(String email, String code) {
        userRepository.updateBrandUser(email, code);
    }

    private String whenGetEmailUserRelated(String brandCode) {
        return userRepository.getEmailUserRelatedToBrandByBrandCode(brandCode);
    }

    private UserModel whenUpdateUser(String email, String n, String ln, String newEmail, String pass, String img) {
        return userRepository.updateUser(email, n, ln, newEmail, pass, img);
    }

    private void thenUserShouldHaveBrand(UserEntity user, MarcaEntity brand) {
        assertThat(user.getBrand()).isEqualTo(brand);
    }

    private void thenUserShouldHaveRole(UserEntity user, Role role) {
        assertThat(user.getRole()).isEqualTo(role);
    }

    private void thenUserShouldBeUnapproved(UserEntity user) {
        assertThat(user.isBrandApproved()).isFalse();
    }

    private void thenEmailShouldBe(String actual, String expected) {
        assertThat(actual).isEqualTo(expected);
    }

    private void thenUserShouldHaveUpdatedFields(UserEntity e, String n, String ln, String email, String pass, String img) {
        assertThat(e.getName()).isEqualTo(n);
        assertThat(e.getLastName()).isEqualTo(ln);
        assertThat(e.getEmail()).isEqualTo(email);
        assertThat(e.getPassword()).isEqualTo(pass);
        assertThat(e.getUserImageUrl()).isEqualTo(img);
    }

    private void thenPasswordShouldBe(UserEntity e, String expected) {
        assertThat(e.getPassword()).isEqualTo(expected);
    }

}