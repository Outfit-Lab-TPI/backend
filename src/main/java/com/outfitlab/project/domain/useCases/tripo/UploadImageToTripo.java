package com.outfitlab.project.domain.useCases.tripo;

import com.outfitlab.project.domain.exceptions.ErroBytesException;
import com.outfitlab.project.domain.exceptions.ErrorReadJsonException;
import com.outfitlab.project.domain.exceptions.ErrorUploadImageToTripoException;
import com.outfitlab.project.domain.exceptions.FileEmptyException;
import com.outfitlab.project.domain.interfaces.repositories.TripoRepository;
import java.util.Map;

public class UploadImageToTripo {

    private final TripoRepository iTripoRepository;

    public UploadImageToTripo(TripoRepository iTripoRepository) {
        this.iTripoRepository = iTripoRepository;
    }

    public Map<String, Object> execute(String url) throws ErroBytesException, ErrorReadJsonException, ErrorUploadImageToTripoException, FileEmptyException {
        return this.iTripoRepository.requestUploadImagenToTripo(url);
    }
}
