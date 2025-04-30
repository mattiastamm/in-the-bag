package com.discgolf.in_the_bag.controllers;

import com.discgolf.in_the_bag.config.GlobalExceptionHandler;
import com.discgolf.in_the_bag.records.WishlistAddRequest;
import com.discgolf.in_the_bag.records.WishlistDiscDto;
import com.discgolf.in_the_bag.services.WishlistService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class WishlistControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WishlistService wishlistService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private WishlistController wishlistController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(wishlistController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        lenient().when(jwtUtil.extractUserIdFromRequest(any())).thenReturn(1L);
    }


    // METHOD: getWishlist()
    @Test
    void testGetWishlist_ReturnsDiscs() throws Exception {
        List<WishlistDiscDto> mockWishlist = List.of(
                new WishlistDiscDto(1L, 10L, "Buzzz", "Discraft", 5f, 4f, -1f, 1f, "Midrange", "Stable"),
                new WishlistDiscDto(2L, 11L, "Destroyer", "Innova", 12f, 5f, -1f, 3f, "Driver", "Overstable")
        );

        when(wishlistService.getWishlistDiscs(1L)).thenReturn(mockWishlist);

        mockMvc.perform(get("/api/v1/wishlist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Buzzz"));
    }


    // METHOD: addToWishlist()
    @Test
    void testAddToWishlist_Success() throws Exception {
        WishlistAddRequest request = new WishlistAddRequest(List.of(10L, 11L));

        mockMvc.perform(post("/api/v1/wishlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
    @Test
    void testAddToWishlist_InvalidSuggestionId_Returns400() throws Exception {
        WishlistAddRequest request = new WishlistAddRequest(List.of(999L));

        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "One or more discs are not from the official suggestion list."))
                .when(wishlistService).addToWishlist(eq(1L), any());

        mockMvc.perform(post("/api/v1/wishlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("One or more discs are not from the official suggestion list."));
    }


}