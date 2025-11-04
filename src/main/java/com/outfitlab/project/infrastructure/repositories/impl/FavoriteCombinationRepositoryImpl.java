package com.outfitlab.project.infrastructure.repositories.impl;

import com.outfitlab.project.domain.interfaces.repositories.UserFavoriteCombinationRepository;
import com.outfitlab.project.domain.model.GarmentRecomendationModel;
import com.outfitlab.project.domain.model.UserFavoriteCombinationModel;
import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.infrastructure.model.GarmentRecomendationEntity;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.model.UserFavoriteCombinationEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserFavoriteCombinationJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class FavoriteCombinationRepositoryImpl implements UserFavoriteCombinationRepository {

    private final UserFavoriteCombinationJpaRepository jpaRepository;

    public FavoriteCombinationRepositoryImpl(UserFavoriteCombinationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public UserFavoriteCombinationModel save(UserFavoriteCombinationModel favoriteCombinationModel) {
        UserFavoriteCombinationEntity entity = toEntity(favoriteCombinationModel);
        UserFavoriteCombinationEntity savedEntity = jpaRepository.save(entity);
        return toModel(savedEntity);
    }

    @Override
    public List<UserFavoriteCombinationModel> findByUserIdAndIsActiveTrue(Long userId) {
        return jpaRepository.findByUserIdAndIsActiveTrue(userId).stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public int countByUserIdAndIsActiveTrue(Long userId) {
        return jpaRepository.countByUserIdAndIsActiveTrue(userId);
    }

    @Override
    public void deleteById(Long id) {
        UserFavoriteCombinationEntity entity = jpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Favorite combination not found with id: " + id));
        entity.setIsActive(false);
        jpaRepository.save(entity);
    }

    private UserFavoriteCombinationModel toModel(UserFavoriteCombinationEntity entity) {
        UserModel userModel = new UserModel(
                entity.getUser().getId(),
                entity.getUser().getSatulation(),
                entity.getUser().getName(),
                entity.getUser().getSecondName(),
                entity.getUser().getLastName(),
                entity.getUser().getYears(),
                entity.getUser().getEmail(),
                entity.getUser().getSubscriptionStatus(),
                entity.getUser().getSubscriptionExpiresAt()
        );

        GarmentRecomendationModel garmentModel = new GarmentRecomendationModel();
        garmentModel.setId(entity.getGarmentRecomendation().getId());

        UserFavoriteCombinationModel model = new UserFavoriteCombinationModel();
        model.setId(entity.getId());
        model.setUser(userModel);
        model.setGarmentRecomendation(garmentModel);
        model.setCreatedAt(entity.getCreatedAt());
        model.setIsActive(entity.getIsActive());
        
        return model;
    }

    private UserFavoriteCombinationEntity toEntity(UserFavoriteCombinationModel model) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(model.getUser().getId());
        // Set other user properties if necessary for saving

        GarmentRecomendationEntity garmentEntity = new GarmentRecomendationEntity(); // Placeholder, adjust as needed
        garmentEntity.setId(model.getGarmentRecomendation().getId());
        // Set other garment properties if necessary for saving

        UserFavoriteCombinationEntity entity = new UserFavoriteCombinationEntity();
        entity.setId(model.getId());
        entity.setUser(userEntity);
        entity.setGarmentRecomendation(garmentEntity);
        entity.setCreatedAt(model.getCreatedAt());
        entity.setIsActive(model.getIsActive());
        return entity;
    }
}
