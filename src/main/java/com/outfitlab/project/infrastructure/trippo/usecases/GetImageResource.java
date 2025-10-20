package com.outfitlab.project.infrastructure.trippo.usecases;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class GetImageResource {
    public ByteArrayResource getImageResource(MultipartFile image) throws IOException {
        return new ByteArrayResource(image.getBytes()) {
            @Override
            public String getFilename() {
                return image.getOriginalFilename();
            }
        };
    }
}
