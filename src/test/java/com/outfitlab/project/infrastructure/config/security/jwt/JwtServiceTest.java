package com.outfitlab.project.infrastructure.config.security.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private static final String TEST_SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final Long TOKEN_EXPIRATION = 86400000L; // 24 hours
    private static final Long REFRESH_EXPIRATION = 604800000L; // 7 days

    @BeforeEach
    void setUp() {
        // GIVEN - Set up test configuration values
        ReflectionTestUtils.setField(jwtService, "jwtSecret", TEST_SECRET);
        ReflectionTestUtils.setField(jwtService, "tokenExpiration", TOKEN_EXPIRATION);
        ReflectionTestUtils.setField(jwtService, "refreshExpiration", REFRESH_EXPIRATION);
    }

    // ========== TOKEN EXTRACTION TESTS ==========

    @Test
    void givenRequestWithBearerTokenWhenExtractTokenThenReturnToken() {
        // GIVEN
        HttpServletRequest request = mock(HttpServletRequest.class);
        String expectedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + expectedToken);

        // WHEN
        String actualToken = jwtService.extractToken(request);

        // THEN
        assertEquals(expectedToken, actualToken);
    }

    @Test
    void givenRequestWithoutAuthHeaderWhenExtractTokenThenReturnNull() {
        // GIVEN
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);

        // WHEN
        String token = jwtService.extractToken(request);

        // THEN
        assertNull(token);
    }

    @Test
    void givenRequestWithInvalidAuthHeaderWhenExtractTokenThenReturnNull() {
        // GIVEN
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader token");

        // WHEN
        String token = jwtService.extractToken(request);

        // THEN
        assertNull(token);
    }

    // ========== TOKEN GENERATION TESTS ==========

    @Test
    void givenUserDetailsWhenGenerateTokenThenReturnValidToken() {
        // GIVEN
        UserDetails userDetails = givenUserDetails("test@example.com");

        // WHEN
        String token = jwtService.generateToken(userDetails);

        // THEN
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts
    }

    @Test
    void givenUserDetailsWithExtraClaimsWhenGenerateTokenThenTokenContainsClaims() {
        // GIVEN
        UserDetails userDetails = givenUserDetails("test@example.com");
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "USER");
        extraClaims.put("userId", 123L);

        // WHEN
        String token = jwtService.generateToken(extraClaims, userDetails);

        // THEN
        assertNotNull(token);
        Claims claims = jwtService.getAllClaims(token);
        assertEquals("USER", claims.get("role"));
        assertEquals(123, claims.get("userId"));
    }

    @Test
    void givenUserDetailsWhenGenerateRefreshTokenThenReturnValidToken() {
        // GIVEN
        UserDetails userDetails = givenUserDetails("test@example.com");

        // WHEN
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        // THEN
        assertNotNull(refreshToken);
        assertFalse(refreshToken.isEmpty());
        assertTrue(refreshToken.split("\\.").length == 3);
    }

    // ========== USERNAME EXTRACTION TESTS ==========

    @Test
    void givenValidTokenWhenExtractUsernameThenReturnCorrectUsername() {
        // GIVEN
        UserDetails userDetails = givenUserDetails("test@example.com");
        String token = jwtService.generateToken(userDetails);

        // WHEN
        String username = jwtService.extractUsername(token);

        // THEN
        assertEquals("test@example.com", username);
    }

    // ========== EXPIRATION TESTS ==========

    @Test
    void givenValidTokenWhenExtractExpirationThenReturnFutureDate() {
        // GIVEN
        UserDetails userDetails = givenUserDetails("test@example.com");
        String token = jwtService.generateToken(userDetails);

        // WHEN
        Date expiration = jwtService.extractExpiration(token);

        // THEN
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void givenValidTokenWhenCheckIsExpiredThenReturnFalse() {
        // GIVEN
        UserDetails userDetails = givenUserDetails("test@example.com");
        String token = jwtService.generateToken(userDetails);

        // WHEN
        Boolean isExpired = jwtService.isTokenExpired(token);

        // THEN
        assertFalse(isExpired);
    }

    @Test
    void givenExpiredTokenWhenCheckIsExpiredThenThrowsException() {
        // GIVEN
        UserDetails userDetails = givenUserDetails("test@example.com");
        // Create token with negative expiration (already expired)
        String expiredToken = jwtService.buildToken(new HashMap<>(), userDetails, -1000L);

        // WHEN & THEN
        // JWT library throws ExpiredJwtException when trying to parse expired token
        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> {
            jwtService.isTokenExpired(expiredToken);
        });
    }

    // ========== TOKEN VALIDATION TESTS ==========

    @Test
    void givenValidTokenAndMatchingUserWhenValidateThenReturnTrue() {
        // GIVEN
        UserDetails userDetails = givenUserDetails("test@example.com");
        String token = jwtService.generateToken(userDetails);

        // WHEN
        Boolean isValid = jwtService.isTokenValid(token, userDetails);

        // THEN
        assertTrue(isValid);
    }

    @Test
    void givenValidTokenAndDifferentUserWhenValidateThenReturnFalse() {
        // GIVEN
        UserDetails userDetails1 = givenUserDetails("test1@example.com");
        UserDetails userDetails2 = givenUserDetails("test2@example.com");
        String token = jwtService.generateToken(userDetails1);

        // WHEN
        Boolean isValid = jwtService.isTokenValid(token, userDetails2);

        // THEN
        assertFalse(isValid);
    }

    // ========== CLAIMS EXTRACTION TESTS ==========

    @Test
    void givenValidTokenWhenGetAllClaimsThenReturnClaims() {
        // GIVEN
        UserDetails userDetails = givenUserDetails("test@example.com");
        String token = jwtService.generateToken(userDetails);

        // WHEN
        Claims claims = jwtService.getAllClaims(token);

        // THEN
        assertNotNull(claims);
        assertEquals("test@example.com", claims.getSubject());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    @Test
    void givenValidTokenWhenExtractCustomClaimThenReturnCorrectValue() {
        // GIVEN
        UserDetails userDetails = givenUserDetails("test@example.com");
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("customClaim", "customValue");
        String token = jwtService.generateToken(extraClaims, userDetails);

        // WHEN
        String customValue = jwtService.extractClaim(token, claims -> claims.get("customClaim", String.class));

        // THEN
        assertEquals("customValue", customValue);
    }

    // ========== HELPER METHODS (GIVEN) ==========

    private UserDetails givenUserDetails(String username) {
        return User.builder()
                .username(username)
                .password("password")
                .authorities(Collections.emptyList())
                .build();
    }
}
