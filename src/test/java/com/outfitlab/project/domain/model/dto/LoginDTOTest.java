package com.outfitlab.project.domain.model.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginDTOTest {

    // ========== CONSTRUCTOR TESTS ==========

    @Test
    void givenNoArgsConstructorWhenCreateThenFieldsAreNull() {
        // WHEN
        LoginDTO dto = new LoginDTO();

        // THEN
        assertNull(dto.getEmail());
        assertNull(dto.getPassword());
    }

    @Test
    void givenAllArgsConstructorWhenCreateThenAllFieldsAreSet() {
        // GIVEN
        String email = "user@example.com";
        String password = "Password123";

        // WHEN
        LoginDTO dto = new LoginDTO(email, password);

        // THEN
        assertEquals(email, dto.getEmail());
        assertEquals(password, dto.getPassword());
    }

    // ========== BUILDER TESTS ==========

    @Test
    void givenBuilderWhenBuildWithAllFieldsThenAllFieldsAreSet() {
        // GIVEN & WHEN
        LoginDTO dto = LoginDTO.builder()
                .email("user@example.com")
                .password("Password123")
                .build();

        // THEN
        assertEquals("user@example.com", dto.getEmail());
        assertEquals("Password123", dto.getPassword());
    }

    @Test
    void givenBuilderWhenBuildWithPartialFieldsThenOnlySetFieldsArePopulated() {
        // GIVEN & WHEN
        LoginDTO dto = LoginDTO.builder()
                .email("user@example.com")
                .build();

        // THEN
        assertEquals("user@example.com", dto.getEmail());
        assertNull(dto.getPassword());
    }

    // ========== SETTER/GETTER TESTS ==========

    @Test
    void givenDtoWhenSetEmailThenGetEmailReturnsCorrectValue() {
        // GIVEN
        LoginDTO dto = new LoginDTO();
        String email = "test@example.com";

        // WHEN
        dto.setEmail(email);

        // THEN
        assertEquals(email, dto.getEmail());
    }

    @Test
    void givenDtoWhenSetPasswordThenGetPasswordReturnsCorrectValue() {
        // GIVEN
        LoginDTO dto = new LoginDTO();
        String password = "SecurePass123";

        // WHEN
        dto.setPassword(password);

        // THEN
        assertEquals(password, dto.getPassword());
    }

    // ========== EQUALS/HASHCODE TESTS (Lombok @Data) ==========

    @Test
    void givenTwoEqualDtosWhenCompareThenAreEqual() {
        // GIVEN
        LoginDTO dto1 = new LoginDTO("user@example.com", "Password123");
        LoginDTO dto2 = new LoginDTO("user@example.com", "Password123");

        // THEN
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void givenTwoDifferentDtosWhenCompareThenAreNotEqual() {
        // GIVEN
        LoginDTO dto1 = new LoginDTO("user1@example.com", "Password123");
        LoginDTO dto2 = new LoginDTO("user2@example.com", "Password456");

        // THEN
        assertNotEquals(dto1, dto2);
    }
}
