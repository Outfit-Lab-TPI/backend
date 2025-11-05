package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.interfaces.repositories.UserFavoriteCombinationRepository;
import com.outfitlab.project.domain.model.UserFavoriteCombinationModel;
import com.outfitlab.project.infrastructure.model.GarmentRecomendationEntity;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.model.UserFavoriteCombinationEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.GarmentRecomendationJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserFavoriteCombinationJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del puerto UserFavoriteCombinationRepository.
 * Adaptador que usa Spring Data JPA para persistencia.
 */
public class UserFavoriteCombinationRepositoryImpl implements UserFavoriteCombinationRepository {

    private final UserFavoriteCombinationJpaRepository jpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final GarmentRecomendationJpaRepository garmentRecomendationJpaRepository;

    public UserFavoriteCombinationRepositoryImpl(UserFavoriteCombinationJpaRepository jpaRepository,
                                                   UserJpaRepository userJpaRepository,
                                                   GarmentRecomendationJpaRepository garmentRecomendationJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.garmentRecomendationJpaRepository = garmentRecomendationJpaRepository;
    }

    @Override
    public UserFavoriteCombinationModel save(UserFavoriteCombinationModel model) {
        // Obtener entidades relacionadas
        UserEntity userEntity = userJpaRepository.findById(model.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + model.getUserId()));
        
        GarmentRecomendationEntity garmentEntity = garmentRecomendationJpaRepository.findById(model.getGarmentRecomendationId())
                .orElseThrow(() -> new RuntimeException("Recomendación no encontrada: " + model.getGarmentRecomendationId()));

        // Crear entidad
        UserFavoriteCombinationEntity entity = new UserFavoriteCombinationEntity();
        entity.setUser(userEntity);
        entity.setGarmentRecomendation(garmentEntity);
        entity.setIsActive(model.getIsActive());

        // Guardar y convertir a modelo
        UserFavoriteCombinationEntity saved = jpaRepository.save(entity);
        return UserFavoriteCombinationEntity.toModel(saved);
    }

    @Override
    public List<UserFavoriteCombinationModel> findActiveByUserId(Long userId) {
        return jpaRepository.findByUserIdAndIsActiveTrue(userId)
                .stream()
                .map(UserFavoriteCombinationEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public int countActiveByUserId(Long userId) {
        return jpaRepository.countByUserIdAndIsActiveTrue(userId);
    }

    @Override
    public void deleteById(Long id) {
        // Soft delete: marcar como inactivo en lugar de borrar
        jpaRepository.findById(id).ifPresent(entity -> {
            entity.setIsActive(false);
            jpaRepository.save(entity);
        });
    }
}
