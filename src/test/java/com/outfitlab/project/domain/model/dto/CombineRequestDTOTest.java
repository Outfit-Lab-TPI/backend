package com.outfitlab.project.domain.model.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CombineRequestDTOTest {

    // ========== CONSTRUCTOR TESTS ==========

    @Test
    void givenValidDataWhenCreateWithConstructorThenAllFieldsAreSet() {
        // GIVEN
        String top = "SHIRT-001";
        String bottom = "PANTS-001";
        Boolean isMan = true;
        String avatarType = "casual";

        // WHEN
        CombineRequestDTO dto = new CombineRequestDTO(top, bottom, isMan, avatarType);

        // THEN
        thenDtoHasCorrectFields(dto, top, bottom, isMan, avatarType);
    }

    @Test
    void givenEmptyConstructorWhenCreateThenFieldsAreNull() {
        // WHEN
        CombineRequestDTO dto = new CombineRequestDTO();

        // THEN
        assertNull(dto.getTop());
        assertNull(dto.getBottom());
        assertNull(dto.getIsMan());
        assertNull(dto.getAvatarType());
    }

    // ========== SETTER/GETTER TESTS ==========

    @Test
    void givenDtoWhenSetTopThenGetReturnsCorrectValue() {
        // GIVEN
        CombineRequestDTO dto = new CombineRequestDTO();
        String top = "JACKET-001";

        // WHEN
        dto.setTop(top);

        // THEN
        assertEquals(top, dto.getTop());
    }

    @Test
    void givenDtoWhenSetBottomThenGetReturnsCorrectValue() {
        // GIVEN
        CombineRequestDTO dto = new CombineRequestDTO();
        String bottom = "SHORTS-001";

        // WHEN
        dto.setBottom(bottom);

        // THEN
        assertEquals(bottom, dto.getBottom());
    }

    @Test
    void givenDtoWhenSetIsManThenGetReturnsCorrectValue() {
        // GIVEN
        CombineRequestDTO dto = new CombineRequestDTO();

        // WHEN
        dto.setIsMan(true);

        // THEN
        assertTrue(dto.getIsMan());

        // WHEN
        dto.setIsMan(false);

        // THEN
        assertFalse(dto.getIsMan());
    }

    @Test
    void givenDtoWhenSetAvatarTypeThenGetReturnsCorrectValue() {
        // GIVEN
        CombineRequestDTO dto = new CombineRequestDTO();
        String avatarType = "formal";

        // WHEN
        dto.setAvatarType(avatarType);

        // THEN
        assertEquals(avatarType, dto.getAvatarType());
    }

    // ========== TO_STRING TEST ==========

    @Test
    void givenDtoWhenToStringThenContainsAllFields() {
        // GIVEN
        CombineRequestDTO dto = new CombineRequestDTO("SHIRT-001", "PANTS-001", true, "casual");

        // WHEN
        String result = dto.toString();

        // THEN
        assertTrue(result.contains("top='SHIRT-001'"));
        assertTrue(result.contains("bottom='PANTS-001'"));
        assertTrue(result.contains("isMan=true"));
        assertTrue(result.contains("avatarType='casual'"));
    }

    // ========== BUSINESS LOGIC TESTS ==========

    @Test
    void givenMaleUserWhenCreateRequestThenIsManIsTrue() {
        // GIVEN & WHEN
        CombineRequestDTO dto = new CombineRequestDTO("SHIRT-001", "PANTS-001", true, "casual");

        // THEN
        assertTrue(dto.getIsMan());
    }

    @Test
    void givenFemaleUserWhenCreateRequestThenIsManIsFalse() {
        // GIVEN & WHEN
        CombineRequestDTO dto = new CombineRequestDTO("BLOUSE-001", "SKIRT-001", false, "elegant");

        // THEN
        assertFalse(dto.getIsMan());
    }

    // ========== HELPER METHODS (THEN) ==========

    private void thenDtoHasCorrectFields(CombineRequestDTO dto, String expectedTop,
            String expectedBottom, Boolean expectedIsMan, String expectedAvatarType) {
        assertEquals(expectedTop, dto.getTop());
        assertEquals(expectedBottom, dto.getBottom());
        assertEquals(expectedIsMan, dto.getIsMan());
        assertEquals(expectedAvatarType, dto.getAvatarType());
    }
}
