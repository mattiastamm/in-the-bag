package com.discgolf.in_the_bag.controllers;

import com.discgolf.in_the_bag.config.GlobalExceptionHandler;
import com.discgolf.in_the_bag.records.DiscAutoFillRecord;
import com.discgolf.in_the_bag.records.DiscSearchRecord;
import com.discgolf.in_the_bag.repositories.DiscRepository;
import com.discgolf.in_the_bag.services.DiscService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DiscControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DiscRepository discRepository;

    @Mock
    private DiscService discService;

    @InjectMocks
    private DiscController discController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(discController)
                .setControllerAdvice(new GlobalExceptionHandler()) // Optional for consistent error formatting
                .build();
    }

    @Test
    void testSearchDiscs_ReturnsResults() throws Exception {
        List<DiscSearchRecord> mockResults = List.of(
                new DiscSearchRecord(1L, "Buzzz"),
                new DiscSearchRecord(2L, "Buzzard")
        );

        when(discRepository.searchDiscsByName("buzz")).thenReturn(mockResults);

        mockMvc.perform(get("/api/v1/discs")
                        .param("name", "buzz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Buzzz"));
    }

    @Test
    void testSearchDiscs_EmptyResults() throws Exception {
        when(discRepository.searchDiscsByName("nonexistent")).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/discs")
                        .param("name", "nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testSearchDiscs_MissingNameParam() throws Exception {
        mockMvc.perform(get("/api/v1/discs"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("name parameter is required"));
    }

    @Test
    void testGetDiscDetails_Success() throws Exception {
        Long discId = 1L;
        DiscAutoFillRecord record = new DiscAutoFillRecord(
                discId, "Buzzz", "Midrange", "Discraft",
                5f, 4f, -1f, 1f, List.of()
        );

        when(discService.getDiscDetailsForCreation(discId)).thenReturn(Optional.of(record));

        mockMvc.perform(get("/api/v1/discs/{discId}/details", discId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Buzzz"))
                .andExpect(jsonPath("$.type").value("Midrange"))
                .andExpect(jsonPath("$.manufacturerName").value("Discraft"));
    }

    @Test
    void testGetDiscDetails_NotFound() throws Exception {
        Long discId = 99L;
        when(discService.getDiscDetailsForCreation(discId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/discs/{discId}/details", discId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}

