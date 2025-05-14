package com.discgolf.in_the_bag.controllers;

import com.discgolf.in_the_bag.config.GlobalExceptionHandler;
import com.discgolf.in_the_bag.models.Bag;
import com.discgolf.in_the_bag.models.User;
import com.discgolf.in_the_bag.records.BagWithDiscsDto;
import com.discgolf.in_the_bag.records.CreateBagRequest;
import com.discgolf.in_the_bag.services.BagService;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BagControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BagService bagService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private BagController bagController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(bagController)
                .setControllerAdvice(new GlobalExceptionHandler()) // Register custom exception handler
                .build();
        objectMapper = new ObjectMapper();
    }


    // METHOD: getBagsWithDiscs()
    @Test
    void testGetBagsWithDiscs_ReturnsBags() throws Exception {
        Long userId = 1L;
        BagWithDiscsDto mockBags = MockDataFactory.createMockBagWithDiscsDto();

        when(jwtUtil.extractUserIdFromRequest(any())).thenReturn(userId);
        when(bagService.getBagsWithDiscsForUser(userId)).thenReturn(List.of(mockBags));

        mockMvc.perform(get("/api/v1/bags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Mock Bag"))
                .andExpect(jsonPath("$[0].discs.length()").value(1))
                .andExpect(jsonPath("$[0].discs[0].name").value("Destroyer"));
    }


    // METHOD: createBag()
    @Test
    void testCreateBag_Success() throws Exception {
        CreateBagRequest createBagRequest = new CreateBagRequest("New Bag", "My comment");
        User mockUser = MockDataFactory.createMockUser();

        Bag createdBag = new Bag();
        createdBag.setId(1L);
        createdBag.setUser(mockUser);
        createdBag.setTitle("New Bag");
        createdBag.setComment("My comment");

        when(jwtUtil.extractUserIdFromRequest(any())).thenReturn(mockUser.getId());
        when(bagService.createBag(eq(mockUser.getId()), eq("New Bag"), eq("My comment"))).thenReturn(createdBag);

        mockMvc.perform(post("/api/v1/bags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBagRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Bag"))
                .andExpect(jsonPath("$.user.id").value(mockUser.getId()));
    }
    @Test
    void testCreateBag_TitleTooLong() throws Exception {
        // Arrange
        CreateBagRequest invalidRequest = new CreateBagRequest("This title is way too long", "Some comment");

        // Act + Assert
        mockMvc.perform(post("/api/v1/bags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Title must be at most 15 characters"));
    }
    @Test
    void testCreateBag_TitleIsEmpty() throws Exception {
        // Arrange
        CreateBagRequest invalidRequest = new CreateBagRequest("", "Some comment");

        // Act + Assert
        mockMvc.perform(post("/api/v1/bags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Title is required"));
    }


    // METHOD: deleteBag()
    @Test
    void testDeleteBag_Success() throws Exception {
        Long userId = 1L;
        Long bagId = 1L;

        when(jwtUtil.extractUserIdFromRequest(any())).thenReturn(userId);

        mockMvc.perform(delete("/api/v1/bags/{bagId}", bagId))
                .andExpect(status().isOk());
    }
    @Test
    void testDeleteBag_BagNotFound() throws Exception {
        Long userId = 1L;
        Long bagId = 99L;

        when(jwtUtil.extractUserIdFromRequest(any())).thenReturn(userId);
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(bagService).deleteBag(userId, bagId);

        mockMvc.perform(delete("/api/v1/bags/{bagId}", bagId))
                .andExpect(status().isNotFound());
    }
    @Test
    void testDeleteBag_Forbidden() throws Exception {
        Long userId = 1L;
        Long bagId = 2L;

        when(jwtUtil.extractUserIdFromRequest(any())).thenReturn(userId);
        doThrow(new ResponseStatusException(HttpStatus.FORBIDDEN)).when(bagService).deleteBag(userId, bagId);

        mockMvc.perform(delete("/api/v1/bags/{bagId}", bagId))
                .andExpect(status().isForbidden());
    }

}
