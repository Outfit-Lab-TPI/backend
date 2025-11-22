package com.outfitlab.project.domain.interfaces.port;

import com.outfitlab.project.domain.model.dto.GeminiRecommendationDTO;

public interface GeminiClient {
    GeminiRecommendationDTO extractParameters(String peticionUsuario);
}
