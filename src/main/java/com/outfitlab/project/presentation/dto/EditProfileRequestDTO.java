package com.outfitlab.project.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditProfileRequestDTO {
    private String name;
    private String lastname;
    private String email;
    private String password;
    private String confirmPassword;
    private MultipartFile userImg;

}
