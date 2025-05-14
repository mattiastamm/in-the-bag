package com.discgolf.in_the_bag.services;

import com.discgolf.in_the_bag.models.*;
import com.discgolf.in_the_bag.records.BagWithDiscsDto;
import com.discgolf.in_the_bag.records.UserDiscDto;
import com.discgolf.in_the_bag.repositories.BagRepository;
import com.discgolf.in_the_bag.repositories.DiscInBagRepository;
import com.discgolf.in_the_bag.repositories.UserDiscRepository;
import com.discgolf.in_the_bag.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.discgolf.in_the_bag.util.MockDataFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BagServiceTest {

    @Mock
    private BagRepository bagRepository;
    @Mock
    private DiscInBagRepository discInBagRepository;
    @Mock
    private UserDiscRepository userDiscRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BagService bagService;
    @Mock
    private UserDiscService userDiscService;


    // METHOD: getBagsWithDiscsForUser()
    @Test
    void testGetBagsWithDiscsForUser() {
        // Arrange
        Long userId = 1L;

        // Prepare mock data using your MockDataFactory
        Bag mockBag = MockDataFactory.createMockBag();
        List<UserDisc> userDiscs = new ArrayList<>();
        UserDisc destroyer = MockDataFactory.createMockUserDiscDestroyer();
        userDiscs.add(destroyer);
        when(bagRepository.findByUserId(userId)).thenReturn(List.of(mockBag));
        when(discInBagRepository.findUserDiscsByBagId(mockBag.getId())).thenReturn(userDiscs);

        // Act
        List<BagWithDiscsDto> result = bagService.getBagsWithDiscsForUser(userId);

        // Assert
        assertEquals(1, result.size());

        BagWithDiscsDto bagDto = result.get(0);
        assertEquals(mockBag.getTitle(), bagDto.title());
        assertEquals(mockBag.getComment(), bagDto.comment());
        assertEquals(mockBag.getCreatedAt(), bagDto.createdAt());

        // Verify disc mapping
        assertEquals(1, bagDto.discs().size());
        UserDiscDto discDto = bagDto.discs().get(0);

        UserDisc originalUserDisc = destroyer; // same disc
        assertEquals(originalUserDisc.getDisc().getName(), discDto.name());
        assertEquals(originalUserDisc.getDisc().getType(), discDto.type());
        assertEquals(originalUserDisc.getCustomSpeed(), discDto.customSpeed());
        assertEquals(originalUserDisc.getCustomGlide(), discDto.customGlide());
        assertEquals(originalUserDisc.getCustomTurn(), discDto.customTurn());
        assertEquals(originalUserDisc.getCustomFade(), discDto.customFade());
        assertEquals(originalUserDisc.getColor(), discDto.color());
        assertEquals(originalUserDisc.getPlastic().getId(), discDto.plasticId());
        assertEquals(originalUserDisc.getPlastic().getName(), discDto.plasticName());
        assertEquals(originalUserDisc.getWeight(), discDto.weight());
        assertEquals(originalUserDisc.getInUse(), discDto.inUse());
        assertEquals(originalUserDisc.getComment(), discDto.comment());

        Disc originalDisc = originalUserDisc.getDisc();
        assertEquals(originalDisc.getSpeed(), discDto.speed());
        assertEquals(originalDisc.getGlide(), discDto.glide());
        assertEquals(originalDisc.getTurn(), discDto.turn());
        assertEquals(originalDisc.getFade(), discDto.fade());

        verify(bagRepository, times(1)).findByUserId(userId);
    }
    @Test
    void testGetBagsWithDiscsForUserWithCustomPlastic() {
        // Arrange
        Long userId = 1L;

        // Prepare mock data using your MockDataFactory
        Bag mockBag = MockDataFactory.createMockBag();
        List<UserDisc> userDiscs = new ArrayList<>();
        UserDisc firebird = MockDataFactory.createMockUserDiscFirebird(); // This disc has a custom plastic
        userDiscs.add(firebird);
        when(bagRepository.findByUserId(userId)).thenReturn(List.of(mockBag));
        when(discInBagRepository.findUserDiscsByBagId(mockBag.getId())).thenReturn(userDiscs);

        // Act
        List<BagWithDiscsDto> result = bagService.getBagsWithDiscsForUser(userId);

        // Assert
        assertEquals(1, result.size());

        BagWithDiscsDto bagDto = result.get(0);
        UserDiscDto discDto = bagDto.discs().get(0);

        UserDisc originalUserDisc = firebird; // same disc
        assertNotNull(originalUserDisc.getCustomPlastic());
        assertEquals(originalUserDisc.getCustomPlastic(), discDto.customPlastic());

        verify(bagRepository, times(1)).findByUserId(userId);
    }


    // METHOD: createBag()
    @Test
    void testCreateBag() {
        // Arrange
        User mockUser = MockDataFactory.createMockUser();
        String title = "Tournament Bag";
        String comment = "For Tournament play only";
        Bag savedBag = MockDataFactory.createMockBag();
        savedBag.setTitle(title);
        savedBag.setComment(comment);

        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        when(bagRepository.findByUserId(mockUser.getId())).thenReturn(Collections.emptyList());
        when(bagRepository.save(any(Bag.class))).thenReturn(savedBag);

        // Act
        Bag result = bagService.createBag(mockUser.getId(), savedBag.getTitle(), savedBag.getComment());

        // Assert
        assertNotNull(result);
        assertEquals(title, result.getTitle());
        assertEquals(comment, result.getComment());
        assertEquals(mockUser.getId(), result.getUser().getId());

        verify(bagRepository, times(1)).findByUserId(mockUser.getId());
        verify(bagRepository, times(1)).save(any(Bag.class));
    }
    @Test
    void testCreateBag_WhenUserHasFiveBags() {
        // Arrange
        User mockUser = MockDataFactory.createMockUser();

        // 5 mock bags
        List<Bag> existingBags = List.of(
                MockDataFactory.createMockBag(),
                MockDataFactory.createMockBag(),
                MockDataFactory.createMockBag(),
                MockDataFactory.createMockBag(),
                MockDataFactory.createMockBag()
        );

        when(bagRepository.findByUserId(mockUser.getId())).thenReturn(existingBags);
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> bagService.createBag(mockUser.getId(), "Overflow Bag", "Should not allow")
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Maximum of 5 bags allowed per user", exception.getReason());

        verify(bagRepository, times(1)).findByUserId(mockUser.getId());
        verify(bagRepository, never()).save(any(Bag.class));
    }


    // METHOD: removeDiscFromBag()
    @Test
    void testRemoveDiscFromBag_DiscStillInOtherBags() {
        // Arrange
        Long userId = 1L;
        Long bagId = 1L;
        Long userDiscId = 1L;
        Bag mockBag = MockDataFactory.createMockBag();
        UserDisc mockUserDisc = MockDataFactory.createMockUserDiscDestroyer();

        when(userDiscService.validateUserDiscOwnershipAndReturn(userId, userDiscId)).thenReturn(mockUserDisc);
        when(bagRepository.findById(bagId)).thenReturn(Optional.of(mockBag));
        when(discInBagRepository.existsByUserDisc_IdAndBag_Id(userDiscId, bagId)).thenReturn(true);
        when(discInBagRepository.existsByUserDisc_Id(userDiscId)).thenReturn(true); // Still in other bags

        // Act
        bagService.removeDiscFromBag(userId, userDiscId, bagId);

        // Assert
        verify(userDiscService, times(1)).validateUserDiscOwnershipAndReturn(userId, userDiscId);
        verify(bagRepository, times(1)).findById(bagId);
        verify(discInBagRepository, times(1)).deleteByUserDisc_IdAndBag_Id(userDiscId, bagId);
        verify(userDiscRepository, never()).save(any()); // Should not save if still in other bags
    }
    @Test
    void testRemoveDiscFromBag_UserDiscOwnershipMismatch() {
        // Arrange
        Long userId = 1L;
        Long wrongUserDiscId = 99L;
        Long bagId = 1L;

        doThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "UserDisc does not belong to this user"))
                .when(userDiscService).validateUserDiscOwnershipAndReturn(userId, wrongUserDiscId);

        // Act + Assert
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> bagService.removeDiscFromBag(userId, wrongUserDiscId, bagId)
        );

        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
        verify(discInBagRepository, never()).deleteByUserDisc_IdAndBag_Id(any(), any());
    }
    @Test
    void testRemoveDiscFromBag_BagOwnershipMismatch() {
        // Arrange
        Long userId = 1L;
        Long userDiscId = 1L;
        Long wrongBagId = 99L;
        User differentUser = new User();
        differentUser.setId(2L);

        // Simulate that bag userId doesn't match
        Bag wrongBag = MockDataFactory.createMockBag();
        wrongBag.setUser(differentUser); // Different user

        when(bagRepository.findById(wrongBagId)).thenReturn(Optional.of(wrongBag));

        // Act + Assert
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> bagService.removeDiscFromBag(userId, userDiscId, wrongBagId)
        );

        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
    }
    @Test
    void testRemoveDiscFromBag_DiscNotInBag() {
        // Arrange
        Long userId = 1L;
        Long userDiscId = 1L;
        Long bagId = 1L;
        Bag mockBag = MockDataFactory.createMockBag();
        UserDisc mockUserDisc = MockDataFactory.createMockUserDiscDestroyer();

        when(userDiscService.validateUserDiscOwnershipAndReturn(userId, userDiscId)).thenReturn(mockUserDisc);
        when(bagRepository.findById(bagId)).thenReturn(Optional.of(mockBag));
        when(discInBagRepository.existsByUserDisc_IdAndBag_Id(userDiscId, bagId)).thenReturn(false);

        // Act + Assert
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> bagService.removeDiscFromBag(userId, userDiscId, bagId)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        verify(discInBagRepository, never()).deleteByUserDisc_IdAndBag_Id(any(), any());
    }
    @Test
    void testRemoveDiscFromBag_DiscNotInOtherBags() {
        // Arrange
        Long userId = 1L;
        Long userDiscId = 1L;
        Long bagId = 1L;
        Bag mockBag = MockDataFactory.createMockBag();
        UserDisc mockUserDisc = MockDataFactory.createMockUserDiscDestroyer();

        when(userDiscService.validateUserDiscOwnershipAndReturn(userId, userDiscId)).thenReturn(mockUserDisc);
        when(bagRepository.findById(bagId)).thenReturn(Optional.of(mockBag));
        when(discInBagRepository.existsByUserDisc_IdAndBag_Id(userDiscId, bagId)).thenReturn(true);
        when(discInBagRepository.existsByUserDisc_Id(userDiscId)).thenReturn(false);

        // Act
        bagService.removeDiscFromBag(userId, userDiscId, bagId);

        // Assert
        verify(userDiscRepository, times(1)).save(argThat(d -> !d.getInUse())); // Verify inUse = false
    }


    // METHOD: updateBagDiscs()
    @Test
    void testUpdateBagDiscs_HappyFlow() {
        // Arrange
        Long userId = 1L;
        Long bagId = 1L;

        List<Long> currentDiscIds = List.of(1L, 2L, 4L, 6L);   // Discs currently in bag
        List<Long> updatedUserDiscIds = List.of(1L, 3L, 5L);    // New selection

        Bag mockBag = MockDataFactory.createMockBag();

        when(bagRepository.findById(bagId)).thenReturn(Optional.of(mockBag));
        when(discInBagRepository.findUserDiscIdsByBagId(bagId)).thenReturn(currentDiscIds);

        // Mock the userDiscs that will be fetched by ID when adding/removing
        when(userDiscRepository.findById(3L)).thenReturn(Optional.of(MockDataFactory.createMockUserDiscFirebird()));
        when(userDiscRepository.findById(5L)).thenReturn(Optional.of(MockDataFactory.createMockUserDiscDestroyer()));
        when(userDiscRepository.findById(2L)).thenReturn(Optional.of(MockDataFactory.createMockUserDiscDestroyer()));
        when(userDiscRepository.findById(4L)).thenReturn(Optional.of(MockDataFactory.createMockUserDiscDestroyer()));
        when(userDiscRepository.findById(6L)).thenReturn(Optional.of(MockDataFactory.createMockUserDiscDestroyer()));

        when(discInBagRepository.existsByUserDisc_Id(anyLong())).thenReturn(false);

        // Act
        bagService.updateBagDiscs(userId, bagId, updatedUserDiscIds);

        // Assert
        verify(bagRepository, times(1)).findById(bagId);
        verify(discInBagRepository, times(1)).findUserDiscIdsByBagId(bagId);

        // Verify saves for new discs (3 and 5)
        verify(discInBagRepository, times(2)).save(any(DiscInBag.class));

        // Verify deletes for removed discs (2, 4, 6)
        verify(discInBagRepository, times(3)).deleteByUserDiscAndBag(any(UserDisc.class), eq(mockBag));

        // Verify userDiscService is updating inUse status for all affected discs
        verify(userDiscService, times(5)).setInUseStatus(anyLong(), eq(false));
    }


    // METHOD: deleteBag()
    @Test
    void testDeleteBag_HappyFlow() {
        // Arrange
        Long userId = 1L;
        Long bagId = 1L;

        // Mock the bag exists and belongs to the user
        Bag mockBag = MockDataFactory.createMockBag();
        when(bagRepository.findById(bagId)).thenReturn(Optional.of(mockBag));

        // Act
        bagService.deleteBag(userId, bagId);

        // Verify that bag ownership was checked
        verify(bagRepository, times(1)).findById(bagId);

        // Verify that all disc links were deleted
        verify(discInBagRepository, times(1)).deleteByBag_Id(bagId);

        // Verify that the bag itself was deleted
        verify(bagRepository, times(1)).deleteById(bagId);
    }


    // Validation Methods:
    // METHOD: validateBagOwnershipAndReturn()
    @Test
    void testValidateBagOwnershipAndReturn_HappyFlow() {
        // Arrange
        Long userId = 1L;
        Long bagId = 1L;
        Bag mockBag = MockDataFactory.createMockBag();
        when(bagRepository.findById(bagId)).thenReturn(Optional.of(mockBag));

        // Act
        Bag result = bagService.validateBagOwnershipAndReturn(userId, bagId);

        // Assert
        assertNotNull(result);
        assertEquals(mockBag, result);
    }
    @Test
    void testValidateBagOwnershipAndReturn_BagNotFound() {
        // Arrange
        Long userId = 1L;
        Long bagId = 1L;
        when(bagRepository.findById(bagId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> bagService.validateBagOwnershipAndReturn(userId, bagId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
    @Test
    void testValidateBagOwnershipAndReturn_UserMismatch_ThrowsForbidden() {
        // Arrange
        Long userId = 1L;
        Long bagId = 1L;
        Bag mockBag = MockDataFactory.createMockBag();
        User differentUser = new User();
        differentUser.setId(2L);
        mockBag.setUser(differentUser); // Wrong user ID
        when(bagRepository.findById(bagId)).thenReturn(Optional.of(mockBag));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> bagService.validateBagOwnershipAndReturn(userId, bagId));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }


}

