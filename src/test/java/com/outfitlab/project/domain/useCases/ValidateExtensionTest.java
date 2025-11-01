package com.outfitlab.project.domain.useCases;

import com.outfitlab.project.domain.useCases.tripo.ValidateExtension;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ValidateExtensionTest {

    private ValidateExtension validateExtension;

    @Before
    public void setUp() {
        validateExtension = new ValidateExtension();
    }

    @Test
    public void ejecutarDeberiaDevolverTrue_cuandoExtensionEsJpg() {
        boolean resultado = validateExtension.execute("jpg");
        assertTrue(resultado);
    }

    @Test
    public void ejecutarDeberiaDevolverTrue_cuandoExtensionEsJpeg() {
        boolean resultado = validateExtension.execute("jpeg");
        assertTrue(resultado);
    }

    @Test
    public void ejecutarDeberiaDevolverTrue_cuandoExtensionEsPng() {
        boolean resultado = validateExtension.execute("png");
        assertTrue(resultado);
    }

    @Test
    public void ejecutarDeberiaDevolverTrue_cuandoExtensionEsWebp() {
        boolean resultado = validateExtension.execute("webp");
        assertTrue(resultado);
    }

    @Test
    public void ejecutarDeberiaDevolverFalse_cuandoExtensionNoEsValida() {
        boolean resultado = validateExtension.execute("bmp");
        assertFalse(resultado);
    }

    @Test
    public void ejecutarDeberiaDevolverFalse_cuandoExtensionEsVacia() {
        boolean resultado = validateExtension.execute("");
        assertFalse(resultado);
    }

    @Test
    public void ejecutarDeberiaDevolverFalse_cuandoExtensionEsNull() {
        boolean resultado = validateExtension.execute(null);
        assertFalse(resultado);
    }
}

