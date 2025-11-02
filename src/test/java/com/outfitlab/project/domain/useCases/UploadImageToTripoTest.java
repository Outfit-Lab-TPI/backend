package com.outfitlab.project.domain.useCases;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.interfaces.repositories.TripoRepository;
import com.outfitlab.project.domain.useCases.tripo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UploadImageToTripoTest {

    private ValidateExtension validateExtensionMock;
    private GetFileExtension getFileExtensionMock;
    private TripoRepository tripoRepositoryMock;
    private UploadImageToTripo uploadImageToTripo;

    @BeforeEach
    public void setUp() {
        validateExtensionMock = mock(ValidateExtension.class);
        getFileExtensionMock = mock(GetFileExtension.class);
        tripoRepositoryMock = mock(TripoRepository.class);
        uploadImageToTripo = new UploadImageToTripo(validateExtensionMock, getFileExtensionMock, tripoRepositoryMock);
    }

    @Test
    public void ejecutarDeberiaSubirImagenYDevolverMapa_cuandoArchivoEsValido() throws Exception, ErrorReadJsonException, ErrorUploadImageToTripo, ErroBytesException {
        String nombreArchivo = "imagen.png";
        byte[] contenido = {1, 2, 3};
        MultipartFile file = new MockMultipartFile("file", nombreArchivo, "image/png", contenido);

        when(getFileExtensionMock.execute(nombreArchivo)).thenReturn("png");
        when(validateExtensionMock.execute("png")).thenReturn(true);
        when(tripoRepositoryMock.peticionUploadImagenToTripo(any(ByteArrayResource.class))).thenReturn("token123");

        Map<String, String> resultado = uploadImageToTripo.execute(file);

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

        ImageInvalidFormatException exception = assertThrows(ImageInvalidFormatException.class, () -> {
            uploadImageToTripo.execute(file);
        });

        assertEquals("Formato de imagen no válido. Solo se aceptan JPG, JPEG, PNG y WEBP.", exception.getMessage());
    }

    @Test
    public void ejecutarDeberiaLanzarErroBytesException_cuandoObtenerBytesFalla() throws IOException {
        String nombreArchivo = "imagen.png";
        MultipartFile file = mock(MultipartFile.class);

        when(file.getOriginalFilename()).thenReturn(nombreArchivo);
        when(getFileExtensionMock.execute(nombreArchivo)).thenReturn("png");
        when(validateExtensionMock.execute("png")).thenReturn(true);
        when(file.getBytes()).thenThrow(new IOException());

        ErroBytesException exception = assertThrows(ErroBytesException.class, () -> {
            uploadImageToTripo.execute(file);
        });

        assertEquals("Hubo un error al obtener los bytes de la imagen.", exception.getMessage());
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

        ErrorUploadImageToTripo exception = assertThrows(ErrorUploadImageToTripo.class, () -> {
            uploadImageToTripo.execute(file);
        });

        assertEquals("Error subiendo imagen", exception.getMessage());
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

        ErrorReadJsonException exception = assertThrows(ErrorReadJsonException.class, () -> {
            uploadImageToTripo.execute(file);
        });

        assertEquals("Error leyendo JSON", exception.getMessage());
    }
}
