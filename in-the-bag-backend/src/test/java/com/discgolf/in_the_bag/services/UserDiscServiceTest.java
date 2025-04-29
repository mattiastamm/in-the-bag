package com.discgolf.in_the_bag.services;

import com.discgolf.in_the_bag.models.*;
import com.discgolf.in_the_bag.records.*;
import com.discgolf.in_the_bag.repositories.*;
import com.discgolf.in_the_bag.util.MockDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDiscServiceTest {

    @Mock private UserDiscRepository userDiscRepository;
    @Mock private BagRepository bagRepository;
    @Mock private DiscRepository discRepository;
    @Mock private PlasticRepository plasticRepository;

    @InjectMocks
    private UserDiscService userDiscService;


    // METHOD: getUserDiscs()
    @Test
    void testGetUserDiscs() {
        // Arrange
        Long userId = 1L;
        when(userDiscRepository.findUserDiscsByUserId(userId)).thenReturn(List.of());

        // Act
        List<UserDiscDto> result = userDiscService.getUserDiscs(userId);

        // Assert
        assertNotNull(result);
        verify(userDiscRepository).findUserDiscsByUserId(userId);
    }


    // METHOD: getDiscDetails()
    @Test
    void testGetDiscDetails() {
        // Arrange
        Long userId = 1L;
        Long userDiscId = 1L;
        UserDisc mockUserDisc = MockDataFactory.createMockUserDiscDestroyer();

        Bag bag = MockDataFactory.createMockBagWithDiscs();
        List<BagRecord> bags = new ArrayList<>();
        BagRecord bagRecord = new BagRecord(bag.getId(), bag.getTitle(), bag.getComment());
        bags.add(bagRecord);

        Plastic plastic = MockDataFactory.createMockPlastic();
        List<PlasticRecord> plastics = new ArrayList<>();
        PlasticRecord plasticRecord = new PlasticRecord(plastic.getId(), plastic.getName());
        plastics.add(plasticRecord);

        when(userDiscRepository.findDiscEntityByUserDiscId(userDiscId)).thenReturn(Optional.of(mockUserDisc));
        when(userDiscRepository.findUserDiscsById(userDiscId)).thenReturn(Optional.of(
                new UserDiscDto(1L, "Destroyer", "Driver", 12f, 5f, -1f, 3f, "#000000", 1, "Star", null, "Innova", 12f, 5f, -1f, 3f, 173d, true, "Good disc!")
        ));
        when(bagRepository.findBagsByUserDiscId(userDiscId)).thenReturn(bags);
        when(userDiscRepository.findPlasticsByUserDiscId(userDiscId)).thenReturn(plastics);

        // Act
        DiscDetailsRecord result = userDiscService.getDiscDetails(userId, userDiscId);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.userDiscId());
        assertEquals(bags, result.bags());
        assertEquals(plastics, result.availablePlastics());
    }
    @Test
    void testGetDiscDetails_WhenDiscNotFound() {
        // Arrange
        Long userId = 1L;
        Long userDiscId = 1L;
        UserDisc mockUserDisc = MockDataFactory.createMockUserDiscDestroyer();

        when(userDiscRepository.findDiscEntityByUserDiscId(userDiscId)).thenReturn(Optional.of(mockUserDisc));
        when(userDiscRepository.findUserDiscsById(userDiscId)).thenReturn(Optional.empty());

        // Act + Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userDiscService.getDiscDetails(userId, userDiscId)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("UserDisc not found: " + userDiscId, exception.getReason());

        // Verify that validate ownership was called
        verify(userDiscRepository, times(1)).findDiscEntityByUserDiscId(userDiscId);

        // Verify that it attempted to find the disc details
        verify(userDiscRepository, times(1)).findUserDiscsById(userDiscId);

        // Verify that bagRepository and plastics weren't even called (optional)
        verify(bagRepository, never()).findBagsByUserDiscId(anyLong());
        verify(userDiscRepository, never()).findPlasticsByUserDiscId(anyLong());
    }


    // METHOD: addDiscToUser()
    @Test
    void testAddDiscToUser_HappyFlow() {
        // Arrange
        Long userId = 1L;
        CreateUserDiscRequest request = new CreateUserDiscRequest(
                1L, // discId
                1L, // plasticId
                null, // customPlastic
                "#FFFFFF",
                175.0,
                12f, 5f, -1f, 3f,
                "Great disc!"
        );
        Plastic mockPlastic = MockDataFactory.createMockPlastic();

        when(discRepository.findById(1L)).thenReturn(Optional.of(MockDataFactory.createMockDiscDestroyer()));
        when(plasticRepository.findById(1L)).thenReturn(Optional.of(mockPlastic));
        when(userDiscRepository.save(any(UserDisc.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserDisc result = userDiscService.addDiscToUser(userId, request);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(175.0, result.getWeight());
        assertEquals("#FFFFFF", result.getColor());
        assertNull(result.getCustomPlastic());
        assertEquals(mockPlastic.getId(), result.getPlastic().getId());

        verify(discRepository).findById(1L);
        verify(plasticRepository).findById(1L);
        verify(userDiscRepository).save(any(UserDisc.class));
    }
    @Test
    void testAddDiscToUser_DiscNotFound() {
        // Arrange
        CreateUserDiscRequest request = new CreateUserDiscRequest(
                99L, 1L, null, "#FFFFFF", 175.0, 12f, 5f, -1f, 3f, "Great disc!"
        );

        when(discRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userDiscService.addDiscToUser(1L, request)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        verify(discRepository).findById(99L);
    }
    @Test
    void testAddDiscToUser_BothPlasticIdAndCustomPlasticProvided() {
        // Arrange
        CreateUserDiscRequest request = new CreateUserDiscRequest(
                1L, 1L, "Custom Plastic", "#FFFFFF", 175.0, 12f, 5f, -1f, 3f, "Great disc!"
        );

        when(discRepository.findById(1L)).thenReturn(Optional.of(MockDataFactory.createMockDiscDestroyer()));

        // Act & Assert
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userDiscService.addDiscToUser(1L, request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("Only one of plasticId or customPlastic should be provided.", ex.getReason());
        verify(discRepository).findById(1L);
    }
    @Test
    void testAddDiscToUser_NoPlasticProvided() {
        // Arrange
        CreateUserDiscRequest request = new CreateUserDiscRequest(
                1L, null, null, "#FFFFFF", 175.0, 12f, 5f, -1f, 3f, "Great disc!"
        );

        when(discRepository.findById(1L)).thenReturn(Optional.of(MockDataFactory.createMockDiscDestroyer()));

        // Act & Assert
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userDiscService.addDiscToUser(1L, request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("You must provide either a plasticId or a customPlastic.", ex.getReason());
        verify(discRepository).findById(1L);
    }
    @Test
    void testAddDiscToUser_PlasticIdProvidedButNotFound() {
        // Arrange
        CreateUserDiscRequest request = new CreateUserDiscRequest(
                1L, 2L, null, "#FFFFFF", 175.0, 12f, 5f, -1f, 3f, "Great disc!"
        );

        when(discRepository.findById(1L)).thenReturn(Optional.of(MockDataFactory.createMockDiscDestroyer()));
        when(plasticRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userDiscService.addDiscToUser(1L, request)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Plastic not found: " + request.plasticId(), ex.getReason());
        verify(discRepository).findById(1L);
        verify(plasticRepository).findById(2L);
    }


    // METHOD: updateUserDisc()
    @Test
    void testUpdateUserDisc_HappyFlow() {
        // Arrange
        Long userId = 1L;
        Long userDiscId = 1L;
        UserDisc existingUserDisc = MockDataFactory.createMockUserDiscDestroyer();
        Plastic mockPlastic = MockDataFactory.createMockPlastic();
        UpdateDiscRequest request = new UpdateDiscRequest(
                10f, 5f, -1f, 3f, "#123456", mockPlastic.getId().longValue(), null, 173.0, "Updated comment"
        );

        when(userDiscRepository.findDiscEntityByUserDiscId(userDiscId)).thenReturn(Optional.of(existingUserDisc));
        when(plasticRepository.findPlasticEntityById(request.plasticId())).thenReturn(Optional.of(mockPlastic));

        // Act
        boolean result = userDiscService.updateUserDisc(userId, userDiscId, request);

        // Assert
        assertTrue(result);
        verify(userDiscRepository, times(1)).save(any(UserDisc.class));
    }
    @Test
    void testUpdateUserDisc_BothPlasticIdAndCustomPlasticProvided() {
        // Arrange
        Long userId = 1L;
        Long userDiscId = 1L;
        UserDisc existingUserDisc = MockDataFactory.createMockUserDiscDestroyer();
        UpdateDiscRequest request = new UpdateDiscRequest(
                10f, 5f, -1f, 3f, "#123456", 1L, "Custom Plastic", 173.0, "Updated comment"
        );

        when(userDiscRepository.findDiscEntityByUserDiscId(userDiscId)).thenReturn(Optional.of(existingUserDisc));

        // Act + Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userDiscService.updateUserDisc(userId, userDiscId, request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Only one of plasticId or customPlastic should be provided.", exception.getReason());
    }
    @Test
    void testUpdateUserDisc_NoPlasticProvided_ThrowsException() {
        // Arrange
        Long userId = 1L;
        Long userDiscId = 1L;
        UserDisc existingUserDisc = MockDataFactory.createMockUserDiscDestroyer();
        UpdateDiscRequest request = new UpdateDiscRequest(
                10f, 5f, -1f, 3f, "#123456", null, null, 173.0, "Updated comment"
        );

        when(userDiscRepository.findDiscEntityByUserDiscId(userDiscId)).thenReturn(Optional.of(existingUserDisc));

        // Act + Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userDiscService.updateUserDisc(userId, userDiscId, request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("You must provide either a plasticId or a customPlastic.", exception.getReason());
    }
    @Test
    void testUpdateUserDisc_PlasticIdProvidedButNotFound() {
        // Arrange
        Long userId = 1L;
        Long userDiscId = 1L;
        UserDisc existingUserDisc = MockDataFactory.createMockUserDiscDestroyer();
        UpdateDiscRequest request = new UpdateDiscRequest(
                10f, 5f, -1f, 3f, "#123456", 1L, null, 173.0, "Updated comment"
        );

        when(userDiscRepository.findDiscEntityByUserDiscId(userDiscId)).thenReturn(Optional.of(existingUserDisc));
        when(plasticRepository.findPlasticEntityById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userDiscService.updateUserDisc(userId, userDiscId, request)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Plastic not found: 1", exception.getReason());
    }


    // METHOD: deleteDisc()
    @Test
    void testDeleteDisc_SuccessfulDeletion() {
        // Arrange
        Long userId = 1L;
        Long userDiscId = 1L;
        UserDisc mockUserDisc = MockDataFactory.createMockUserDiscDestroyer();

        when(userDiscRepository.findDiscEntityByUserDiscId(userDiscId)).thenReturn(Optional.of(mockUserDisc));

        // Act
        boolean result = userDiscService.deleteDisc(userId, userDiscId);

        // Assert
        assertTrue(result);
        verify(userDiscRepository, times(1)).findDiscEntityByUserDiscId(userDiscId);
        verify(userDiscRepository, times(1)).deleteById(userDiscId);
    }
    @Test
    void testDeleteDisc_DiscNotFound() {
        // Arrange
        Long userId = 1L;
        Long userDiscId = 1L;

        when(userDiscRepository.findDiscEntityByUserDiscId(userDiscId)).thenReturn(Optional.empty());

        // Act + Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userDiscService.deleteDisc(userId, userDiscId)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("UserDisc with this id does not exist", exception.getReason());

        verify(userDiscRepository, times(1)).findDiscEntityByUserDiscId(userDiscId);
        verify(userDiscRepository, never()).deleteById(anyLong());
    }
    @Test
    void testDeleteDisc_DiscDoesNotBelongToUser() {
        // Arrange
        Long userId = 1L;
        Long userDiscId = 1L;
        UserDisc mockUserDisc = MockDataFactory.createMockUserDiscDestroyer();
        mockUserDisc.setUserId(99L); // Simulate different user

        when(userDiscRepository.findDiscEntityByUserDiscId(userDiscId)).thenReturn(Optional.of(mockUserDisc));

        // Act + Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userDiscService.deleteDisc(userId, userDiscId)
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("UserDisc does not belong to this user", exception.getReason());

        verify(userDiscRepository, times(1)).findDiscEntityByUserDiscId(userDiscId);
        verify(userDiscRepository, never()).deleteById(anyLong());
    }


    // METHOD: setInUseStatus()
    @Test
    void testSetInUseStatus_Success() {
        // Arrange
        Long userDiscId = 1L;

        // Act
        userDiscService.setInUseStatus(userDiscId, true);

        // Assert
        verify(userDiscRepository).updateInUseStatus(userDiscId, true);
    }

}

