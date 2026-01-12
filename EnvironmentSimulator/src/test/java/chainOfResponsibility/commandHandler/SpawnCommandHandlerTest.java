package chainOfResponsibility.commandHandler;

import builder.MapBuilder.MapBuilder;
import builder.MapBuilder.Position;
import factoryMethod.AnimalFactory.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import template.Game.GameLoop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SpawnCommandHandler.
 * Uses real objects for integration-style testing of command handling.
 */
@DisplayName("SpawnCommandHandler Unit Tests")
class SpawnCommandHandlerTest {

    private SpawnCommandHandler handler;
    private GameLoop gameLoop;
    private MapBuilder mapBuilder;
    private AnimalRepository animalRepository;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        handler = new SpawnCommandHandler();

        // Setup real MapBuilder
        mapBuilder = new MapBuilder()
                .setWidth(20)
                .setHeight(20)
                .setWaterPositions(new ArrayList<>(Arrays.asList(
                        new Position(0, 0),
                        new Position(1, 0)
                )))
                .setGrassPositions(new ArrayList<>(Arrays.asList(
                        new Position(2, 0),
                        new Position(3, 0)
                )))
                .setObstaclesPositions(new ArrayList<>(Arrays.asList(
                        new Position(4, 0),
                        new Position(5, 0)
                )));

        animalRepository = new AnimalRepository();
        gameLoop = new GameLoop(mapBuilder, animalRepository);

        // Capture System.out
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        // Restore System.out
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should handle spawn command for water")
    void testSpawnWater() {
        // Arrange
        String input = "water\n3\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        int initialWaterCount = mapBuilder.getWaterPositions().size();

        // Act
        boolean result = handler.handle("spawn", scanner, gameLoop);

        // Assert
        assertTrue(result);
        assertEquals(initialWaterCount + 3, mapBuilder.getWaterPositions().size());
    }

    @Test
    @DisplayName("Should handle spawn command for grass")
    void testSpawnGrass() {
        // Arrange
        String input = "grass\n5\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        int initialGrassCount = mapBuilder.getGrassPositions().size();

        // Act
        boolean result = handler.handle("spawn", scanner, gameLoop);

        // Assert
        assertTrue(result);
        assertEquals(initialGrassCount + 5, mapBuilder.getGrassPositions().size());
    }

    @Test
    @DisplayName("Should not handle non-spawn commands")
    void testNonSpawnCommand() {
        // Arrange
        Scanner scanner = new Scanner(System.in);

        // Act
        boolean result = handler.handle("help", scanner, gameLoop);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should be case-insensitive")
    void testCaseInsensitive() {
        // Arrange
        String input = "water\n2\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        int initialWaterCount = mapBuilder.getWaterPositions().size();

        // Act
        boolean result = handler.handle("SPAWN", scanner, gameLoop);

        // Assert
        assertTrue(result);
        assertEquals(initialWaterCount + 2, mapBuilder.getWaterPositions().size());
    }

    @Test
    @DisplayName("Should spawn herbivores")
    void testSpawnHerbivores() {
        // Arrange
        String input = "herbivore\n2\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        int initialCount = animalRepository.getAll().size();

        // Act
        boolean result = handler.handle("spawn", scanner, gameLoop);

        // Assert
        assertTrue(result);
        assertEquals(initialCount + 2, animalRepository.getAll().size());
    }
}
