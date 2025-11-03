package com.outfitlab.project.domain.useCases.tripo;

public class ValidateExtension {

    public boolean execute(String extension) {
        if (extension == null || extension.equals("")) return false;
        return extension.equals("jpg") ||
                extension.equals("jpeg") ||
                extension.equals("png") ||
                extension.equals("webp");
    }
}
