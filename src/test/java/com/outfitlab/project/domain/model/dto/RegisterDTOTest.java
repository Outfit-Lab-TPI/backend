package com.outfitlab.project.domain.model.dto;

import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RegisterDTOTest {

    // ========== BASIC FIELD TESTS ==========

    @Test
    void givenValidDataWhenCreateRegisterDtoThenAllFieldsAreSet() {
        // GIVEN
        RegisterDTO dto = new RegisterDTO();

        // WHEN
        dto.setEmail("user@example.com");
        dto.setName("John");
        dto.setLastName("Doe");
        dto.setPassword("Password123");

        // THEN
        assertEquals("user@example.com", dto.getEmail());
        assertEquals("John", dto.getName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("Password123", dto.getPassword());
    }

    @Test
    void givenBrandDataWhenSetBrandFieldsThenFieldsAreSet() {
        // GIVEN
        RegisterDTO dto = new RegisterDTO();
        MultipartFile logo = mock(MultipartFile.class);

        // WHEN
        dto.setBrandName("Nike");
        dto.setUrlSite("https://nike.com");
        dto.setLogoBrand(logo);
        dto.setRegisterAsBrandAsBrand(true);

        // THEN
        assertEquals("Nike", dto.getBrandName());
        assertEquals("https://nike.com", dto.getUrlSite());
        assertEquals(logo, dto.getLogoBrand());
        assertTrue(dto.isRegisterAsBrandAsBrand());
    }

    @Test
    void givenNewDtoWhenCheckDefaultValuesThenBrandFieldsAreNull() {
        // GIVEN & WHEN
        RegisterDTO dto = new RegisterDTO();

        // THEN
        assertNull(dto.getEmail());
        assertNull(dto.getName());
        assertNull(dto.getLastName());
        assertNull(dto.getPassword());
        assertNull(dto.getBrandName());
        assertNull(dto.getUrlSite());
        assertNull(dto.getLogoBrand());
        assertFalse(dto.isRegisterAsBrandAsBrand());
    }

    // ========== BRAND REGISTRATION TESTS ==========

    @Test
    void givenRegisterAsBrandTrueWhenCheckThenReturnTrue() {
        // GIVEN
        RegisterDTO dto = new RegisterDTO();

        // WHEN
        dto.setRegisterAsBrandAsBrand(true);

        // THEN
        assertTrue(dto.isRegisterAsBrandAsBrand());
    }

    @Test
    void givenRegisterAsBrandFalseWhenCheckThenReturnFalse() {
        // GIVEN
        RegisterDTO dto = new RegisterDTO();

        // WHEN
        dto.setRegisterAsBrandAsBrand(false);

        // THEN
        assertFalse(dto.isRegisterAsBrandAsBrand());
    }

    // ========== LOGO HANDLING TESTS ==========

    @Test
    void givenMultipartFileWhenSetLogoBrandThenLogoIsSet() {
        // GIVEN
        RegisterDTO dto = new RegisterDTO();
        MultipartFile mockLogo = mock(MultipartFile.class);

        // WHEN
        dto.setLogoBrand(mockLogo);

        // THEN
        assertNotNull(dto.getLogoBrand());
        assertEquals(mockLogo, dto.getLogoBrand());
    }

    @Test
    void givenNullLogoWhenSetLogoBrandThenLogoIsNull() {
        // GIVEN
        RegisterDTO dto = new RegisterDTO();

        // WHEN
        dto.setLogoBrand(null);

        // THEN
        assertNull(dto.getLogoBrand());
    }
}
