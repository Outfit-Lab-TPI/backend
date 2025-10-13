package com.outfitlab.project.domain.exceptions;

public class MarcasNotFoundException extends Throwable {
    public MarcasNotFoundException(String noEncontramosMarcas) {
        super(noEncontramosMarcas);
    }
}
