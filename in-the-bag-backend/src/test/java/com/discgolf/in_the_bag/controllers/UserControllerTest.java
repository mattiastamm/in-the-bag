package com.discgolf.in_the_bag.controllers;

import com.discgolf.in_the_bag.config.GlobalExceptionHandler;
import com.discgolf.in_the_bag.records.ChangePasswordRequest;
import com.discgolf.in_the_bag.records.UserProfileDto;
import com.discgolf.in_the_bag.services.UserService;
import com.discgolf.in_the_bag.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        lenient().when(jwtUtil.extractUserIdFromRequest(any())).thenReturn(1L);
    }


    // METHOD: getOwnProfile()
    @Test
    void testGetOwnProfile() throws Exception {
        UserProfileDto profile = new UserProfileDto(
                "user@example.com", "2023-01-01", 10, 2, 7, 1, 2, 3, 4
        );

        when(userService.getUserProfile(1L)).thenReturn(profile);

        mockMvc.perform(get("/api/v1/users/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.totalDiscs").value(10));
    }


    // METHOD: changePassword()
    @Test
    void testChangePassword_Success() throws Exception {
        ChangePasswordRequest request = new ChangePasswordRequest("oldpass", "newpass123");

        mockMvc.perform(put("/api/v1/users/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password updated successfully"));
    }
    @Test
    void testChangePassword_MissingCurrentPassword() throws Exception {
        ChangePasswordRequest invalidRequest = new ChangePasswordRequest("", "newpass123");

        mockMvc.perform(put("/api/v1/users/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Current password is required"));
    }
    @Test
    void testChangePassword_NewPasswordTooShort() throws Exception {
        ChangePasswordRequest invalidRequest = new ChangePasswordRequest("oldpass", "123");

        mockMvc.perform(put("/api/v1/users/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("New password must be between 4 and 16 characters"));
    }



}