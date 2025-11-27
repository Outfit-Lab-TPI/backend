package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.infrastructure.repositories.GarmentRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetGarmentsByTypeTest {

    private final GarmentRepository garmentRepository = mock(GarmentRepositoryImpl.class);
    private final GetGarmentsByType getGarmentsByType = new GetGarmentsByType(garmentRepository);

    @Test
    public void givenValidTypeWithGarmentsWhenExecuteThenReturnPageSuccessfully() throws GarmentNotFoundException {
        String type = givenType("superior");
        int page = givenPage(0);
        Page<PrendaModel> pageResponse = givenPageWithElements(2);

        mockRepositoryReturning(type, page, pageResponse);

        Page<PrendaModel> result = whenExecute(type, page);

        thenResultNotNull(result);
        thenResultHasSize(result, 2);
        thenRepositoryCalledOnce(type, page);
    }

    @Test
    public void givenValidTypeWithNoGarmentsWhenExecuteThenThrowGarmentNotFoundException() {
        String type = givenType("inferior");
        int page = givenPage(1);
        Page<PrendaModel> emptyPage = givenEmptyPage();

        mockRepositoryReturning(type, page, emptyPage);

        GarmentNotFoundException ex = assertThrows(GarmentNotFoundException.class,
                () -> whenExecute(type, page));

        thenMessageContains(ex, "No encontramos prendas de tipo: " + type);
        thenRepositoryCalledOnce(type, page);
    }

    @Test
    public void givenEmptyPageWhenExecuteThenThrowGarmentNotFoundException() {
        String type = givenType("accesorio");
        int page = givenPage(2);
        Page<PrendaModel> emptyPage = givenEmptyPage();

        mockRepositoryReturning(type, page, emptyPage);

        assertThrows(GarmentNotFoundException.class, () -> whenExecute(type, page));
        thenRepositoryCalledOnce(type, page);
    }



    // privados ---
    private String givenType(String type) {
        return type;
    }

    private int givenPage(int page) {
        return page;
    }

    private Page<PrendaModel> givenPageWithElements(int count) {
        return new PageImpl<>(
                java.util.stream.Stream.generate(PrendaModel::new).limit(count).toList()
        );
    }

    private Page<PrendaModel> givenEmptyPage() {
        return new PageImpl<>(List.of());
    }

    private void mockRepositoryReturning(String type, int page, Page<PrendaModel> result) {
        when(garmentRepository.getGarmentsByType(type.toLowerCase(), page)).thenReturn(result);
    }

    private Page<PrendaModel> whenExecute(String type, int page) throws GarmentNotFoundException {
        return getGarmentsByType.execute(type, page);
    }

    private void thenResultNotNull(Object result) {
        assertNotNull(result);
    }

    private void thenResultHasSize(Page<?> page, int size) {
        assertEquals(size, page.getContent().size());
    }

    private void thenResultIsEmpty(Page<?> page) {
        assertTrue(page.isEmpty());
    }

    private void thenMessageContains(Exception ex, String expected) {
        assertTrue(ex.getMessage().contains(expected));
    }

    private void thenRepositoryCalledOnce(String type, int page) {
        verify(garmentRepository, times(1))
                .getGarmentsByType(type.toLowerCase(), page);
    }
}
