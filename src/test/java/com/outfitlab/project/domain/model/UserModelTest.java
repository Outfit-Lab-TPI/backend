package com.outfitlab.project.domain.model;

import com.outfitlab.project.domain.enums.Role;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserModelTest {

    // ========== CONSTRUCTOR TESTS ==========

    @Test
    void givenEmptyConstructorWhenCreateThenFieldsAreNull() {
        // WHEN
        UserModel user = new UserModel();

        // THEN
        assertNull(user.getId());
        assertNull(user.getEmail());
        assertNull(user.getName());
        assertNull(user.getHashedPassword());
        assertFalse(user.isVerified());
        assertFalse(user.isStatus());
    }

    @Test
    void givenBasicDataWhenCreateWith5ParamsThenFieldsAreSet() {
        // GIVEN
        String email = "user@example.com";
        String name = "John";
        String lastName = "Doe";
        String password = "hashedPassword123";
        String token = "verification-token";

        // WHEN
        UserModel user = new UserModel(email, name, lastName, password, token);

        // THEN
        assertEquals(email, user.getEmail());
        assertEquals(name, user.getName());
        assertEquals(lastName, user.getLastName());
        assertEquals(password, user.getHashedPassword());
        assertEquals(token, user.getVerificationToken());
    }

    @Test
    void givenIdAndEmailWhenCreateWith2ParamsThenFieldsAreSet() {
        // GIVEN
        long id = 1L;
        String email = "user@example.com";

        // WHEN
        UserModel user = new UserModel(id, email);

        // THEN
        assertEquals(id, user.getId());
        assertEquals(email, user.getEmail());
    }

    @Test
    void givenBrandUserDataWhenCreateWith8ParamsThenAllFieldsAreSet() {
        // GIVEN
        String name = "Brand";
        String lastName = "User";
        String email = "brand@example.com";
        Role role = Role.BRAND;
        boolean verified = true;
        boolean status = true;
        String userImg = "http://img.url/user.jpg";
        BrandModel brand = givenBrandModel();

        // WHEN
        UserModel user = new UserModel(name, lastName, email, role, verified, status, userImg, brand);

        // THEN
        thenUserHasBasicFields(user, name, lastName, email);
        assertEquals(role, user.getRole());
        assertTrue(user.isVerified());
        assertTrue(user.isStatus());
        assertEquals(userImg, user.getUserImg());
        assertEquals(brand, user.getBrand());
    }

    @Test
    void givenFullDataWhenCreateWith14ParamsThenAllFieldsAreSet() {
        // GIVEN
        String name = "John";
        String lastName = "Doe";
        String email = "john@example.com";
        String satulation = "Mr.";
        String secondName = "Michael";
        Integer years = 30;
        String password = "hashedPassword";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        Role role = Role.USER;
        boolean verified = true;
        boolean status = true;
        String token = "token123";
        String userImg = "http://img.url/user.jpg";

        // WHEN
        UserModel user = new UserModel(name, lastName, email, satulation, secondName, years, password,
                createdAt, updatedAt, role, verified, status, token, userImg);

        // THEN
        thenUserHasBasicFields(user, name, lastName, email);
        assertEquals(satulation, user.getSatulation());
        assertEquals(secondName, user.getSecondName());
        assertEquals(years, user.getYears());
        assertEquals(password, user.getHashedPassword());
        assertEquals(createdAt, user.getCreatedAt());
        assertEquals(updatedAt, user.getUpdatedAt());
        assertEquals(role, user.getRole());
        assertTrue(user.isVerified());
        assertTrue(user.isStatus());
        assertEquals(token, user.getVerificationToken());
        assertEquals(userImg, user.getUserImg());
    }

    @Test
    void givenFullDataWithBrandWhenCreateWith15ParamsThenAllFieldsAreSet() {
        // GIVEN
        String name = "Brand";
        String lastName = "Owner";
        String email = "owner@example.com";
        String satulation = "Ms.";
        String secondName = "Jane";
        Integer years = 35;
        String password = "hashedPassword";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        Role role = Role.BRAND;
        boolean verified = true;
        boolean status = true;
        String token = "token456";
        String userImg = "http://img.url/brand.jpg";
        BrandModel brand = givenBrandModel();

        // WHEN
        UserModel user = new UserModel(name, lastName, email, satulation, secondName, years, password,
                createdAt, updatedAt, role, verified, status, token, userImg, brand);

        // THEN
        thenUserHasBasicFields(user, name, lastName, email);
        assertEquals(brand, user.getBrand());
        assertEquals(role, user.getRole());
    }

    @Test
    void givenFullDataWithIdWhenCreateWith15ParamsIncludingIdThenAllFieldsAreSet() {
        // GIVEN
        long id = 100L;
        String name = "Admin";
        String lastName = "User";
        String email = "admin@example.com";
        String satulation = "Dr.";
        String secondName = "Robert";
        Integer years = 40;
        String password = "adminPassword";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        Role role = Role.ADMIN;
        boolean verified = true;
        boolean status = true;
        String token = "admin-token";
        String userImg = "http://img.url/admin.jpg";

        // WHEN
        UserModel user = new UserModel(id, name, lastName, email, satulation, secondName, years, password,
                createdAt, updatedAt, role, verified, status, token, userImg);

        // THEN
        assertEquals(id, user.getId());
        thenUserHasBasicFields(user, name, lastName, email);
        assertEquals(role, user.getRole());
    }

    // ========== SETTER/GETTER TESTS ==========

    @Test
    void givenUserWhenSetEmailThenGetReturnsCorrectValue() {
        // GIVEN
        UserModel user = new UserModel();
        String email = "test@example.com";

        // WHEN
        user.setEmail(email);

        // THEN
        assertEquals(email, user.getEmail());
    }

    @Test
    void givenUserWhenSetRoleThenGetReturnsCorrectValue() {
        // GIVEN
        UserModel user = new UserModel();

        // WHEN
        user.setRole(Role.ADMIN);

        // THEN
        assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    void givenUserWhenSetVerifiedThenGetReturnsCorrectValue() {
        // GIVEN
        UserModel user = new UserModel();

        // WHEN
        user.setVerified(true);

        // THEN
        assertTrue(user.isVerified());
    }

    @Test
    void givenUserWhenSetStatusThenGetReturnsCorrectValue() {
        // GIVEN
        UserModel user = new UserModel();

        // WHEN
        user.setStatus(true);

        // THEN
        assertTrue(user.isStatus());
    }

    @Test
    void givenUserWhenSetBrandThenGetReturnsCorrectValue() {
        // GIVEN
        UserModel user = new UserModel();
        BrandModel brand = givenBrandModel();

        // WHEN
        user.setBrand(brand);

        // THEN
        assertEquals(brand, user.getBrand());
    }

    // ========== BUSINESS LOGIC TESTS ==========

    @Test
    void givenRegularUserWhenCheckRoleThenRoleIsUser() {
        // GIVEN
        UserModel user = new UserModel("John", "Doe", "user@example.com", Role.USER, true, true, "http://img.jpg",
                null);

        // WHEN & THEN
        assertEquals(Role.USER, user.getRole());
        assertNull(user.getBrand());
    }

    @Test
    void givenBrandUserWhenCheckRoleThenRoleIsBrand() {
        // GIVEN
        BrandModel brand = givenBrandModel();
        UserModel user = new UserModel("Brand", "Owner", "brand@example.com", Role.BRAND, true, true, "http://img.jpg",
                brand);

        // WHEN & THEN
        assertEquals(Role.BRAND, user.getRole());
        assertNotNull(user.getBrand());
        assertEquals("Nike", user.getBrand().getNombre());
    }

    @Test
    void givenAdminUserWhenCheckRoleThenRoleIsAdmin() {
        // GIVEN
        UserModel user = new UserModel();
        user.setRole(Role.ADMIN);

        // WHEN & THEN
        assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    void givenUnverifiedUserWhenCheckVerifiedThenReturnsFalse() {
        // GIVEN
        UserModel user = new UserModel();
        user.setVerified(false);

        // WHEN & THEN
        assertFalse(user.isVerified());
    }

    @Test
    void givenVerifiedUserWhenCheckVerifiedThenReturnsTrue() {
        // GIVEN
        UserModel user = new UserModel();
        user.setVerified(true);

        // WHEN & THEN
        assertTrue(user.isVerified());
    }

    @Test
    void givenInactiveUserWhenCheckStatusThenReturnsFalse() {
        // GIVEN
        UserModel user = new UserModel();
        user.setStatus(false);

        // WHEN & THEN
        assertFalse(user.isStatus());
    }

    @Test
    void givenActiveUserWhenCheckStatusThenReturnsTrue() {
        // GIVEN
        UserModel user = new UserModel();
        user.setStatus(true);

        // WHEN & THEN
        assertTrue(user.isStatus());
    }

    // ========== HELPER METHODS (GIVEN) ==========

    private BrandModel givenBrandModel() {
        return new BrandModel("NIKE-001", "Nike", "http://logo.url/nike.png");
    }

    // ========== HELPER METHODS (THEN) ==========

    private void thenUserHasBasicFields(UserModel user, String expectedName, String expectedLastName,
            String expectedEmail) {
        assertEquals(expectedName, user.getName());
        assertEquals(expectedLastName, user.getLastName());
        assertEquals(expectedEmail, user.getEmail());
    }
}
