package com.outfitlab.project.infrastructure.config.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.outfitlab.project.domain.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    @JsonProperty("access_token")
    private String access_token;
    @JsonProperty("refresh_token")
    private String refresh_token;
    @JsonProperty("user")
    private UserModel user;
}
