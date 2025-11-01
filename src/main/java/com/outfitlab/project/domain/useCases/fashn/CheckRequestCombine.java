package com.outfitlab.project.domain.useCases.fashn;

import com.outfitlab.project.domain.exceptions.FashnApiException;

public class CheckRequestCombine {

    public void execute(String superior, String inferior) throws FashnApiException {
        if ((superior == null || superior.isBlank()) && (inferior == null || inferior.isBlank())) {
            throw new FashnApiException("Debe proporcionarse al menos una prenda (superior o inferior).");
        }
    }

}
