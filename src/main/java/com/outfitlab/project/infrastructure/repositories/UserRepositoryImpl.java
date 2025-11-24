package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.domain.model.dto.UserWithBrandsDTO;
import com.outfitlab.project.infrastructure.model.MarcaEntity;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.BrandJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static com.outfitlab.project.domain.enums.Role.*; // ← Usar enums

public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final BrandJpaRepository brandJpaRepository;
    private final int PAGE_SIZE = 10;

    public UserRepositoryImpl(UserJpaRepository UserJpaRepository, BrandJpaRepository brandJpaRepository) {
        this.userJpaRepository = UserJpaRepository;
        this.brandJpaRepository = brandJpaRepository;
    }

    @Override
    public UserModel findUserByEmail(String userEmail) throws UserNotFoundException {
        UserEntity entity = getUserByEmail(userEmail);
        if (entity == null)
            throw new UserNotFoundException("No encontramos el usuario con el email: " + userEmail);
        return UserEntity.convertEntityToModel(entity);
    }

    @Override
    public UserModel findUserByVerificationToken(String token) throws UserNotFoundException {
        UserEntity entity = this.userJpaRepository.findByVerificationToken(token);
        if (entity == null)
            throw userNotFoundException();
        return UserEntity.convertEntityToModel(entity);
    }

    @Override
    public UserModel saveUser(UserModel userModel) {
        UserEntity entityToSave = new UserEntity(userModel);
        UserEntity savedEntity = userJpaRepository.save(entityToSave);
        return UserEntity.convertEntityToModel(savedEntity);
    }

    @Override
    public List<UserModel> findAll() {
        List<UserModel> users = this.userJpaRepository.findAll()
                .stream().map(UserEntity::convertEntityToModel)
                .toList();
        if (users.isEmpty())
            throw new UserNotFoundException("No encontramos usuarios.");
        return users;
    }

    @Override
    public void desactivateUser(String email) {
        UserEntity entity = getUserByEmail(email);
        checkifUserExistsOrThrowException(entity);

        entity.setStatus(false);
        this.userJpaRepository.save(entity);
    }

    @Override
    public void activateUser(String email) {
        UserEntity entity = getUserByEmail(email);
        checkifUserExistsOrThrowException(entity);

        entity.setStatus(true);
        entity.setBrandApproved(true);
        this.userJpaRepository.save(entity);
    }

    @Override
    public void convertToAdmin(String email) {
        UserEntity user = getUserByEmail(email);
        checkifUserExistsOrThrowException(user);

        user.setRole(ADMIN);
        this.userJpaRepository.save(user);
    }

    @Override
    public void convertToUser(String email) {
        UserEntity user = getUserByEmail(email);
        checkifUserExistsOrThrowException(user);

        user.setRole(USER);
        this.userJpaRepository.save(user);
    }

    @Override
    public void updateBrandUser(String userEmail, String brandCode) {
        UserEntity user = getUserByEmail(userEmail);
        checkifUserExistsOrThrowException(user);

        MarcaEntity brand = this.brandJpaRepository.findByCodigoMarca(brandCode);
        checkIfBrandExists(brand);

        user.setBrand(brand);
        user.setRole(BRAND); // la creo con rol Marca
        user.setBrandApproved(false); // NO está aprobada por un admin, la aprueban desde las notif.
        this.userJpaRepository.save(user);
    }

    // ← Métodos de develop
    @Override
    public String getEmailUserRelatedToBrandByBrandCode(String brandCode) {
        UserEntity user = this.userJpaRepository.findByBrand_CodigoMarca(brandCode);
        checkifUserExistsOrThrowException(user);
        return user.getEmail();
    }

    @Override
    public UserModel updateUser(String oldUserEmail, String name, String lastname, String newEmail, String password,
            String newImageUrl) {
        UserEntity entity = this.userJpaRepository.findByEmail(oldUserEmail);
        checkifUserExistsOrThrowException(entity);
        entity.setName(name);
        entity.setLastName(lastname);
        entity.setEmail(newEmail);

        if (!password.isEmpty())
            entity.setPassword(password);
        if (!newImageUrl.isEmpty())
            entity.setUserImageUrl(newImageUrl);

        return UserEntity.convertEntityToModel(this.userJpaRepository.save(entity));
    }

    @Override
    public Page<UserWithBrandsDTO> getAllBrandsWithUserRelated(int page) {
        Page<UserEntity> records = this.userJpaRepository.findAllByRole(BRAND, PageRequest.of(page, PAGE_SIZE));
        if (records == null)
            throw new BrandsNotFoundException("No hay Usuarios con marcas relacionadas.");
        return records.map(UserEntity::convertEntityToModelWithBrand);
    }

    @Override
    public List<UserModel> findAllWithRoleUserAndAdmin() {
        List<UserModel> users = this.userJpaRepository.findAllByRoleIn(List.of(ADMIN, USER))
                .stream().map(UserEntity::convertEntityUserOrAdminToModel)
                .toList();
        if (users.isEmpty())
            throw new UserNotFoundException("No encontramos usuarios.");
        return users;
    }

    @Override
    public List<UserWithBrandsDTO> getNotApprovedBrands() {
        List<UserEntity> records = this.userJpaRepository.findAllByRoleAndBrandApprovedFalse(BRAND);
        if (records == null || records.isEmpty()) {
            throw new BrandsNotFoundException("Todas las marcas ya están aprobadas.");
        }
        return records.stream()
                .map(UserEntity::convertEntityToModelWithBrand)
                .toList();
    }

    private static void checkIfBrandExists(MarcaEntity brand) {
        if (brand == null)
            throw new BrandsNotFoundException("No encontramos la marca para relacionarla al usuario.");
    }

    private void checkifUserExistsOrThrowException(UserEntity user) {
        if (user == null)
            throw userNotFoundException();
    }

    private UserEntity getUserByEmail(String email) {
        return this.userJpaRepository.findByEmail(email);
    }

    private UserNotFoundException userNotFoundException() {
        return new UserNotFoundException("No encontramos el usuario.");
    }
}
