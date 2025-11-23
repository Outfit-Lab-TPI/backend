package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.domain.enums.Role;
import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.infrastructure.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String userEmail);

    Optional<UserEntity> getByEmail(String userEmail);

    UserEntity findByVerificationToken(String token);

    UserEntity findByBrand_CodigoMarca(String brandCode);

    Page<UserEntity> findAllByRole(Role role, Pageable pageable);

    List<UserEntity> findAllByRoleIn(List<Role> role);
}
