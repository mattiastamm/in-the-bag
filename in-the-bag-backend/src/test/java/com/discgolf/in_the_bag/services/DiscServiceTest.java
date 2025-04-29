package com.discgolf.in_the_bag.services;

import com.discgolf.in_the_bag.records.DiscAutoFillBaseRecord;
import com.discgolf.in_the_bag.records.DiscAutoFillRecord;
import com.discgolf.in_the_bag.records.PlasticRecord;
import com.discgolf.in_the_bag.repositories.DiscRepository;
import com.discgolf.in_the_bag.repositories.PlasticRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DiscServiceTest {

    @Mock
    private DiscRepository discRepository;

    @Mock
    private PlasticRepository plasticRepository;

    @InjectMocks
    private DiscService discService;


    // METHOD: getDiscDetailsForCreation()
    @Test
    void testGetDiscDetailsForCreation_WhenDiscExists() {
        // Arrange
        Long discId = 1L;
        DiscAutoFillBaseRecord baseRecord = new DiscAutoFillBaseRecord(
                discId, "Destroyer", "Distance Driver", 1, "Innova", 12f, 5f, -1f, 3f
        );

        List<PlasticRecord> plastics = List.of(
                new PlasticRecord(1, "Star"),
                new PlasticRecord(2, "Champion")
        );

        when(discRepository.findDiscDetailsForCreation(discId)).thenReturn(Optional.of(baseRecord));
        when(plasticRepository.findPlasticsByManufacturer(1)).thenReturn(plastics);

        // Act
        Optional<DiscAutoFillRecord> result = discService.getDiscDetailsForCreation(discId);

        // Assert
        assertTrue(result.isPresent());
        DiscAutoFillRecord record = result.get();

        assertEquals(discId, record.id());
        assertEquals("Destroyer", record.name());
        assertEquals("Distance Driver", record.type());
        assertEquals("Innova", record.manufacturerName());
        assertEquals(12f, record.speed());
        assertEquals(5f, record.glide());
        assertEquals(-1f, record.turn());
        assertEquals(3f, record.fade());
        assertEquals(plastics, record.availablePlastics());

        // Verify that methods were called
        verify(discRepository, times(1)).findDiscDetailsForCreation(discId);
        verify(plasticRepository, times(1)).findPlasticsByManufacturer(1);
    }
    @Test
    void testGetDiscDetailsForCreation_WhenDiscNotFound() {
        // Arrange
        Long discId = 99L;
        when(discRepository.findDiscDetailsForCreation(discId)).thenReturn(Optional.empty());

        // Act
        Optional<DiscAutoFillRecord> result = discService.getDiscDetailsForCreation(discId);

        // Assert
        assertTrue(result.isEmpty());

        verify(discRepository, times(1)).findDiscDetailsForCreation(discId);
        verify(plasticRepository, never()).findPlasticsByManufacturer(anyInt());
    }
}

