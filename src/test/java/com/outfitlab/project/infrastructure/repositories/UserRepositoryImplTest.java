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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    void givenExistingEmailWhenFindUserByEmailThenReturnUserModel() {
        // ---------- GIVEN ---------
        String email = "test@mail.com";

        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setEmail(email);
        entity.setName("Juan");
        entity.setLastName("Lopez");

        when(userJpaRepository.findByEmail(email)).thenReturn(entity);

        // ---------- WHEN --------
        UserModel result = userRepository.findUserByEmail(email);

        // ---------- THEN --------
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getId()).isEqualTo(1L);
        verify(userJpaRepository).findByEmail(email);
    }

    @Test
    void givenValidTokenWhenFindUserByVerificationTokenThenReturnUserModel() {
        String token = "abc123";

        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setEmail("test@mail.com");
        entity.setVerificationToken(token);

        when(userJpaRepository.findByVerificationToken(token)).thenReturn(entity);

        UserModel result = userRepository.findUserByVerificationToken(token);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test@mail.com");
        verify(userJpaRepository).findByVerificationToken(token);
    }

    @Test
    void givenInvalidTokenWhenFindUserByVerificationTokenThenThrowException() {
        String token = "def456";
        when(userJpaRepository.findByVerificationToken(token)).thenReturn(null);

        assertThatThrownBy(() -> userRepository.findUserByVerificationToken(token))
                .isInstanceOf(UserNotFoundException.class);

        verify(userJpaRepository).findByVerificationToken(token);
    }

    @Test
    void givenExistingIdWhenFindByIdThenReturnUserModel() {
        Long id = 1L;

        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setEmail("test@mail.com");

        when(userJpaRepository.findById(id)).thenReturn(Optional.of(entity));

        UserModel result = userRepository.findById(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getEmail()).isEqualTo("test@mail.com");
        verify(userJpaRepository).findById(id);
    }

    @Test
    void givenNonExistingIdWhenFindByIdThenThrowException() {
        Long id = 99L;
        when(userJpaRepository.findById(id)).thenReturn(Optional.empty());


        assertThatThrownBy(() -> userRepository.findById(id))
                .isInstanceOf(UserNotFoundException.class);

        verify(userJpaRepository).findById(id);
    }

    @Test
    void givenUserModelWhenSaveUserThenPersistAndReturnUserModel() {
        UserModel model = new UserModel();
        model.setEmail("save@mail.com");
        model.setName("Juan");

        UserEntity savedEntity = new UserEntity(model);
        savedEntity.setId(1L);

        when(userJpaRepository.save(any(UserEntity.class)))
                .thenReturn(savedEntity);

        UserModel result = userRepository.saveUser(model);
        System.out.println(result);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("save@mail.com");
        assertThat(result.getId()).isNull();
        verify(userJpaRepository).save(any(UserEntity.class));
    }

    @Test
    void givenExistingUsersWhenFindAllThenReturnUserList() {
        List<UserEntity> entities = new ArrayList<>();

        UserEntity u1 = new UserEntity();
        u1.setId(1L);
        u1.setEmail("u1@mail.com");

        UserEntity u2 = new UserEntity();
        u2.setId(2L);
        u2.setEmail("u2@mail.com");

        entities.add(u1);
        entities.add(u2);

        when(userJpaRepository.findAll()).thenReturn(entities);

        List<UserModel> result = userRepository.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getEmail()).isEqualTo("u1@mail.com");
        assertThat(result.get(1).getEmail()).isEqualTo("u2@mail.com");
        verify(userJpaRepository).findAll();
    }

    @Test
    void givenExistingEmailWhenDesactivateUserThenStatusIsSetToFalse() {
        String email = "test@mail.com";

        UserEntity entity = new UserEntity();
        entity.setEmail(email);
        entity.setStatus(true);

        when(userJpaRepository.findByEmail(email)).thenReturn(entity);

        userRepository.desactivateUser(email);

        assertThat(entity.isStatus()).isFalse();
        verify(userJpaRepository).findByEmail(email);
        verify(userJpaRepository).save(entity);
    }

    @Test
    void givenNonExistingEmailWhenDesactivateUserThenThrowException() {
        String email = "test15@mail.com";
        when(userJpaRepository.findByEmail(email)).thenReturn(null);

        assertThatThrownBy(() -> userRepository.desactivateUser(email))
                .isInstanceOf(UserNotFoundException.class);

        verify(userJpaRepository).findByEmail(email);
        verify(userJpaRepository, never()).save(any());
    }

    @Test
    void givenExistingEmailWhenActivateUserThenStatusTrueAndBrandApprovedTrue() {
        String email = "test@mail.com";

        UserEntity entity = new UserEntity();
        entity.setEmail(email);
        entity.setStatus(false);
        entity.setBrandApproved(false);

        when(userJpaRepository.findByEmail(email)).thenReturn(entity);

        userRepository.activateUser(email);

        assertThat(entity.isStatus()).isTrue();
        assertThat(entity.isBrandApproved()).isTrue();
        verify(userJpaRepository).findByEmail(email);
        verify(userJpaRepository).save(entity);
    }

    @Test
    void givenNonExistingEmailWhenActivateUserThenThrowException() {
        String email = "test17@mail.com";
        when(userJpaRepository.findByEmail(email)).thenReturn(null);

        assertThatThrownBy(() -> userRepository.activateUser(email))
                .isInstanceOf(UserNotFoundException.class);

        verify(userJpaRepository).findByEmail(email);
        verify(userJpaRepository, never()).save(any());
    }

    @Test
    void givenExistingEmailWhenConvertToAdminThenRoleIsAdmin() {
        String email = "test@mail.com";

        UserEntity entity = new UserEntity();
        entity.setEmail(email);
        entity.setRole(Role.USER);

        when(userJpaRepository.findByEmail(email)).thenReturn(entity);

        userRepository.convertToAdmin(email);

        assertThat(entity.getRole()).isEqualTo(Role.ADMIN);
        verify(userJpaRepository).save(entity);
    }

    @Test
    void givenExistingEmailWhenConvertToUserThenRoleIsUser() {
        String email = "test@mail.com";

        UserEntity entity = new UserEntity();
        entity.setEmail(email);
        entity.setRole(Role.ADMIN);

        when(userJpaRepository.findByEmail(email)).thenReturn(entity);

        userRepository.convertToUser(email);

        assertThat(entity.getRole()).isEqualTo(Role.USER);
        verify(userJpaRepository).save(entity);
    }

    @Test
    void givenValidUserAndBrandWHENUpdateBrandUserTHENUserIsUpdatedCorrectly() {
        String email = "user@mail.com";
        String brandCode = "TESTBRAND";

        UserEntity user = new UserEntity();
        user.setEmail(email);

        MarcaEntity brand = new MarcaEntity();
        brand.setCodigoMarca(brandCode);

        when(userJpaRepository.findByEmail(email)).thenReturn(user);
        when(brandJpaRepository.findByCodigoMarca(brandCode)).thenReturn(brand);
        when(userJpaRepository.save(user)).thenReturn(user);

        userRepository.updateBrandUser(email, brandCode);

        assertThat(user.getBrand()).isEqualTo(brand);
        assertThat(user.getRole()).isEqualTo(Role.BRAND);
        assertThat(user.isBrandApproved()).isFalse();
        verify(userJpaRepository).save(user);
    }

    @Test
    void givenBrandCodeWhenGetEmailUserRelatedThenReturnEmail() {
        String brandCode = "TESTBRAND";

        UserEntity user = new UserEntity();
        user.setEmail("brand@mail.com");

        when(userJpaRepository.findByBrand_CodigoMarca(brandCode)).thenReturn(user);

        String email = userRepository.getEmailUserRelatedToBrandByBrandCode(brandCode);

        assertThat(email).isEqualTo("brand@mail.com");
        verify(userJpaRepository).findByBrand_CodigoMarca(brandCode);
    }

    @Test
    void givenUserDataWhenUpdateUserThenPersistChangesAndReturnModel() {
        UserEntity entity = new UserEntity();
        entity.setEmail("old@mail.com");
        entity.setPassword("oldpass");
        entity.setUserImageUrl("old.png");

        when(userJpaRepository.findByEmail("old@mail.com")).thenReturn(entity);
        when(userJpaRepository.save(entity)).thenReturn(entity);

        UserModel result = userRepository.updateUser(
                "old@mail.com",
                "newName",
                "newLName",
                "new@mail.com",
                "newpass",
                "new.png"
        );

        assertThat(entity.getName()).isEqualTo("newName");
        assertThat(entity.getLastName()).isEqualTo("newLName");
        assertThat(entity.getEmail()).isEqualTo("new@mail.com");
        assertThat(entity.getPassword()).isEqualTo("newpass");
        assertThat(entity.getUserImageUrl()).isEqualTo("new.png");

        assertThat(result.getEmail()).isEqualTo("new@mail.com");
        verify(userJpaRepository).save(entity);
    }

    @Test
    void givenEmptyPasswordWhenUpdateUserThenPasswordIsNotModified() {
        UserEntity entity = new UserEntity();
        entity.setEmail("old@mail.com");
        entity.setPassword("oldpass");

        when(userJpaRepository.findByEmail("old@mail.com")).thenReturn(entity);
        when(userJpaRepository.save(entity)).thenReturn(entity);

        userRepository.updateUser(
                "old@mail.com",
                "newName",
                "newLNAme",
                "new@mail.com",
                "",
                "image.png"
        );

        assertThat(entity.getPassword()).isEqualTo("oldpass");
        assertThat(entity.getEmail()).isEqualTo("new@mail.com");
    }
}