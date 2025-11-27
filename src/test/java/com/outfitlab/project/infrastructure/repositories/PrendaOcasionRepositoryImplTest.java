package com.outfitlab.project.infrastructure.repositories;
import com.outfitlab.project.infrastructure.repositories.interfaces.PrendaOcasionJpaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
class PrendaOcasionRepositoryImplTest {

    @Mock
    private PrendaOcasionJpaRepository prendaOcasionJpaRepository;

    @InjectMocks
    private PrendaOcasionRepositoryImpl repository;

    @Test
    public void shouldDeleteAllPrendaOcacionByGarment() {
        String garmentCode = givenGarmentCode();

        whenDeleteByGarmentCode(garmentCode);

        thenRepositoryShouldCallDeleteMethod(garmentCode);
    }

    private String givenGarmentCode() {
        return "GARM123";
    }

    private void whenDeleteByGarmentCode(String garmentCode) {
        repository.deleteAllPrendaOcasionByGarment(garmentCode);
    }

    private void thenRepositoryShouldCallDeleteMethod(String garmentCode) {
        verify(prendaOcasionJpaRepository).deleteAllByGarmentCode(garmentCode);
    }
}