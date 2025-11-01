package com.outfitlab.project.domain.useCases;

import com.outfitlab.project.domain.exceptions.ErrorGenerateGlbException;
import com.outfitlab.project.domain.exceptions.ErrorReadJsonException;
import com.outfitlab.project.domain.interfaces.repositories.ITripoRepository;
import com.outfitlab.project.domain.useCases.tripo.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GenerateImageToModelTrippoTest {

    private ITripoRepository tripoRepositoryMock;
    private GenerateImageToModelTrippo generateImageToModelTrippo;

    @Before
    public void setUp() {
        tripoRepositoryMock = mock(ITripoRepository.class);
        generateImageToModelTrippo = new GenerateImageToModelTrippo(tripoRepositoryMock);
    }

    @Test
    public void ejecutarDeberiaDevolverRutaImagen_cuandoRepositorioGeneraImagenCorrectamente() throws ErrorGenerateGlbException, ErrorReadJsonException {
        Map<String, String> uploadData = new HashMap<>();
        uploadData.put("fileName", "modelo.glb");
        String rutaMock = "/ruta/a/modelo.glb";

        when(tripoRepositoryMock.peticionGenerateGlbToTripo(uploadData)).thenReturn(rutaMock);

        String resultado = generateImageToModelTrippo.execute(uploadData);

        assertNotNull(resultado);
        assertEquals("/ruta/a/modelo.glb", resultado);

        verify(tripoRepositoryMock, times(1)).peticionGenerateGlbToTripo(uploadData);
    }

    @Test(expected = ErrorGenerateGlbException.class)
    public void ejecutarDeberiaLanzarErrorGenerateGlbException_cuandoRepositorioLanzaErrorGenerateGlbException() throws ErrorGenerateGlbException, ErrorReadJsonException {
        Map<String, String> uploadData = new HashMap<>();
        uploadData.put("fileName", "modelo.glb");

        when(tripoRepositoryMock.peticionGenerateGlbToTripo(uploadData)).thenThrow(new ErrorGenerateGlbException("Error generando GLB"));

        generateImageToModelTrippo.execute(uploadData);
    }

    @Test(expected = ErrorReadJsonException.class)
    public void ejecutarDeberiaLanzarErrorReadJsonException_cuandoRepositorioLanzaErrorReadJsonException() throws ErrorGenerateGlbException, ErrorReadJsonException {
        Map<String, String> uploadData = new HashMap<>();
        uploadData.put("fileName", "modelo.glb");

        when(tripoRepositoryMock.peticionGenerateGlbToTripo(uploadData)).thenThrow(new ErrorReadJsonException("Error leyendo JSON"));

        generateImageToModelTrippo.execute(uploadData);
    }
}

