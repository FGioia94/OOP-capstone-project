package testutils;

import builder.MapBuilder.MapBuilder;
import builder.MapBuilder.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for creating test data and fixtures.
 * Provides common test scenarios and configurations.
 */
public class TestDataBuilder {

    /**
     * Creates a basic MapBuilder with minimal valid configuration.
     */
    public static MapBuilder createBasicMapBuilder() {
        List<Position> waterPositions = List.of(
                new Position(0, 0),
                new Position(1, 0)
        );
        List<Position> grassPositions = List.of(
                new Position(2, 0),
                new Position(3, 0)
        );
        List<Position> obstaclePositions = List.of(
                new Position(4, 0),
                new Position(5, 0)
        );

        return new MapBuilder()
                .setWidth(20)
                .setHeight(20)
                .setWaterPositions(waterPositions)
                .setGrassPositions(grassPositions)
                .setObstaclesPositions(obstaclePositions);
    }

    /**
     * Creates a large MapBuilder for stress testing.
     */
    public static MapBuilder createLargeMapBuilder() {
        List<Position> waterPositions = new ArrayList<>();
        List<Position> grassPositions = new ArrayList<>();
        List<Position> obstaclePositions = new ArrayList<>();

        // Create scattered resources
        for (int i = 0; i < 10; i++) {
            waterPositions.add(new Position(i, 0));
            grassPositions.add(new Position(i, 1));
            obstaclePositions.add(new Position(i, 2));
        }

        return new MapBuilder()
                .setWidth(100)
                .setHeight(100)
                .setWaterPositions(waterPositions)
                .setGrassPositions(grassPositions)
                .setObstaclesPositions(obstaclePositions);
    }

    /**
     * Creates a list of valid spawn positions for testing.
     */
    public static List<Position> createValidPositionsList() {
        return List.of(
                new Position(5, 5),
                new Position(6, 6),
                new Position(7, 7),
                new Position(8, 8),
                new Position(9, 9)
        );
    }

    /**
     * Creates a list of invalid positions (out of bounds for a 20x20 map).
     */
    public static List<Position> createInvalidPositionsList() {
        return List.of(
                new Position(-1, 0),
                new Position(0, -1),
                new Position(25, 10),
                new Position(10, 25),
                new Position(100, 100)
        );
    }
}
