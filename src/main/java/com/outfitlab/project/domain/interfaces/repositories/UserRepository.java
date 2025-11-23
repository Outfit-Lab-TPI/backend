package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.enums.Role;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.domain.model.dto.UserWithBrandsDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public interface UserRepository {
    UserModel findUserByEmail(String userEmail) throws UserNotFoundException;

    UserModel findUserByVerificationToken(String token) throws UserNotFoundException;

    UserModel saveUser(UserModel userModel);

    List<UserModel> findAll();

    void desactivateUser(String email);

    void activateUser(String email);

    void convertToAdmin(String email);

    void convertToUser(String email);

    void updateBrandUser(String userEmail, String brandCode);

    String getEmailUserRelatedToBrandByBrandCode(String brandCode);

    @Transactional
    UserModel updateUser(String oldUserEmail, String name, String lastname, String email, String password, String newImageUrl);

    Page<UserWithBrandsDTO> getAllBrandsWithUserRelated(int page);

    List<UserModel> findAllWithRoleUserAndAdmin();
}
