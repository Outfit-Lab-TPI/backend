package com.outfitlab.project.domain.useCases;

import com.outfitlab.project.domain.exceptions.ErrorTripoEntityNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.TripoRepository;
import com.outfitlab.project.domain.model.TripoModel;
import com.outfitlab.project.domain.useCases.tripo.UpdateTripoModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UpdateTripoModelTest {

    private TripoRepository tripoRepositoryMock;
    private UpdateTripoModel updateTripoModel;

    @BeforeEach
    public void setUp() {
        tripoRepositoryMock = mock(TripoRepository.class);
        updateTripoModel = new UpdateTripoModel(tripoRepositoryMock);
    }

    @Test
    public void ejecutarDeberiaActualizarYDevolverTripoModel_cuandoModeloExiste() throws ErrorTripoEntityNotFoundException {
        TripoModel modelo = new TripoModel();
        modelo.setImageToken("TripoActualizado");

        when(tripoRepositoryMock.update(modelo)).thenReturn(modelo);

        TripoModel resultado = updateTripoModel.execute(modelo);

        assertNotNull(resultado);
        assertEquals("TripoActualizado", resultado.getImageToken());
        verify(tripoRepositoryMock, times(1)).update(modelo);
    }

    @Test
    public void ejecutarDeberiaLanzarErrorTripoEntityNotFound_cuandoRepositorioLanzaExcepcion() throws ErrorTripoEntityNotFoundException {
        TripoModel modelo = new TripoModel();

        when(tripoRepositoryMock.update(modelo))
                .thenThrow(new ErrorTripoEntityNotFoundException("Tripo no encontrado"));

        assertThrows(ErrorTripoEntityNotFoundException.class, () -> {
            updateTripoModel.execute(modelo);
        });
    }
}
