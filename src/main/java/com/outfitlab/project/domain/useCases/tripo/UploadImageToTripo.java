package com.outfitlab.project.domain.useCases.tripo;

import com.outfitlab.project.domain.exceptions.ErroBytesException;
import com.outfitlab.project.domain.exceptions.ErrorReadJsonException;
import com.outfitlab.project.domain.exceptions.ErrorUploadImageToTripo;
import com.outfitlab.project.domain.exceptions.ImageInvalidFormatException;
import com.outfitlab.project.domain.interfaces.repositories.ITripoRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UploadImageToTripo {

    private final ValidateExtension validateExtension;
    private final GetFileExtension getFileExtension;
    private final ITripoRepository iTripoRepository;

    public UploadImageToTripo(ValidateExtension validateExtension, GetFileExtension getFileExtension, ITripoRepository iTripoRepository) {
        this.validateExtension = validateExtension;
        this.getFileExtension = getFileExtension;
        this.iTripoRepository = iTripoRepository;
    }

    public Map<String, String> execute(MultipartFile image) throws ErroBytesException, ErrorReadJsonException, ErrorUploadImageToTripo {
        String originalFilename = image.getOriginalFilename();
        String extension = this.getFileExtension.execute(originalFilename);

        if (!this.validateExtension.execute(extension)) {
            throw new ImageInvalidFormatException("Formato de imagen no válido. Solo se aceptan JPG, JPEG, PNG y WEBP.");
        }

        Map<String, String> uploadResult = new HashMap<>();
        uploadResult.put("originalFilename", originalFilename);
        uploadResult.put("fileExtension", extension);

        byte[] imageBytes;
        try{
            imageBytes = image.getBytes();
        }catch (IOException e){
            throw new ErroBytesException("Hubo un error al obtener los bytes de la imagen.");
        }

        ByteArrayResource imageResource = new ByteArrayResource(imageBytes) {
            @Override
            public String getFilename() {
                return originalFilename;
            }
        };

        uploadResult.put("imageToken", this.iTripoRepository.peticionUploadImagenToTripo(imageResource));
        return uploadResult;
    }
}
