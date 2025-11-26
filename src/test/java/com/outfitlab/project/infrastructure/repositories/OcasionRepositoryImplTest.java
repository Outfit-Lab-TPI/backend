package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.exceptions.OcasionNotFoundException;
import com.outfitlab.project.domain.model.OcasionModel;
import com.outfitlab.project.infrastructure.model.OcasionEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.OcasionJpaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class OcasionRepositoryImplTest {

    @Mock
    private OcasionJpaRepository ocasionJpaRepository;

    @InjectMocks
    private OcasionRepositoryImpl repository;

    @Test
    void shouldReturnAllOcasiones() {
        givenExistingOcaciones(1);

        List<OcasionModel> result = whenFindAll();

        thenListSizeShouldBe(result, 1);
    }

    @Test
    void shouldThrowExceptionWhenNoOcasionesFound() {
        givenNoOcaciones();

        assertThrows(OcasionNotFoundException.class, this::whenFindAll);
    }

    private void givenExistingOcaciones(int qty) {
        List<OcasionEntity> entities = new ArrayList<>();
        for (int i = 0; i < qty; i++) {
            OcasionEntity e = mock(OcasionEntity.class);
            when(e.convertEntityToModel(e)).thenReturn(new OcasionModel());
            entities.add(e);
        }
        when(ocasionJpaRepository.findAll()).thenReturn(entities);
    }

    private void givenNoOcaciones() {
        when(ocasionJpaRepository.findAll()).thenReturn(List.of());
    }

    private List<OcasionModel> whenFindAll() {
        return repository.findAllOcasiones();
    }

    private void thenListSizeShouldBe(List<?> list, int expected) {
        assertEquals(expected, list.size());
    }
}