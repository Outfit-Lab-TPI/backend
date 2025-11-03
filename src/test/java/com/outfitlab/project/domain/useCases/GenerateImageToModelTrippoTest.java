package com.outfitlab.project.domain.useCases;

import com.outfitlab.project.domain.exceptions.ErrorGenerateGlbException;
import com.outfitlab.project.domain.exceptions.ErrorReadJsonException;
import com.outfitlab.project.domain.interfaces.repositories.TripoRepository;
import com.outfitlab.project.domain.useCases.tripo.GenerateImageToModelTrippo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GenerateImageToModelTrippoTest {

    private TripoRepository tripoRepositoryMock;
    private GenerateImageToModelTrippo generateImageToModelTrippo;

    @BeforeEach
    public void setUp() {
        tripoRepositoryMock = mock(TripoRepository.class);
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

    @Test
    public void ejecutarDeberiaLanzarErrorGenerateGlbException_cuandoRepositorioLanzaErrorGenerateGlbException() throws ErrorGenerateGlbException, ErrorReadJsonException {
        Map<String, String> uploadData = new HashMap<>();
        uploadData.put("fileName", "modelo.glb");

        when(tripoRepositoryMock.peticionGenerateGlbToTripo(uploadData))
                .thenThrow(new ErrorGenerateGlbException("Error generando GLB"));

        assertThrows(ErrorGenerateGlbException.class, () -> {
            generateImageToModelTrippo.execute(uploadData);
        });
    }

    @Test
    public void ejecutarDeberiaLanzarErrorReadJsonException_cuandoRepositorioLanzaErrorReadJsonException() throws ErrorGenerateGlbException, ErrorReadJsonException {
        Map<String, String> uploadData = new HashMap<>();
        uploadData.put("fileName", "modelo.glb");

        when(tripoRepositoryMock.peticionGenerateGlbToTripo(uploadData))
                .thenThrow(new ErrorReadJsonException("Error leyendo JSON"));

        assertThrows(ErrorReadJsonException.class, () -> {
            generateImageToModelTrippo.execute(uploadData);
        });
    }
}
