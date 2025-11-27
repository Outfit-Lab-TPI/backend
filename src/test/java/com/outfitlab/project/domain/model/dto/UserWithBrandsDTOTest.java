package com.outfitlab.project.domain.model.dto;

import com.outfitlab.project.domain.enums.Role;
import com.outfitlab.project.domain.model.BrandModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserWithBrandsDTOTest {

    // ========== CONSTRUCTOR TESTS ==========

    @Test
    void givenValidDataWhenCreateWith8ParamsThenAllFieldsAreSet() {
        // GIVEN
        String email = "user@example.com";
        String name = "John";
        Role role = Role.USER;
        boolean status = true;
        boolean verified = true;
        boolean brandApproved = false;
        String userImageUrl = "http://image.url/user.jpg";
        BrandModel brand = null;

        // WHEN
        UserWithBrandsDTO dto = new UserWithBrandsDTO(email, name, role, status, verified, brandApproved, userImageUrl,
                brand);

        // THEN
        assertEquals(email, dto.getEmail());
        assertEquals(name, dto.getName());
        assertEquals(role, dto.getRole());
        assertTrue(dto.isStatus());
        assertTrue(dto.isVerified());
        assertFalse(dto.isBrandApproved());
        assertEquals(userImageUrl, dto.getUserImageUrl());
        assertNull(dto.getBrand());
        assertNull(dto.getLastname()); // Not set in 8-param constructor
    }

    @Test
    void givenValidDataWhenCreateWith9ParamsThenAllFieldsAreSet() {
        // GIVEN
        String email = "brand@example.com";
        String lastName = "Doe";
        String name = "John";
        Role role = Role.BRAND;
        boolean status = true;
        boolean verified = true;
        boolean brandApproved = true;
        String userImageUrl = "http://image.url/brand.jpg";
        BrandModel brand = givenBrandModel();

        // WHEN
        UserWithBrandsDTO dto = new UserWithBrandsDTO(email, lastName, name, role, status, verified, brandApproved,
                userImageUrl, brand);

        // THEN
        assertEquals(email, dto.getEmail());
        assertEquals(name, dto.getName());
        assertEquals(lastName, dto.getLastname());
        assertEquals(role, dto.getRole());
        assertTrue(dto.isStatus());
        assertTrue(dto.isVerified());
        assertTrue(dto.isBrandApproved());
        assertEquals(userImageUrl, dto.getUserImageUrl());
        assertNotNull(dto.getBrand());
    }

    @Test
    void givenEmptyConstructorWhenCreateThenFieldsAreNull() {
        // WHEN
        UserWithBrandsDTO dto = new UserWithBrandsDTO();

        // THEN
        assertNull(dto.getEmail());
        assertNull(dto.getName());
        assertNull(dto.getRole());
        assertFalse(dto.isStatus());
        assertFalse(dto.isVerified());
        assertFalse(dto.isBrandApproved());
        assertNull(dto.getUserImageUrl());
        assertNull(dto.getBrand());
        assertNull(dto.getLastname());
    }

    // ========== SETTER/GETTER TESTS ==========

    @Test
    void givenDtoWhenSetEmailThenGetReturnsCorrectValue() {
        // GIVEN
        UserWithBrandsDTO dto = new UserWithBrandsDTO();
        String email = "test@example.com";

        // WHEN
        dto.setEmail(email);

        // THEN
        assertEquals(email, dto.getEmail());
    }

    @Test
    void givenDtoWhenSetRoleThenGetReturnsCorrectValue() {
        // GIVEN
        UserWithBrandsDTO dto = new UserWithBrandsDTO();

        // WHEN
        dto.setRole(Role.ADMIN);

        // THEN
        assertEquals(Role.ADMIN, dto.getRole());
    }

    @Test
    void givenDtoWhenSetStatusThenGetReturnsCorrectValue() {
        // GIVEN
        UserWithBrandsDTO dto = new UserWithBrandsDTO();

        // WHEN
        dto.setStatus(true);

        // THEN
        assertTrue(dto.isStatus());
    }

    @Test
    void givenDtoWhenSetVerifiedThenGetReturnsCorrectValue() {
        // GIVEN
        UserWithBrandsDTO dto = new UserWithBrandsDTO();

        // WHEN
        dto.setVerified(true);

        // THEN
        assertTrue(dto.isVerified());
    }

    @Test
    void givenDtoWhenSetBrandApprovedThenGetReturnsCorrectValue() {
        // GIVEN
        UserWithBrandsDTO dto = new UserWithBrandsDTO();

        // WHEN
        dto.setBrandApproved(true);

        // THEN
        assertTrue(dto.isBrandApproved());
    }

    @Test
    void givenDtoWhenSetBrandThenGetReturnsCorrectValue() {
        // GIVEN
        UserWithBrandsDTO dto = new UserWithBrandsDTO();
        BrandModel brand = givenBrandModel();

        // WHEN
        dto.setBrand(brand);

        // THEN
        assertEquals(brand, dto.getBrand());
    }

    // ========== BUSINESS LOGIC TESTS ==========

    @Test
    void givenBrandUserWhenCheckRoleThenRoleIsBrand() {
        // GIVEN
        BrandModel brand = givenBrandModel();
        UserWithBrandsDTO dto = new UserWithBrandsDTO("brand@example.com", "John", Role.BRAND,
                true, true, true, "http://img.jpg", brand);

        // WHEN & THEN
        assertEquals(Role.BRAND, dto.getRole());
        assertNotNull(dto.getBrand());
        assertTrue(dto.isBrandApproved());
    }

    @Test
    void givenRegularUserWhenCheckRoleThenRoleIsUser() {
        // GIVEN
        UserWithBrandsDTO dto = new UserWithBrandsDTO("user@example.com", "Jane", Role.USER,
                true, true, false, "http://img.jpg", null);

        // WHEN & THEN
        assertEquals(Role.USER, dto.getRole());
        assertNull(dto.getBrand());
        assertFalse(dto.isBrandApproved());
    }

    // ========== HELPER METHODS (GIVEN) ==========

    private BrandModel givenBrandModel() {
        BrandModel brand = new BrandModel();
        brand.setCodigoMarca("NIKE-001");
        brand.setNombre("Nike");
        brand.setLogoUrl("http://logo.url/nike.png");
        return brand;
    }
}
