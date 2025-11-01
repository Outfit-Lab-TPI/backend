package com.outfitlab.project.domain.useCases;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.interfaces.repositories.ITripoRepository;
import com.outfitlab.project.domain.useCases.tripo.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UploadImageToTripoTest {

    private ValidateExtension validateExtensionMock;
    private GetFileExtension getFileExtensionMock;
    private ITripoRepository tripoRepositoryMock;
    private UploadImageToTripo uploadImageToTripo;

    @Before
    public void setUp() {
        validateExtensionMock = mock(ValidateExtension.class);
        getFileExtensionMock = mock(GetFileExtension.class);
        tripoRepositoryMock = mock(ITripoRepository.class);
        uploadImageToTripo = new UploadImageToTripo(validateExtensionMock, getFileExtensionMock, tripoRepositoryMock);
    }

    @Test
    public void ejecutarDeberiaSubirImagenYDevolverMapa_cuandoArchivoEsValido() throws Exception, ErrorReadJsonException, ErrorUploadImageToTripo {
        String nombreArchivo = "imagen.png";
        byte[] contenido = {1, 2, 3};
        MultipartFile file = new MockMultipartFile("file", nombreArchivo, "image/png", contenido);

        when(getFileExtensionMock.execute(nombreArchivo)).thenReturn("png");
        when(validateExtensionMock.execute("png")).thenReturn(true);
        when(tripoRepositoryMock.peticionUploadImagenToTripo(any(ByteArrayResource.class))).thenReturn("token123");

        Map<String, String> resultado = null;
        try {
            resultado = uploadImageToTripo.execute(file);
        } catch (ErroBytesException | ErrorReadJsonException | ErrorUploadImageToTripo e) {
            fail("No se esperaba ninguna excepción: " + e.getMessage());
        }

        assertNotNull(resultado);
        assertEquals(nombreArchivo, resultado.get("originalFilename"));
        assertEquals("png", resultado.get("fileExtension"));
        assertEquals("token123", resultado.get("imageToken"));
    }

    @Test
    public void ejecutarDeberiaLanzarImageInvalidFormatException_cuandoExtensionNoEsValida() {
        String nombreArchivo = "imagen.bmp";
        MultipartFile file = new MockMultipartFile("file", nombreArchivo, "image/bmp", new byte[]{1,2,3});

        when(getFileExtensionMock.execute(nombreArchivo)).thenReturn("bmp");
        when(validateExtensionMock.execute("bmp")).thenReturn(false);

        try {
            uploadImageToTripo.execute(file);
            fail("Se esperaba ImageInvalidFormatException");
        } catch (ImageInvalidFormatException e) {
            assertEquals("Formato de imagen no válido. Solo se aceptan JPG, JPEG, PNG y WEBP.", e.getMessage());
        } catch (ErroBytesException | ErrorReadJsonException | ErrorUploadImageToTripo e) {
            fail("Se lanzó una excepción inesperada: " + e.getMessage());
        }
    }

    @Test
    public void ejecutarDeberiaLanzarErroBytesException_cuandoObtenerBytesFalla() throws IOException {
        String nombreArchivo = "imagen.png";
        MultipartFile file = mock(MultipartFile.class);

        when(file.getOriginalFilename()).thenReturn(nombreArchivo);
        when(getFileExtensionMock.execute(nombreArchivo)).thenReturn("png");
        when(validateExtensionMock.execute("png")).thenReturn(true);
        when(file.getBytes()).thenThrow(new IOException());

        try {
            uploadImageToTripo.execute(file);
            fail("Se esperaba ErroBytesException");
        } catch (ErroBytesException e) {
            assertEquals("Hubo un error al obtener los bytes de la imagen.", e.getMessage());
        } catch (ImageInvalidFormatException | ErrorReadJsonException | ErrorUploadImageToTripo e) {
            fail("Se lanzó una excepción inesperada: " + e.getMessage());
        }
    }

    @Test
    public void ejecutarDeberiaLanzarErrorUploadImageToTripo_cuandoRepositorioLanzaExcepcion() throws Exception, ErrorReadJsonException, ErrorUploadImageToTripo {
        String nombreArchivo = "imagen.png";
        byte[] contenido = {1, 2, 3};
        MultipartFile file = new MockMultipartFile("file", nombreArchivo, "image/png", contenido);

        when(getFileExtensionMock.execute(nombreArchivo)).thenReturn("png");
        when(validateExtensionMock.execute("png")).thenReturn(true);
        when(tripoRepositoryMock.peticionUploadImagenToTripo(any(ByteArrayResource.class)))
                .thenThrow(new ErrorUploadImageToTripo("Error subiendo imagen"));

        try {
            uploadImageToTripo.execute(file);
            fail("Se esperaba ErrorUploadImageToTripo");
        } catch (ErrorUploadImageToTripo e) {
            assertEquals("Error subiendo imagen", e.getMessage());
        } catch (ErroBytesException | ImageInvalidFormatException | ErrorReadJsonException e) {
            fail("Se lanzó una excepción inesperada: " + e.getMessage());
        }
    }

    @Test
    public void ejecutarDeberiaLanzarErrorReadJsonException_cuandoRepositorioLanzaErrorReadJsonException() throws Exception, ErrorReadJsonException, ErrorUploadImageToTripo {
        String nombreArchivo = "imagen.png";
        byte[] contenido = {1, 2, 3};
        MultipartFile file = new MockMultipartFile("file", nombreArchivo, "image/png", contenido);

        when(getFileExtensionMock.execute(nombreArchivo)).thenReturn("png");
        when(validateExtensionMock.execute("png")).thenReturn(true);
        when(tripoRepositoryMock.peticionUploadImagenToTripo(any(ByteArrayResource.class)))
                .thenThrow(new ErrorReadJsonException("Error leyendo JSON"));

        try {
            uploadImageToTripo.execute(file);
            fail("Se esperaba ErrorReadJsonException");
        } catch (ErrorReadJsonException e) {
            assertEquals("Error leyendo JSON", e.getMessage());
        } catch (ErroBytesException | ImageInvalidFormatException | ErrorUploadImageToTripo e) {
            fail("Se lanzó una excepción inesperada: " + e.getMessage());
        }
    }
}
