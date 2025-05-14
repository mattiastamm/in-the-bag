package com.discgolf.in_the_bag.controllers;

import com.discgolf.in_the_bag.config.GlobalExceptionHandler;
import com.discgolf.in_the_bag.models.UserDisc;
import com.discgolf.in_the_bag.records.CreateUserDiscRequest;
import com.discgolf.in_the_bag.records.UserDiscDto;
import com.discgolf.in_the_bag.services.UserDiscService;
import com.discgolf.in_the_bag.util.MockDataFactory;
import com.discgolf.in_the_bag.utils.JwtUtil;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserDiscControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserDiscService userDiscService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserDiscController userDiscController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(userDiscController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        lenient().when(jwtUtil.extractUserIdFromRequest(any())).thenReturn(1L);
    }


    // METHOD: getUserDiscs()
    @Test
    void testGetUserDiscs_ReturnsList() throws Exception {
        List<UserDiscDto> mockDiscs = List.of(
                MockDataFactory.createMockUserDiscDto()
        );

        when(userDiscService.getUserDiscs(1L)).thenReturn(mockDiscs);

        mockMvc.perform(get("/api/v1/user-discs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Destroyer"));
    }


    // METHOD: addUserDisc()
    @Test
    void testAddUserDisc_Success() throws Exception {
        CreateUserDiscRequest request = new CreateUserDiscRequest(
                10L, null, "Z-Line", "#00FF00", 175.0,
                5f, 4f, -1f, 1f, "Nice disc"
        );

        UserDisc createdDisc = MockDataFactory.createMockUserDiscFirebird();

        when(userDiscService.addDiscToUser(eq(1L), any())).thenReturn(createdDisc);

        mockMvc.perform(post("/api/v1/user-discs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.color").value("#7f2afe"))
                .andExpect(jsonPath("$.disc.name").value("Firebird"));
    }
    @Test
    void testAddUserDisc_MissingDiscId() throws Exception {
        // discId is null here (invalid)
        CreateUserDiscRequest invalidRequest = new CreateUserDiscRequest(
                null, null, "Z", "#00FF00", 175.0,
                5f, 4f, -1f, 1f, "Oops"
        );

        mockMvc.perform(post("/api/v1/user-discs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Disc ID is required"));
    }
    @Test
    void testAddUserDisc_WeightTooLarge() throws Exception {
        CreateUserDiscRequest invalidRequest = new CreateUserDiscRequest(
                1L, null, "Z", "#00FF00", 1000.0, // Invalid weight
                5f, 4f, -1f, 1f, "Too heavy"
        );

        mockMvc.perform(post("/api/v1/user-discs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Weight must be less than 1000"));
    }



    // METHOD: deleteUserDisc()
    @Test
    void testDeleteUserDisc_Success() throws Exception {
        doNothing().when(userDiscService).deleteUserDisc(1L, 42L);

        mockMvc.perform(delete("/api/v1/user-discs/{userDiscId}", 42))
                .andExpect(status().isNoContent());
    }
    @Test
    void testDeleteUserDisc_NotFound() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "User disc not found"))
                .when(userDiscService).deleteUserDisc(1L, 42L);

        mockMvc.perform(delete("/api/v1/user-discs/{userDiscId}", 42))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User disc not found"));
    }


}

