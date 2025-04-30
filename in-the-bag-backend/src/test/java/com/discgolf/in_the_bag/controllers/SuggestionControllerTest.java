package com.discgolf.in_the_bag.controllers;

import com.discgolf.in_the_bag.config.GlobalExceptionHandler;
import com.discgolf.in_the_bag.suggestions.BagSuggestionDto;
import com.discgolf.in_the_bag.suggestions.DiscSuggestionDto;
import com.discgolf.in_the_bag.suggestions.SuggestionController;
import com.discgolf.in_the_bag.suggestions.SuggestionService;
import com.discgolf.in_the_bag.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SuggestionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SuggestionService suggestionService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private SuggestionController suggestionController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(suggestionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        lenient().when(jwtUtil.extractUserIdFromRequest(any())).thenReturn(1L);
    }


    // METHOD: suggestForUser()
    @Test
    void testSuggestForUser_ReturnsSuggestions() throws Exception {
        Long bagId = 42L;

        List<DiscSuggestionDto> discs = List.of(
                new DiscSuggestionDto(1L, "Buzzz", "Discraft", 5f, 4f, -1f, 1f),
                new DiscSuggestionDto(2L, "Zone", "Discraft", 4f, 3f, 0f, 3f)
        );

        List<BagSuggestionDto> suggestions = List.of(
                new BagSuggestionDto("Stable Midranges", discs)
        );

        when(suggestionService.suggestForUser(1L, bagId)).thenReturn(suggestions);

        mockMvc.perform(get("/api/v1/suggestions/{bagId}", bagId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].categoryTitle").value("Stable Midranges"))
                .andExpect(jsonPath("$[0].discSuggestionDtos.length()").value(2))
                .andExpect(jsonPath("$[0].discSuggestionDtos[0].name").value("Buzzz"));
    }
    @Test
    void testSuggestForUser_BagNotOwned_Returns403() throws Exception {
        Long bagId = 999L;

        doThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this bag"))
                .when(suggestionService).suggestForUser(1L, bagId);

        mockMvc.perform(get("/api/v1/suggestions/{bagId}", bagId))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("You do not own this bag"));
    }


}