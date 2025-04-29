package com.discgolf.in_the_bag.services;

import com.discgolf.in_the_bag.models.UserDisc;
import com.discgolf.in_the_bag.repositories.DiscInBagRepository;
import com.discgolf.in_the_bag.suggestions.*;
import com.discgolf.in_the_bag.util.MockDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SuggestionServiceTest {

    @Mock
    private DiscInBagRepository discInBagRepository;

    @Mock
    private BagService bagService;

    @Mock
    private SuggestionEngine suggestionEngine;

    @InjectMocks
    private SuggestionService suggestionService;


    // METHOD: suggestForUser()
    @Test
    void testSuggestForUser_Success() {
        // Arrange
        Long userId = 1L;
        Long bagId = 1L;

        List<UserDisc> mockUserDiscs = List.of(
                MockDataFactory.createMockUserDiscDestroyer(),
                MockDataFactory.createMockUserDiscFirebird()
        );

        List<BagSuggestionInputDto> mockInputs = mockUserDiscs.stream()
                .map(BagSuggestionInputDto::from)
                .toList();

        List<BagSuggestionDto> mockSuggestions = List.of(
                new BagSuggestionDto("Distance Driver", List.of(
                        new DiscSuggestionDto(101L, "Wraith", "Innova", 11f, 5f, -1f, 3f),
                        new DiscSuggestionDto(102L, "Boss", "Innova", 13f, 5f, -1f, 3f)
                ))
        );

        when(discInBagRepository.findUserDiscsByBagId(bagId)).thenReturn(mockUserDiscs);
        when(suggestionEngine.suggestDiscs(mockInputs)).thenReturn(mockSuggestions);

        // Act
        List<BagSuggestionDto> result = suggestionService.suggestForUser(userId, bagId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Distance Driver", result.get(0).categoryTitle());
        assertEquals(2, result.get(0).discSuggestionDtos().size());

        // ✅ Verify interactions
        verify(bagService, times(1)).validateBagOwnership(userId, bagId);
        verify(discInBagRepository, times(1)).findUserDiscsByBagId(bagId);
        verify(suggestionEngine, times(1)).suggestDiscs(mockInputs);
    }
    @Test
    void testSuggestForUser_BagNotFound() {
        // Arrange
        Long userId = 1L;
        Long bagId = 1L;

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Bag not found"))
                .when(bagService).validateBagOwnership(userId, bagId);

        // Act + Assert
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> suggestionService.suggestForUser(userId, bagId)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        verify(bagService, times(1)).validateBagOwnership(userId, bagId);
        verifyNoInteractions(discInBagRepository);
        verifyNoInteractions(suggestionEngine);
    }
    @Test
    void testSuggestForUser_BagForbidden() {
        // Arrange
        Long userId = 1L;
        Long bagId = 1L;

        doThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden"))
                .when(bagService).validateBagOwnership(userId, bagId);

        // Act + Assert
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> suggestionService.suggestForUser(userId, bagId)
        );

        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
        verify(bagService, times(1)).validateBagOwnership(userId, bagId);
        verifyNoInteractions(discInBagRepository);
        verifyNoInteractions(suggestionEngine);
    }

}

