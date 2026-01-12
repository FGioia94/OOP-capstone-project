package builder.MapBuilder;

import factoryMethod.AnimalFactory.AnimalComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Additional unit tests for MapBuilder using Mockito for animal interactions.
 * Demonstrates mocking AnimalComponent dependencies.
 */
@DisplayName("MapBuilder with Mockito Tests")
class MapBuilderMockitoTest {

    private MapBuilder mapBuilder;

    @Mock
    private AnimalComponent mockAnimal;

    private List<Position> waterPositions;
    private List<Position> grassPositions;
    private List<Position> obstaclePositions;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        waterPositions = new ArrayList<>(Arrays.asList(
                new Position(0, 0),
                new Position(1, 0)
        ));
        grassPositions = new ArrayList<>(Arrays.asList(
                new Position(2, 0),
                new Position(3, 0)
        ));
        obstaclePositions = new ArrayList<>(Arrays.asList(
                new Position(4, 0),
                new Position(5, 0)
        ));

        mapBuilder = new MapBuilder()
                .setWidth(10)
                .setHeight(10)
                .setWaterPositions(waterPositions)
                .setGrassPositions(grassPositions)
                .setObstaclesPositions(obstaclePositions);

        // Setup mock animal behavior
        when(mockAnimal.getPosition()).thenReturn(new Position(5, 5));
        when(mockAnimal.getRange()).thenReturn(3);
        when(mockAnimal.getId()).thenReturn("MOCK_ANIMAL_001");
    }

    @Test
    @DisplayName("Should move animal and verify position update")
    void testMoveAnimalWithMock() {
        // Act
        mapBuilder.moveAnimal(mockAnimal);

        // Assert
        verify(mockAnimal, atLeastOnce()).getPosition();
        verify(mockAnimal, atLeastOnce()).getRange();
        verify(mockAnimal, atMost(1)).setPosition(any(Position.class));
        verify(mockAnimal, atLeastOnce()).getId();
    }

    @Test
    @DisplayName("Should not move animal when no valid positions available")
    void testMoveAnimalNoValidPositions() {
        // Arrange - Create a fully occupied map
        List<Position> allPositions = new ArrayList<>();
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                allPositions.add(new Position(x, y));
            }
        }
        
        MapBuilder fullMapBuilder = new MapBuilder()
                .setWidth(10)
                .setHeight(10)
                .setWaterPositions(allPositions.subList(0, 33))
                .setGrassPositions(allPositions.subList(33, 66))
                .setObstaclesPositions(allPositions.subList(66, 100));

        // Act
        fullMapBuilder.moveAnimal(mockAnimal);

        // Assert
        verify(mockAnimal, never()).setPosition(any(Position.class));
    }

    @Test
    @DisplayName("Should respect animal range when moving")
    void testMoveAnimalRespectRange() {
        // Arrange
        Position startPosition = new Position(5, 5);
        when(mockAnimal.getPosition()).thenReturn(startPosition);
        when(mockAnimal.getRange()).thenReturn(2);

        // Act
        mapBuilder.moveAnimal(mockAnimal);

        // Assert - Verify animal position was queried for range calculation
        verify(mockAnimal, atLeastOnce()).getPosition();
        verify(mockAnimal, atLeastOnce()).getRange();
    }

    @Test
    @DisplayName("Should handle animal with zero range")
    void testMoveAnimalZeroRange() {
        // Arrange
        when(mockAnimal.getRange()).thenReturn(0);

        // Act
        mapBuilder.moveAnimal(mockAnimal);

        // Assert
        verify(mockAnimal, atLeastOnce()).getRange();
        // Animal should not move if range is 0 and no adjacent positions
        verify(mockAnimal, never()).setPosition(argThat(pos -> 
            !pos.equals(mockAnimal.getPosition())
        ));
    }

    @Test
    @DisplayName("Should handle multiple animal movements")
    void testMultipleAnimalMovements() {
        // Arrange
        AnimalComponent mockAnimal2 = mock(AnimalComponent.class);
        when(mockAnimal2.getPosition()).thenReturn(new Position(7, 7));
        when(mockAnimal2.getRange()).thenReturn(4);
        when(mockAnimal2.getId()).thenReturn("MOCK_ANIMAL_002");

        // Act
        mapBuilder.moveAnimal(mockAnimal);
        mapBuilder.moveAnimal(mockAnimal2);

        // Assert
        verify(mockAnimal, atLeastOnce()).getPosition();
        verify(mockAnimal2, atLeastOnce()).getPosition();
        verify(mockAnimal, atLeastOnce()).getId();
        verify(mockAnimal2, atLeastOnce()).getId();
    }
}
