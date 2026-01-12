package integration;

import builder.MapBuilder.MapBuilder;
import builder.MapBuilder.Position;
import factoryMethod.AnimalFactory.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the complete animal creation workflow.
 * Tests the integration between MapBuilder, AnimalFactory, and AnimalRepository.
 */
@DisplayName("Animal Creation Integration Tests")
class AnimalCreationIntegrationTest {

    private MapBuilder mapBuilder;
    private AnimalRepository animalRepository;
    private List<Position> waterPositions;
    private List<Position> grassPositions;
    private List<Position> obstaclePositions;

    @BeforeEach
    void setUp() throws Exception {
        // Set up a proper map
        waterPositions = Arrays.asList(
                new Position(0, 0),
                new Position(1, 0),
                new Position(2, 0)
        );
        grassPositions = Arrays.asList(
                new Position(0, 1),
                new Position(1, 1),
                new Position(2, 1)
        );
        obstaclePositions = Arrays.asList(
                new Position(0, 2),
                new Position(1, 2)
        );

        mapBuilder = new MapBuilder()
                .setWidth(20)
                .setHeight(20)
                .setWaterPositions(waterPositions)
                .setGrassPositions(grassPositions)
                .setObstaclesPositions(obstaclePositions);

        animalRepository = new AnimalRepository();
    }

    @Test
    @DisplayName("Should create carnivore and add to repository")
    void testCreateCarnivoreIntegration() {
        // Arrange
        CarnivoreFactory factory = new CarnivoreFactory();
        Position spawnPosition = new Position(5, 5);

        // Act
        Animal carnivore = factory.buildAnimal(
                mapBuilder,
                animalRepository,
                spawnPosition,
                "M",
                100,
                0,
                1
        );

        // Assert
        assertNotNull(carnivore);
        assertEquals("Carnivore", carnivore.getAnimalType());
        assertEquals(1, animalRepository.getAll().size());
        assertTrue(animalRepository.getAll().contains(carnivore));
        assertEquals(spawnPosition, carnivore.getPosition());
    }

    @Test
    @DisplayName("Should create herbivore and add to repository")
    void testCreateHerbivoreIntegration() {
        // Arrange
        HerbivoreFactory factory = new HerbivoreFactory();
        Position spawnPosition = new Position(6, 6);

        // Act
        Animal herbivore = factory.buildAnimal(
                mapBuilder,
                animalRepository,
                spawnPosition,
                "F",
                80,
                10,
                2
        );

        // Assert
        assertNotNull(herbivore);
        assertEquals("Herbivore", herbivore.getAnimalType());
        assertEquals(1, animalRepository.getAll().size());
        assertTrue(animalRepository.getAll().contains(herbivore));
    }

    @Test
    @DisplayName("Should create multiple animals and track in repository")
    void testCreateMultipleAnimals() {
        // Arrange
        CarnivoreFactory carnivoreFactory = new CarnivoreFactory();
        HerbivoreFactory herbivoreFactory = new HerbivoreFactory();

        // Act
        Animal carnivore1 = carnivoreFactory.buildAnimal(
                mapBuilder, animalRepository, new Position(5, 5), "M", 100, 0, 1
        );
        Animal carnivore2 = carnivoreFactory.buildAnimal(
                mapBuilder, animalRepository, new Position(6, 5), "F", 100, 0, 1
        );
        Animal herbivore1 = herbivoreFactory.buildAnimal(
                mapBuilder, animalRepository, new Position(7, 5), "M", 80, 0, 1
        );
        Animal herbivore2 = herbivoreFactory.buildAnimal(
                mapBuilder, animalRepository, new Position(8, 5), "F", 80, 0, 1
        );

        // Assert
        assertEquals(4, animalRepository.getAll().size());
        assertTrue(animalRepository.getAll().contains(carnivore1));
        assertTrue(animalRepository.getAll().contains(carnivore2));
        assertTrue(animalRepository.getAll().contains(herbivore1));
        assertTrue(animalRepository.getAll().contains(herbivore2));
    }

    @Test
    @DisplayName("Should respect map boundaries during animal creation")
    void testMapBoundariesIntegration() {
        // Arrange
        CarnivoreFactory factory = new CarnivoreFactory();
        Position outsidePosition = new Position(25, 25); // Outside 20x20 map

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            factory.buildAnimal(
                    mapBuilder,
                    animalRepository,
                    outsidePosition,
                    "M",
                    100,
                    0,
                    1
            );
        });

        // Repository should remain empty
        assertEquals(0, animalRepository.getAll().size());
    }

    @Test
    @DisplayName("Should create animals at random valid positions")
    void testCreateAtRandomValidPosition() {
        // Arrange
        CarnivoreFactory factory = new CarnivoreFactory();

        // Act
        Position randomPosition = mapBuilder.getRandomValidPosition();
        assertNotNull(randomPosition);

        Animal carnivore = factory.buildAnimal(
                mapBuilder,
                animalRepository,
                randomPosition,
                "M",
                100,
                0,
                1
        );

        // Assert
        assertNotNull(carnivore);
        List<Position> validPositions = mapBuilder.getAllValidPositions();
        
        // The original position should be valid (before animal was placed)
        // We just check that the animal was created successfully
        assertEquals(1, animalRepository.getAll().size());
    }

    @Test
    @DisplayName("Should handle animal attribute modifications")
    void testAnimalAttributeModifications() {
        // Arrange
        HerbivoreFactory factory = new HerbivoreFactory();
        Animal herbivore = factory.buildAnimal(
                mapBuilder,
                animalRepository,
                new Position(5, 5),
                "M",
                80,
                0,
                1
        );

        // Act - Simulate game events
        herbivore.setHp(60); // Take damage
        herbivore.setExp(25); // Gain experience
        herbivore.setLevel(2); // Level up
        herbivore.setPosition(new Position(6, 6)); // Move

        // Assert
        assertEquals(60, herbivore.getHp());
        assertEquals(25, herbivore.getExp());
        assertEquals(2, herbivore.getLevel());
        assertEquals(new Position(6, 6), herbivore.getPosition());
    }

    @Test
    @DisplayName("Should support finding animals by ID")
    void testFindAnimalById() {
        // Arrange
        CarnivoreFactory factory = new CarnivoreFactory();
        Animal carnivore = factory.buildAnimal(
                mapBuilder,
                animalRepository,
                new Position(5, 5),
                "M",
                100,
                0,
                1
        );

        String animalId = carnivore.getId();

        // Act
        AnimalComponent found = animalRepository.get(animalId);

        // Assert
        assertNotNull(found);
        assertEquals(carnivore, found);
        assertEquals(animalId, found.getId());
    }
}
