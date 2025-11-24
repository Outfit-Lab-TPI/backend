package com.outfitlab.project.presentation.dto;

public record RegisterAttemptRequest(
        String userEmail,
        String prendaSupCode,
        String prendaInfCode,
        String imageUrl
) {}
