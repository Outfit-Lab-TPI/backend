package com.outfitlab.project.domain.useCases;

import com.outfitlab.project.domain.useCases.tripo.GetImageResource;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.Assert.*;

public class GetImageResourceTest {

    private GetImageResource getImageResource;

    @Before
    public void setUp() {
        getImageResource = new GetImageResource();
    }

    @Test
    public void ejecutarDeberiaDevolverByteArrayResource_cuandoSePasaImagenValida() throws IOException {
        String nombreArchivo = "imagen.png";
        byte[] contenido = {1, 2, 3, 4, 5};
        MultipartFile file = new MockMultipartFile("file", nombreArchivo, "image/png", contenido);

        ByteArrayResource resource = getImageResource.execute(file);

        assertNotNull(resource);
        assertArrayEquals(contenido, resource.getByteArray());
        assertEquals(nombreArchivo, resource.getFilename());
    }

    @Test
    public void ejecutarDeberiaDevolverByteArrayResourceVacio_cuandoArchivoEstaVacio() throws IOException {
        String nombreArchivo = "vacio.png";
        byte[] contenido = {};
        MultipartFile file = new MockMultipartFile("file", nombreArchivo, "image/png", contenido);

        ByteArrayResource resource = getImageResource.execute(file);

        assertNotNull(resource);
        assertArrayEquals(contenido, resource.getByteArray());
        assertEquals(nombreArchivo, resource.getFilename());
    }
}

