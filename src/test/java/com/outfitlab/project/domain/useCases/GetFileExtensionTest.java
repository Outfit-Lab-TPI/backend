package com.outfitlab.project.domain.useCases;

import com.outfitlab.project.domain.useCases.tripo.GetFileExtension;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GetFileExtensionTest {

    private GetFileExtension getFileExtension;

    @Before
    public void setUp() {
        getFileExtension = new GetFileExtension();
    }

    @Test
    public void ejecutarDeberiaDevolverExtension_cuandoArchivoTieneExtension() {
        String nombreArchivo = "modelo.glb";
        String resultado = getFileExtension.execute(nombreArchivo);
        assertEquals("glb", resultado);
    }

    @Test
    public void ejecutarDeberiaDevolverExtensionMinuscula_cuandoArchivoTieneExtensionEnMayuscula() {
        String nombreArchivo = "MODELO.GLB";
        String resultado = getFileExtension.execute(nombreArchivo);
        assertEquals("glb", resultado);
    }

    @Test
    public void ejecutarDeberiaDevolverVacio_cuandoArchivoNoTieneExtension() {
        String nombreArchivo = "modelo";
        String resultado = getFileExtension.execute(nombreArchivo);
        assertEquals("", resultado);
    }

    @Test
    public void ejecutarDeberiaDevolverVacio_cuandoArchivoEsNull() {
        String resultado = getFileExtension.execute(null);
        assertEquals("", resultado);
    }

    @Test
    public void ejecutarDeberiaDevolverVacio_cuandoArchivoEsVacio() {
        String resultado = getFileExtension.execute("");
        assertEquals("", resultado);
    }

    @Test
    public void ejecutarDeberiaDevolverVacio_cuandoArchivoTerminaConPunto() {
        String nombreArchivo = "archivo.";
        String resultado = getFileExtension.execute(nombreArchivo);
        assertEquals("", resultado);
    }
}
