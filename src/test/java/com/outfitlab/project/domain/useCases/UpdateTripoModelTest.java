package com.outfitlab.project.domain.useCases;

import com.outfitlab.project.domain.exceptions.ErrorTripoEntityNotFound;
import com.outfitlab.project.domain.interfaces.repositories.ITripoRepository;
import com.outfitlab.project.domain.model.TripoModel;
import com.outfitlab.project.domain.useCases.tripo.UpdateTripoModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UpdateTripoModelTest {

    private ITripoRepository tripoRepositoryMock;
    private UpdateTripoModel updateTripoModel;

    @BeforeEach
    public void setUp() {
        tripoRepositoryMock = mock(ITripoRepository.class);
        updateTripoModel = new UpdateTripoModel(tripoRepositoryMock);
    }

    @Test
    public void ejecutarDeberiaActualizarYDevolverTripoModel_cuandoModeloExiste() throws ErrorTripoEntityNotFound {
        TripoModel modelo = new TripoModel();
        modelo.setImageToken("TripoActualizado");

        when(tripoRepositoryMock.update(modelo)).thenReturn(modelo);

        TripoModel resultado = updateTripoModel.execute(modelo);

        assertNotNull(resultado);
        assertEquals("TripoActualizado", resultado.getImageToken());
        verify(tripoRepositoryMock, times(1)).update(modelo);
    }

    @Test
    public void ejecutarDeberiaLanzarErrorTripoEntityNotFound_cuandoRepositorioLanzaExcepcion() throws ErrorTripoEntityNotFound {
        TripoModel modelo = new TripoModel();

        when(tripoRepositoryMock.update(modelo))
                .thenThrow(new ErrorTripoEntityNotFound("Tripo no encontrado"));

        assertThrows(ErrorTripoEntityNotFound.class, () -> {
            updateTripoModel.execute(modelo);
        });
    }
}
