package com.outfitlab.project.domain.useCases.tripo;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.interfaces.repositories.TripoRepository;
import com.outfitlab.project.infrastructure.repositories.TripoRepositoryImpl;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UploadImageToTripoTest {

    private TripoRepository tripoRepository = mock(TripoRepositoryImpl.class);
    private UploadImageToTripo uploadImageToTripo = new UploadImageToTripo(tripoRepository);

    @Test
    public void givenValidUrlWhenExecuteThenReturnResultMap() throws FileEmptyException, ErrorReadJsonException, ErroBytesException, ErrorUploadImageToTripoException {
        String url = "https://image.com/fotoOk.jpg";
        Map<String, Object> expectedResponse = Map.of("status", "ok");

        when(tripoRepository.requestUploadImagenToTripo(url)).thenReturn(expectedResponse);
        Map<String, Object> result = uploadImageToTripo.execute(url);

        assertNotNull(result);
        assertEquals("ok", result.get("status"));
        verify(tripoRepository, times(1)).requestUploadImagenToTripo(url);
    }

    @Test
    public void givenWrongImageWhenUploadImageToTripoThenThrowErroBytesException() throws FileEmptyException, ErrorReadJsonException, ErroBytesException, ErrorUploadImageToTripoException {
        String url = "https://image.com/cualquieraErrorBytes.jpg";
        when(tripoRepository.requestUploadImagenToTripo(url)).thenThrow(new ErroBytesException("Error de bytes"));
        assertThrows(ErroBytesException.class, () -> uploadImageToTripo.execute(url));
    }

    @Test
    public void givenValidResponseWhenUploadImageToTrippoAndReadTheJsomThenThrowErrorReadJsonException() throws FileEmptyException, ErrorReadJsonException, ErroBytesException, ErrorUploadImageToTripoException {
        String url = "https://image.com/cualquieraErrorJson.jpg";
        when(tripoRepository.requestUploadImagenToTripo(url)).thenThrow(new ErrorReadJsonException("Error al leer JSON"));
        assertThrows(ErrorReadJsonException.class, () -> uploadImageToTripo.execute(url));
    }

    @Test
    public void givenValidImageWhenUploadImageToTripoThenThrowsErrorUploadImageToTripoException() throws FileEmptyException, ErrorReadJsonException, ErroBytesException, ErrorUploadImageToTripoException {
        String url = "https://image.com/errorAlsubirla.jpg";
        when(tripoRepository.requestUploadImagenToTripo(url)).thenThrow(new ErrorUploadImageToTripoException("Error al subir imagen a Tripo"));
        assertThrows(ErrorUploadImageToTripoException.class, () -> uploadImageToTripo.execute(url));
    }

    @Test
    public void givenEmptyImageeWhenUploadImageToTripoThenThrowFileEmptyException() throws FileEmptyException, ErrorReadJsonException, ErroBytesException, ErrorUploadImageToTripoException {
        String url = "https://image.com/errorArchivoVacio.jpg";
        when(tripoRepository.requestUploadImagenToTripo(url)).thenThrow(new FileEmptyException("Archivo vacío"));
        assertThrows(FileEmptyException.class, () -> uploadImageToTripo.execute(url));
    }
}
