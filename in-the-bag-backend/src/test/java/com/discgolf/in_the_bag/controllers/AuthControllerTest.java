package com.discgolf.in_the_bag.controllers;

import com.discgolf.in_the_bag.config.GlobalExceptionHandler;
import com.discgolf.in_the_bag.records.LoginRequest;
import com.discgolf.in_the_bag.records.LoginResponse;
import com.discgolf.in_the_bag.records.SignupRequest;
import com.discgolf.in_the_bag.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler()) // Add your handler here
                .build();
    }


    // METHOD: login()
    @Test
    void testLogin_Success() throws Exception {
        LoginRequest request = new LoginRequest("user@example.com", "securePassword");
        LoginResponse response = new LoginResponse("jwt-token", 1L);

        when(userService.authenticateUser(request)).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.userId").value(1L));
    }
    @Test
    void testLogin_MissingPassword() throws Exception {
        LoginRequest request = new LoginRequest("user@example.com", "");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Password is required"));
    }


    // METHOD: signup()
    @Test
    void testSignup_Success() throws Exception {
        SignupRequest request = new SignupRequest("user@example.com", "strongPass");
        LoginResponse response = new LoginResponse("jwt-token", 2L);

        when(userService.signup(request)).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.userId").value(2L));
    }
    @Test
    void testSignup_InvalidEmailFormat() throws Exception {
        SignupRequest request = new SignupRequest("invalid-email", "password123");

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Must be a valid email"));
    }
    @Test
    void testSignup_EmailAlreadyInUse() throws Exception {
        SignupRequest request = new SignupRequest("used@example.com", "somePass");

        when(userService.signup(request)).thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Email is already in use."));

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email is already in use."));
    }
    @Test
    void testSignup_PasswordTooShort() throws Exception {
        SignupRequest request = new SignupRequest("user@example.com", "123");

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Password must be between 4 and 16 characters"));
    }

}
