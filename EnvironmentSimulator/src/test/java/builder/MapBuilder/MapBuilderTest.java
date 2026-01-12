package builder.MapBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MapBuilder class.
 * Tests the builder pattern functionality, validation, and map operations.
 */
@DisplayName("MapBuilder Unit Tests")
class MapBuilderTest {

    private MapBuilder mapBuilder;
    private List<Position> waterPositions;
    private List<Position> grassPositions;
    private List<Position> obstaclePositions;

    @BeforeEach
    void setUp() {
        mapBuilder = new MapBuilder();
        waterPositions = Arrays.asList(
                new Position(0, 0),
                new Position(1, 0)
        );
        grassPositions = Arrays.asList(
                new Position(2, 0),
                new Position(3, 0)
        );
        obstaclePositions = Arrays.asList(
                new Position(4, 0),
                new Position(5, 0)
        );
    }

    @Nested
    @DisplayName("Builder Methods")
    class BuilderMethods {

        @Test
        @DisplayName("Should set width correctly")
        void testSetWidth() {
            // Act
            MapBuilder result = mapBuilder.setWidth(100);

            // Assert
            assertSame(mapBuilder, result); // Fluent API
            assertEquals(100, mapBuilder.getWidth());
        }

        @Test
        @DisplayName("Should set height correctly")
        void testSetHeight() {
            // Act
            MapBuilder result = mapBuilder.setHeight(50);

            // Assert
            assertSame(mapBuilder, result); // Fluent API
            assertEquals(50, mapBuilder.getHeight());
        }

        @Test
        @DisplayName("Should set water positions correctly")
        void testSetWaterPositions() {
            // Act
            MapBuilder result = mapBuilder.setWaterPositions(waterPositions);

            // Assert
            assertSame(mapBuilder, result);
            assertEquals(waterPositions, mapBuilder.getWaterPositions());
        }

        @Test
        @DisplayName("Should set grass positions correctly")
        void testSetGrassPositions() {
            // Act
            MapBuilder result = mapBuilder.setGrassPositions(grassPositions);

            // Assert
            assertSame(mapBuilder, result);
            assertEquals(grassPositions, mapBuilder.getGrassPositions());
        }

        @Test
        @DisplayName("Should set obstacle positions correctly")
        void testSetObstaclesPositions() {
            // Act
            MapBuilder result = mapBuilder.setObstaclesPositions(obstaclePositions);

            // Assert
            assertSame(mapBuilder, result);
            assertEquals(obstaclePositions, mapBuilder.getObstaclesPositions());
        }

        @Test
        @DisplayName("Should support method chaining")
        void testMethodChaining() {
            // Act & Assert
            assertDoesNotThrow(() -> {
                mapBuilder
                        .setWidth(10)
                        .setHeight(10)
                        .setWaterPositions(waterPositions)
                        .setGrassPositions(grassPositions)
                        .setObstaclesPositions(obstaclePositions)
                        .build();
            });
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should build successfully with valid configuration")
        void testBuildSuccess() throws InvalidMapConfigurationException {
            // Arrange
            mapBuilder
                    .setWidth(10)
                    .setHeight(10)
                    .setWaterPositions(waterPositions)
                    .setGrassPositions(grassPositions)
                    .setObstaclesPositions(obstaclePositions);

            // Act
            EnvironmentMap map = mapBuilder.build();

            // Assert
            assertNotNull(map);
        }

        @Test
        @DisplayName("Should throw exception when width is invalid")
        void testBuildFailsWithInvalidWidth() {
            // Arrange
            mapBuilder
                    .setWidth(0) // Invalid
                    .setHeight(10)
                    .setWaterPositions(waterPositions)
                    .setGrassPositions(grassPositions)
                    .setObstaclesPositions(obstaclePositions);

            // Act & Assert
            assertThrows(InvalidMapConfigurationException.class, () -> mapBuilder.build());
        }

        @Test
        @DisplayName("Should throw exception when height is invalid")
        void testBuildFailsWithInvalidHeight() {
            // Arrange
            mapBuilder
                    .setWidth(10)
                    .setHeight(-1) // Invalid
                    .setWaterPositions(waterPositions)
                    .setGrassPositions(grassPositions)
                    .setObstaclesPositions(obstaclePositions);

            // Act & Assert
            assertThrows(InvalidMapConfigurationException.class, () -> mapBuilder.build());
        }

        @Test
        @DisplayName("Should throw exception when water positions are empty")
        void testBuildFailsWithEmptyWater() {
            // Arrange
            mapBuilder
                    .setWidth(10)
                    .setHeight(10)
                    .setWaterPositions(new ArrayList<>()) // Empty
                    .setGrassPositions(grassPositions)
                    .setObstaclesPositions(obstaclePositions);

            // Act & Assert
            assertThrows(InvalidMapConfigurationException.class, () -> mapBuilder.build());
        }

        @Test
        @DisplayName("Should throw exception when grass positions are empty")
        void testBuildFailsWithEmptyGrass() {
            // Arrange
            mapBuilder
                    .setWidth(10)
                    .setHeight(10)
                    .setWaterPositions(waterPositions)
                    .setGrassPositions(new ArrayList<>()) // Empty
                    .setObstaclesPositions(obstaclePositions);

            // Act & Assert
            assertThrows(InvalidMapConfigurationException.class, () -> mapBuilder.build());
        }

        @Test
        @DisplayName("Should throw exception when obstacle positions are empty")
        void testBuildFailsWithEmptyObstacles() {
            // Arrange
            mapBuilder
                    .setWidth(10)
                    .setHeight(10)
                    .setWaterPositions(waterPositions)
                    .setGrassPositions(grassPositions)
                    .setObstaclesPositions(new ArrayList<>()); // Empty

            // Act & Assert
            assertThrows(InvalidMapConfigurationException.class, () -> mapBuilder.build());
        }
    }

