package com.discgolf.in_the_bag.services;

import com.discgolf.in_the_bag.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private static final String TEST_SECRET = "45LDJSmOtPn7Arg7g2ew7Y3e7cKx19e0a3cTCY7TpMf12vQ3kvToh4IFFNm+4oRd"; // 256-bit dummy

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "jwtSecret", TEST_SECRET);
    }

    @Test
    void testGenerateAndExtractToken() {
        // Arrange
        Long userId = 1L;
        String email = "test@example.com";

        // Act
        String token = jwtService.generateToken(userId, email);

        // Assert
        assertNotNull(token);
        assertEquals(email, jwtService.extractEmail(token));
        assertEquals(userId, jwtService.extractUserId(token));
        assertNotNull(jwtService.extractExpiration(token));
    }

    @Test
    void testIsTokenValid_ReturnsTrue() {
        // Arrange
        Long userId = 1L;
        String email = "test@example.com";
        String token = jwtService.generateToken(userId, email);

        // Act + Assert
        assertTrue(jwtService.isTokenValid(token, email));
    }

    @Test
    void testIsTokenValid_ReturnsFalse_WrongEmail() {
        // Arrange
        Long userId = 1L;
        String correctEmail = "test@example.com";
        String wrongEmail = "wrong@example.com";
        String token = jwtService.generateToken(userId, correctEmail);

        // Act + Assert
        assertFalse(jwtService.isTokenValid(token, wrongEmail));
    }

    @Test
    void testIsTokenExpired_False() {
        // Arrange
        Long userId = 1L;
        String email = "test@example.com";
        String token = jwtService.generateToken(userId, email);

        // Act + Assert
        assertFalse(jwtService.extractExpiration(token).before(new Date())); // basically just isTokenExpired() method
    }
}

