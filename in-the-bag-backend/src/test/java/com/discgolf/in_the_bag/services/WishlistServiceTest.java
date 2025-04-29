package com.discgolf.in_the_bag.services;

import com.discgolf.in_the_bag.records.WishlistAddRequest;
import com.discgolf.in_the_bag.records.WishlistDiscDto;
import com.discgolf.in_the_bag.repositories.SuggestionRepository;
import com.discgolf.in_the_bag.repositories.WishlistRepository;
import com.discgolf.in_the_bag.util.MockDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WishlistServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private SuggestionRepository suggestionRepository;

    @InjectMocks
    private WishlistService wishlistService;


    // METHOD: getWishlistDiscs
    @Test
    void testGetWishlistDiscs_ReturnsDiscs() {
        // Arrange
        List<WishlistDiscDto> mockWishlistDiscs = List.of(MockDataFactory.createMockWishlistDiscDto());
        when(wishlistRepository.findWishlistDiscsByUserId(1L)).thenReturn(mockWishlistDiscs);

        // Act
        List<WishlistDiscDto> result = wishlistService.getWishlistDiscs(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Destroyer", result.get(0).name());
        verify(wishlistRepository, times(1)).findWishlistDiscsByUserId(1L);
    }


    // METHOD: addToWishlist()
    @Test
    void testAddToWishlist_SuccessfullyAddsNewSuggestions() {
        // Arrange
        WishlistAddRequest addRequest = new WishlistAddRequest(List.of(10L, 20L));

        // Mock suggestion IDs
        when(suggestionRepository.findAllIds()).thenReturn(Set.of(10L, 20L, 30L));
        when(wishlistRepository.findAllSuggestionIdsByUserId(1L)).thenReturn(Set.of(10L)); // 10 already added
        when(suggestionRepository.findAllById(List.of(20L))).thenReturn(List.of(MockDataFactory.createMockSuggestion(20L)));

        // Act
        wishlistService.addToWishlist(1L, addRequest);

        // Assert
        verify(wishlistRepository, times(1)).saveAll(anyList());
    }
    @Test
    void testAddToWishlist_AllSuggestionsAlreadyExist() {
        // Arrange
        WishlistAddRequest addRequest = new WishlistAddRequest(List.of(10L, 20L));

        when(suggestionRepository.findAllIds()).thenReturn(Set.of(10L, 20L, 30L));
        when(wishlistRepository.findAllSuggestionIdsByUserId(1L)).thenReturn(Set.of(10L, 20L)); // All exist already

        // Act
        wishlistService.addToWishlist(1L, addRequest);

        // Assert
        verify(wishlistRepository, never()).saveAll(anyList());
    }
    @Test
    void testAddToWishlist_InvalidSuggestionIds() {
        // Arrange
        WishlistAddRequest addRequest = new WishlistAddRequest(List.of(10L, 99L)); // 99 invalid
        when(suggestionRepository.findAllIds()).thenReturn(Set.of(10L, 20L, 30L));

        // Act + Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> wishlistService.addToWishlist(1L, addRequest)
        );
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("One or more discs are not from the official suggestion list.", exception.getReason());
    }


    // METHOD: removeFromWishlist()
    @Test
    void testRemoveFromWishlist_Success() {
        // Arrange
        when(wishlistRepository.existsByUserIdAndSuggestionId(1L, 10L)).thenReturn(true);

        // Act
        wishlistService.removeFromWishlist(1L, 10L);

        // Assert
        verify(wishlistRepository, times(1)).deleteByUserIdAndSuggestionId(1L, 10L);
    }
    @Test
    void testRemoveFromWishlist_WhenNotFound() {
        // Arrange
        when(wishlistRepository.existsByUserIdAndSuggestionId(1L, 10L)).thenReturn(false);

        // Act + Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> wishlistService.removeFromWishlist(1L, 10L)
        );
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Wishlist entry for suggestionId=" + 10L + " and userId=" + 1L + " does not exist.", exception.getReason());
    }
}


