package com.outfitlab.project.domain.useCases.tripo;

import com.outfitlab.project.domain.exceptions.ErroBytesException;
import com.outfitlab.project.domain.exceptions.ErrorReadJsonException;
import com.outfitlab.project.domain.exceptions.ErrorUploadImageToTripoException;
import com.outfitlab.project.domain.exceptions.FileEmptyException;
import com.outfitlab.project.domain.interfaces.repositories.TripoRepository;
import com.outfitlab.project.infrastructure.repositories.TripoRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UploadImageToTripoTest {

    private TripoRepository tripoRepository = mock(TripoRepositoryImpl.class);
    private UploadImageToTripo uploadImageToTripo;

    private final String VALID_URL = "https://image.com/fotoOk.jpg";
    private final String STATUS_OK = "ok";
    private final Map<String, Object> SUCCESS_RESPONSE = Map.of("status", STATUS_OK);
    private final String URL_ERROR_BYTES = "https://image.com/cualquieraErrorBytes.jpg";
    private final String URL_ERROR_JSON = "https://image.com/cualquieraErrorJson.jpg";
    private final String URL_ERROR_UPLOAD = "https://image.com/errorAlsubirla.jpg";
    private final String URL_ERROR_EMPTY = "https://image.com/errorArchivoVacio.jpg";


    @BeforeEach
    void setUp() {
        uploadImageToTripo = new UploadImageToTripo(tripoRepository);
    }


    @Test
    public void shouldReturnSuccessStatusMapWhenUploadIsSuccessful() throws Exception {
        givenRepositoryReturnsResponse(VALID_URL, SUCCESS_RESPONSE);

        Map<String, Object> result = whenExecuteUploadImage(VALID_URL);

        thenReturnedMapIsSuccessful(result, STATUS_OK);
        thenRepositoryWasCalledOnce(VALID_URL);
    }

    @Test
    public void shouldPropagateErroBytesExceptionWhenFileIsCorrupted() {
        givenRepositoryThrowsException(URL_ERROR_BYTES, new ErroBytesException("Error de bytes"));

        thenExecutionThrowsException(URL_ERROR_BYTES, ErroBytesException.class);
    }

    @Test
    public void shouldPropagateErrorReadJsonExceptionWhenResponseIsMalformed() {
        givenRepositoryThrowsException(URL_ERROR_JSON, new ErrorReadJsonException("Error al leer JSON"));

        thenExecutionThrowsException(URL_ERROR_JSON, ErrorReadJsonException.class);
    }

    @Test
    public void shouldPropagateErrorUploadImageToTripoExceptionWhenTripoUploadFails() {
        givenRepositoryThrowsException(URL_ERROR_UPLOAD, new ErrorUploadImageToTripoException("Error al subir imagen a Tripo"));

        thenExecutionThrowsException(URL_ERROR_UPLOAD, ErrorUploadImageToTripoException.class);
    }

    @Test
    public void shouldPropagateFileEmptyExceptionWhenFileIsReportedEmpty() {
        givenRepositoryThrowsException(URL_ERROR_EMPTY, new FileEmptyException("Archivo vacío"));

        thenExecutionThrowsException(URL_ERROR_EMPTY, FileEmptyException.class);
    }


    //privadosss
    private void givenRepositoryReturnsResponse(String url, Map<String, Object> response) throws FileEmptyException, ErrorReadJsonException, ErroBytesException, ErrorUploadImageToTripoException {
        when(tripoRepository.requestUploadImagenToTripo(url)).thenReturn(response);
    }

    private void givenRepositoryThrowsException(String url, Exception exception) {
        try {
            doThrow(exception).when(tripoRepository).requestUploadImagenToTripo(url);
        } catch (FileEmptyException | ErrorReadJsonException | ErroBytesException | ErrorUploadImageToTripoException e) {
        }
    }

    private Map<String, Object> whenExecuteUploadImage(String url) throws FileEmptyException, ErrorReadJsonException, ErroBytesException, ErrorUploadImageToTripoException {
        return uploadImageToTripo.execute(url);
    }


    private void thenReturnedMapIsSuccessful(Map<String, Object> result, String expectedStatus) {
        assertNotNull(result, "El resultado no debe ser nulo.");
        assertTrue(result.containsKey("status"), "El mapa debe contener la clave 'status'.");
        assertEquals(expectedStatus, result.get("status"), "El estado debe ser 'ok'.");
    }

    private void thenExecutionThrowsException(String url, Class<? extends Exception> expectedException) {
        assertThrows(expectedException, () -> uploadImageToTripo.execute(url));

        thenRepositoryWasCalledOnce(url);
    }

    private void thenRepositoryWasCalledOnce(String url) {
        verify(tripoRepository, times(1)).requestUploadImagenToTripo(url);
    }
}