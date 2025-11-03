package com.outfitlab.project.domain.useCases;

import com.outfitlab.project.domain.useCases.tripo.ValidateExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateExtensionTest {

    private ValidateExtension validateExtension;

    @BeforeEach
    public void setUp() {
        validateExtension = new ValidateExtension();
    }

    @Test
    public void ejecutarDeberiaDevolverTrue_cuandoExtensionEsJpg() {
        assertTrue(validateExtension.execute("jpg"));
    }

    @Test
    public void ejecutarDeberiaDevolverTrue_cuandoExtensionEsJpeg() {
        assertTrue(validateExtension.execute("jpeg"));
    }

    @Test
    public void ejecutarDeberiaDevolverTrue_cuandoExtensionEsPng() {
        assertTrue(validateExtension.execute("png"));
    }

    @Test
    public void ejecutarDeberiaDevolverTrue_cuandoExtensionEsWebp() {
        assertTrue(validateExtension.execute("webp"));
    }

    @Test
    public void ejecutarDeberiaDevolverFalse_cuandoExtensionNoEsValida() {
        assertFalse(validateExtension.execute("bmp"));
    }

    @Test
    public void ejecutarDeberiaDevolverFalse_cuandoExtensionEsVacia() {
        assertFalse(validateExtension.execute(""));
    }

    @Test
    public void ejecutarDeberiaDevolverFalse_cuandoExtensionEsNull() {
        assertFalse(validateExtension.execute(null));
    }
}