    @Nested
    @DisplayName("Position Operations")
    class PositionOperations {

        @BeforeEach
        void setUp() {
            mapBuilder
                    .setWidth(10)
                    .setHeight(10)
                    .setWaterPositions(waterPositions)
                    .setGrassPositions(grassPositions)
                    .setObstaclesPositions(obstaclePositions);
        }

        @Test
        @DisplayName("Should get all valid positions")
        void testGetAllValidPositions() {
            // Act
            List<Position> valid = mapBuilder.getAllValidPositions();

            // Assert
            assertNotNull(valid);
            assertFalse(valid.isEmpty());
            // Valid positions should not include water, grass, or obstacles
            for (Position pos : waterPositions) {
                assertFalse(valid.contains(pos));
            }
            for (Position pos : grassPositions) {
                assertFalse(valid.contains(pos));
            }
            for (Position pos : obstaclePositions) {
                assertFalse(valid.contains(pos));
            }
        }

        @Test
        @DisplayName("Should get random valid position")
        void testGetRandomValidPosition() {
            // Act
            Position pos = mapBuilder.getRandomValidPosition();

            // Assert
            assertNotNull(pos);
            List<Position> valid = mapBuilder.getAllValidPositions();
            assertTrue(valid.contains(pos));
        }

        @Test
        @DisplayName("Should spawn elements at valid positions")
        void testSpawnElements() {
            // Arrange
            int initialSize = waterPositions.size();

            // Act
            List<Position> newPositions = mapBuilder.spawnElements(3, new ArrayList<>(waterPositions));

            // Assert
            assertEquals(initialSize + 3, newPositions.size());
        }
    }

    @Nested
    @DisplayName("Clear Operations")
    class ClearOperations {

        @BeforeEach
        void setUp() {
            mapBuilder
                    .setWidth(10)
                    .setHeight(10)
                    .setWaterPositions(new ArrayList<>(waterPositions))
                    .setGrassPositions(new ArrayList<>(grassPositions))
                    .setObstaclesPositions(new ArrayList<>(obstaclePositions));
        }

        @Test
        @DisplayName("Should clear water and grass positions")
        void testClear() {
            // Act
            mapBuilder.clear();

            // Assert
            assertTrue(mapBuilder.getWaterPositions().isEmpty());
            assertTrue(mapBuilder.getGrassPositions().isEmpty());
            assertFalse(mapBuilder.getObstaclesPositions().isEmpty()); // Obstacles not cleared
        }

        @Test
        @DisplayName("Should clear all positions")
        void testClearAll() {
            // Act
            mapBuilder.clearAll();

            // Assert
            assertTrue(mapBuilder.getWaterPositions().isEmpty());
            assertTrue(mapBuilder.getGrassPositions().isEmpty());
            assertTrue(mapBuilder.getObstaclesPositions().isEmpty());
        }
    }
}
