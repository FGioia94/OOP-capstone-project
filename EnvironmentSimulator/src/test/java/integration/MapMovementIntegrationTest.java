package integration;

import builder.MapBuilder.InvalidMapConfigurationException;
import builder.MapBuilder.MapBuilder;
import builder.MapBuilder.Position;
import factoryMethod.AnimalFactory.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for map building and animal movement.
 * Tests the integration between MapBuilder, Position, and Animal movement logic.
 */
@DisplayName("Map and Movement Integration Tests")
class MapMovementIntegrationTest {

    private MapBuilder mapBuilder;
    private AnimalRepository animalRepository;

    @BeforeEach
    void setUp() {
        List<Position> waterPositions = new ArrayList<>(Arrays.asList(
                new Position(0, 0),
                new Position(1, 0)
        ));
        List<Position> grassPositions = new ArrayList<>(Arrays.asList(
                new Position(2, 0),
                new Position(3, 0)
        ));
        List<Position> obstaclePositions = new ArrayList<>(Arrays.asList(
                new Position(4, 0),
                new Position(5, 0)
        ));

        mapBuilder = new MapBuilder()
                .setWidth(15)
                .setHeight(15)
                .setWaterPositions(waterPositions)
                .setGrassPositions(grassPositions)
                .setObstaclesPositions(obstaclePositions);

        animalRepository = new AnimalRepository();
    }

    @Test
    @DisplayName("Should successfully build valid map configuration")
    void testBuildValidMap() throws InvalidMapConfigurationException {
        // Act
        var map = mapBuilder.build();

        // Assert
        assertNotNull(map);
    }

    @Test
    @DisplayName("Should get valid positions excluding occupied ones")
    void testGetValidPositions() {
        // Act
        List<Position> validPositions = mapBuilder.getAllValidPositions();

        // Assert
        assertNotNull(validPositions);
        assertFalse(validPositions.isEmpty());
        
        // Valid positions should not include water, grass, or obstacles
        assertFalse(validPositions.contains(new Position(0, 0))); // water
        assertFalse(validPositions.contains(new Position(2, 0))); // grass
        assertFalse(validPositions.contains(new Position(4, 0))); // obstacle
    }

    @Test
    @DisplayName("Should move animal within its range")
    void testMoveAnimalWithinRange() {
        // Arrange
        CarnivoreFactory factory = new CarnivoreFactory();
        Position startPosition = new Position(7, 7);
        
        Animal carnivore = factory.buildAnimal(
                mapBuilder,
                animalRepository,
                startPosition,
                "M",
                100,
                0,
                1
        );

        // Act
        mapBuilder.moveAnimal(carnivore);
        Position newPosition = carnivore.getPosition();

        // Assert
        assertNotNull(newPosition);
        
        // Check if moved within range (carnivore has range 5)
        int distance = Math.abs(newPosition.x() - startPosition.x()) 
                     + Math.abs(newPosition.y() - startPosition.y());
        assertTrue(distance <= carnivore.getRange() || newPosition.equals(startPosition));
    }

    @Test
    @DisplayName("Should spawn new resources on valid positions")
    void testSpawnResources() {
        // Arrange
        int initialWaterCount = mapBuilder.getWaterPositions().size();

        // Act
        List<Position> newWaterPositions = mapBuilder.spawnElements(
                3,
                mapBuilder.getWaterPositions()
        );

        // Assert
        assertEquals(initialWaterCount + 3, newWaterPositions.size());
        
        // New positions should be valid
        List<Position> occupiedBeforeSpawn = Arrays.asList(
                new Position(0, 0), new Position(1, 0),
                new Position(2, 0), new Position(3, 0),
                new Position(4, 0), new Position(5, 0)
        );
        
        for (Position pos : newWaterPositions) {
            if (!occupiedBeforeSpawn.contains(pos)) {
                // New spawned position should be within map bounds
                assertTrue(pos.x() >= 0 && pos.x() < 15);
                assertTrue(pos.y() >= 0 && pos.y() < 15);
            }
        }
    }

    @Test
    @DisplayName("Should clear resources from map")
    void testClearResources() {
        // Act
        mapBuilder.clear();

        // Assert
        assertTrue(mapBuilder.getWaterPositions().isEmpty());
        assertTrue(mapBuilder.getGrassPositions().isEmpty());
        assertFalse(mapBuilder.getObstaclesPositions().isEmpty()); // Obstacles not cleared
    }

    @Test
    @DisplayName("Should clear all elements including obstacles")
    void testClearAll() {
        // Act
        mapBuilder.clearAll();

        // Assert
        assertTrue(mapBuilder.getWaterPositions().isEmpty());
        assertTrue(mapBuilder.getGrassPositions().isEmpty());
        assertTrue(mapBuilder.getObstaclesPositions().isEmpty());
    }

    @Test
    @DisplayName("Should place multiple animals on different positions")
    void testMultipleAnimalPlacement() {
        // Arrange
        CarnivoreFactory carnivoreFactory = new CarnivoreFactory();
        HerbivoreFactory herbivoreFactory = new HerbivoreFactory();

        // Act
        Animal carnivore1 = carnivoreFactory.buildAnimal(
                mapBuilder, animalRepository, new Position(7, 7), "M", 100, 0, 1
        );
        Animal carnivore2 = carnivoreFactory.buildAnimal(
                mapBuilder, animalRepository, new Position(8, 8), "F", 100, 0, 1
        );
        Animal herbivore1 = herbivoreFactory.buildAnimal(
                mapBuilder, animalRepository, new Position(9, 9), "M", 80, 0, 1
        );

        // Assert
        assertEquals(3, animalRepository.getAll().size());
        assertNotEquals(carnivore1.getPosition(), carnivore2.getPosition());
        assertNotEquals(carnivore1.getPosition(), herbivore1.getPosition());
        assertNotEquals(carnivore2.getPosition(), herbivore1.getPosition());
    }

    @Test
    @DisplayName("Should get random valid position multiple times")
    void testMultipleRandomPositions() {
        // Act
        Position pos1 = mapBuilder.getRandomValidPosition();
        Position pos2 = mapBuilder.getRandomValidPosition();
        Position pos3 = mapBuilder.getRandomValidPosition();

        // Assert
        assertNotNull(pos1);
        assertNotNull(pos2);
        assertNotNull(pos3);
        
        List<Position> validPositions = mapBuilder.getAllValidPositions();
        assertTrue(validPositions.contains(pos1));
        assertTrue(validPositions.contains(pos2));
        assertTrue(validPositions.contains(pos3));
    }

    @Test
    @DisplayName("Herbivore should have shorter range than carnivore")
    void testAnimalRangeDifferences() {
        // Arrange & Act
        Herbivore herbivore = new Herbivore("H1", new Position(7, 7), "M", 80, 0, 1);
        Carnivore carnivore = new Carnivore("C1", new Position(7, 7), "M", 100, 0, 1);

        // Assert
        assertEquals(3, herbivore.getRange());
        assertEquals(5, carnivore.getRange());
        assertTrue(carnivore.getRange() > herbivore.getRange());
    }
}
