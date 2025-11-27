package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CreateGarmentTest {

    private final GarmentRepository garmentRepository = mock(GarmentRepository.class);
    private final CreateGarment createGarment = new CreateGarment(garmentRepository);

    @Test
    void givenValidDataWhenExecuteThenCallsRepositoryCorrectly() {
        String name = "Camisa Azul";

        whenExecute(name, "superior", "azul", "BR001", "url", "calido", List.of("casual"), "hombre");

        thenRepositoryWasCalledWith(
                name,
                "camisa_azul",
                "superior",
                "azul",
                "BR001",
                "url",
                "calido",
                List.of("casual"),
                "hombre"
        );
    }

    @Test
    void givenAccentedNameWhenExecuteThenFormatIsCorrect() {
        String name = "Pañuelo Óptimo";

        whenExecute(name, "accesorio", "rojo", "BR002", "img", "templado", List.of(), "unisex");

        thenRepositoryWasCalledWith(
                name,
                "panuelo_optimo",
                "accesorio",
                "rojo",
                "BR002",
                "img",
                "templado",
                List.of(),
                "unisex"
        );
    }

    @Test
    void givenRepositoryThrowsRuntimeWhenExecuteThenPropagateSameException() {
        RuntimeException expected = new RuntimeException("falló el repo");
        givenRepositoryThrows(expected);

        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> whenExecute("Camisa", "superior", "azul", "BR", "img", "calido", List.of(), "hombre"));

        assertEquals("falló el repo", thrown.getMessage());
    }


//privados -----
    private void whenExecute(String name, String type, String color, String brand, String img, String clima, List<String> ocasiones, String genero) {
        createGarment.execute(name, type, color, brand, img, clima, ocasiones, genero);
    }

    private void thenRepositoryWasCalledWith(String name, String expectedCode, String type,
            String color, String brand, String image, String clima, List<String> ocasiones, String genero) {
        verify(garmentRepository, times(1)).createGarment(eq(name), eq(expectedCode), eq(type), eq(color),
                        eq(brand), eq(image), eq(clima), eq(ocasiones), eq(genero));
    }

    private void givenRepositoryThrows(RuntimeException exception) {
        doThrow(exception)
                .when(garmentRepository)
                .createGarment(any(), any(), any(), any(), any(), any(), any(), any(), any());
    }
}
