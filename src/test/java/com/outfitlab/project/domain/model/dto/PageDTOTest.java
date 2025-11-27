package com.outfitlab.project.domain.model.dto;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PageDTOTest {

    // ========== CONSTRUCTOR TESTS ==========

    @Test
    void givenValidDataWhenCreateWithConstructorThenAllFieldsAreSet() {
        // GIVEN
        List<String> content = Arrays.asList("Item1", "Item2", "Item3");
        int page = 0;
        int size = 10;
        long totalElements = 25L;
        int totalPages = 3;
        boolean last = false;

        // WHEN
        PageDTO<String> pageDTO = new PageDTO<>(content, page, size, totalElements, totalPages, last);

        // THEN
        assertEquals(content, pageDTO.getContent());
        assertEquals(page, pageDTO.getPage());
        assertEquals(size, pageDTO.getSize());
        assertEquals(totalElements, pageDTO.getTotalElements());
        assertEquals(totalPages, pageDTO.getTotalPages());
        assertEquals(last, pageDTO.isLast());
    }

    @Test
    void givenEmptyConstructorWhenCreateThenAllFieldsAreNull() {
        // WHEN
        PageDTO<String> pageDTO = new PageDTO<>();

        // THEN
        assertNull(pageDTO.getContent());
        assertEquals(0, pageDTO.getPage());
        assertEquals(0, pageDTO.getSize());
        assertEquals(0L, pageDTO.getTotalElements());
        assertEquals(0, pageDTO.getTotalPages());
        assertFalse(pageDTO.isLast());
    }

    // ========== PAGINATION LOGIC TESTS ==========

    @Test
    void givenFirstPageWhenCreateThenPageIsZeroAndNotLast() {
        // GIVEN
        List<String> content = Arrays.asList("Item1", "Item2");
        int page = 0;
        int size = 2;
        long totalElements = 10L;
        int totalPages = 5;
        boolean last = false;

        // WHEN
        PageDTO<String> pageDTO = new PageDTO<>(content, page, size, totalElements, totalPages, last);

        // THEN
        assertEquals(0, pageDTO.getPage());
        assertFalse(pageDTO.isLast());
        assertEquals(5, pageDTO.getTotalPages());
    }

    @Test
    void givenLastPageWhenCreateThenIsLastIsTrue() {
        // GIVEN
        List<String> content = Arrays.asList("Item9", "Item10");
        int page = 4;
        int size = 2;
        long totalElements = 10L;
        int totalPages = 5;
        boolean last = true;

        // WHEN
        PageDTO<String> pageDTO = new PageDTO<>(content, page, size, totalElements, totalPages, last);

        // THEN
        assertEquals(4, pageDTO.getPage());
        assertTrue(pageDTO.isLast());
        assertEquals(5, pageDTO.getTotalPages());
    }

    @Test
    void givenEmptyPageWhenCreateThenContentIsEmpty() {
        // GIVEN
        List<String> content = Collections.emptyList();
        int page = 0;
        int size = 10;
        long totalElements = 0L;
        int totalPages = 0;
        boolean last = true;

        // WHEN
        PageDTO<String> pageDTO = new PageDTO<>(content, page, size, totalElements, totalPages, last);

        // THEN
        assertNotNull(pageDTO.getContent());
        assertTrue(pageDTO.getContent().isEmpty());
        assertEquals(0, pageDTO.getTotalElements());
        assertEquals(0, pageDTO.getTotalPages());
        assertTrue(pageDTO.isLast());
    }

    @Test
    void givenSinglePageWhenCreateThenPageIsZeroAndIsLast() {
        // GIVEN
        List<String> content = Arrays.asList("Item1", "Item2", "Item3");
        int page = 0;
        int size = 10;
        long totalElements = 3L;
        int totalPages = 1;
        boolean last = true;

        // WHEN
        PageDTO<String> pageDTO = new PageDTO<>(content, page, size, totalElements, totalPages, last);

        // THEN
        assertEquals(0, pageDTO.getPage());
        assertTrue(pageDTO.isLast());
        assertEquals(1, pageDTO.getTotalPages());
        assertEquals(3, pageDTO.getTotalElements());
    }

    // ========== SETTER/GETTER TESTS ==========

    @Test
    void givenPageDtoWhenSetContentThenGetContentReturnsCorrectValue() {
        // GIVEN
        PageDTO<String> pageDTO = new PageDTO<>();
        List<String> newContent = Arrays.asList("New1", "New2");

        // WHEN
        pageDTO.setContent(newContent);

        // THEN
        assertEquals(newContent, pageDTO.getContent());
    }

    @Test
    void givenPageDtoWhenSetPageThenGetPageReturnsCorrectValue() {
        // GIVEN
        PageDTO<String> pageDTO = new PageDTO<>();
        int newPage = 5;

        // WHEN
        pageDTO.setPage(newPage);

        // THEN
        assertEquals(newPage, pageDTO.getPage());
    }

    @Test
    void givenPageDtoWhenSetSizeThenGetSizeReturnsCorrectValue() {
        // GIVEN
        PageDTO<String> pageDTO = new PageDTO<>();
        int newSize = 20;

        // WHEN
        pageDTO.setSize(newSize);

        // THEN
        assertEquals(newSize, pageDTO.getSize());
    }

    @Test
    void givenPageDtoWhenSetTotalElementsThenGetTotalElementsReturnsCorrectValue() {
        // GIVEN
        PageDTO<String> pageDTO = new PageDTO<>();
        long newTotalElements = 100L;

        // WHEN
        pageDTO.setTotalElements(newTotalElements);

        // THEN
        assertEquals(newTotalElements, pageDTO.getTotalElements());
    }

    @Test
    void givenPageDtoWhenSetTotalPagesThenGetTotalPagesReturnsCorrectValue() {
        // GIVEN
        PageDTO<String> pageDTO = new PageDTO<>();
        int newTotalPages = 10;

        // WHEN
        pageDTO.setTotalPages(newTotalPages);

        // THEN
        assertEquals(newTotalPages, pageDTO.getTotalPages());
    }

    @Test
    void givenPageDtoWhenSetLastThenIsLastReturnsCorrectValue() {
        // GIVEN
        PageDTO<String> pageDTO = new PageDTO<>();

        // WHEN
        pageDTO.setLast(true);

        // THEN
        assertTrue(pageDTO.isLast());

        // WHEN
        pageDTO.setLast(false);

        // THEN
        assertFalse(pageDTO.isLast());
    }

    // ========== GENERIC TYPE TESTS ==========

    @Test
    void givenGarmentDtoListWhenCreatePageDtoThenGenericTypeWorks() {
        // GIVEN
        GarmentDTO garment1 = new GarmentDTO("Shirt", "superior", "http://img1.jpg", "SHIRT-001", "Nike", "Rojo",
                "Calido");
        GarmentDTO garment2 = new GarmentDTO("Pants", "inferior", "http://img2.jpg", "PANTS-001", "Adidas", "Azul",
                "Frio");
        List<GarmentDTO> content = Arrays.asList(garment1, garment2);

        // WHEN
        PageDTO<GarmentDTO> pageDTO = new PageDTO<>(content, 0, 10, 2L, 1, true);

        // THEN
        assertNotNull(pageDTO.getContent());
        assertEquals(2, pageDTO.getContent().size());
        assertEquals(garment1, pageDTO.getContent().get(0));
        assertEquals(garment2, pageDTO.getContent().get(1));
    }

    @Test
    void givenIntegerListWhenCreatePageDtoThenGenericTypeWorks() {
        // GIVEN
        List<Integer> content = Arrays.asList(1, 2, 3, 4, 5);

        // WHEN
        PageDTO<Integer> pageDTO = new PageDTO<>(content, 0, 5, 5L, 1, true);

        // THEN
        assertNotNull(pageDTO.getContent());
        assertEquals(5, pageDTO.getContent().size());
        assertEquals(Integer.valueOf(1), pageDTO.getContent().get(0));
    }
}
