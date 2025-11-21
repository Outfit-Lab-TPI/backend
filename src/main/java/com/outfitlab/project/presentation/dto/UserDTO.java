package com.outfitlab.project.presentation.dto;

import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.infrastructure.config.security.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
        private String name;
        private String lastName;
        private String email;
        private Role role;
        private boolean verified;
        private boolean status;

        public static UserDTO convertToDTO(UserModel model) {
            return new UserDTO(
                    model.getName(),
                    model.getLastName(),
                    model.getEmail(),
                    model.getRole(),
                    model.isVerified(),
                    model.isStatus()
            );
        }
}
