package com.outfitlab.project.presentation.dto;

import com.outfitlab.project.domain.model.UserFavoriteCombinationModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FavoriteResponse {

    private Long id;
    private Long userId;
    private Long garmentRecomendationId;
    private LocalDateTime createdAt;

    public static FavoriteResponse fromModel(UserFavoriteCombinationModel model) {
        FavoriteResponse response = new FavoriteResponse();
        response.setId(model.getId());
        response.setUserId(model.getUser().getId());
        response.setGarmentRecomendationId(model.getGarmentRecomendation().getId());
        response.setCreatedAt(model.getCreatedAt());
        return response;
    }
}