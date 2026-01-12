package factoryMethod.AnimalFactory;

import builder.MapBuilder.MapBuilder;
import builder.MapBuilder.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AnimalFactory using Mockito for mocking dependencies.
 */
@DisplayName("AnimalFactory Unit Tests with Mockito")
class AnimalFactoryTest {

    @Mock
    private MapBuilder mockMapBuilder;

    @Mock
    private AnimalRepository mockRepository;

    private CarnivoreFactory carnivoreFactory;
    private HerbivoreFactory herbivoreFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        carnivoreFactory = new CarnivoreFactory();
        herbivoreFactory = new HerbivoreFactory();

        // Setup common mock behaviors
        when(mockMapBuilder.getWidth()).thenReturn(100);
        when(mockMapBuilder.getHeight()).thenReturn(100);
    }

    @Test
    @DisplayName("Should create carnivore with valid parameters")
    void testCreateCarnivoreSuccess() {
        // Arrange
        Position position = new Position(10, 20);

        // Act
        Animal animal = carnivoreFactory.buildAnimal(
                mockMapBuilder, mockRepository, position, "M", 100, 0, 1
        );

        // Assert
        assertNotNull(animal);
        assertTrue(animal instanceof Carnivore);
        assertEquals("Carnivore", animal.getAnimalType());
        assertEquals(100, animal.getHp());
        assertEquals(1, animal.getLevel());
        assertEquals("M", animal.getSex());

        verify(mockRepository, times(1)).add(any(Animal.class));
    }

    @Test
    @DisplayName("Should create herbivore with valid parameters")
    void testCreateHerbivoreSuccess() {
        // Arrange
        Position position = new Position(15, 25);

        // Act
        Animal animal = herbivoreFactory.buildAnimal(
                mockMapBuilder, mockRepository, position, "F", 80, 10, 2
        );

        // Assert
        assertNotNull(animal);
        assertTrue(animal instanceof Herbivore);
        assertEquals("Herbivore", animal.getAnimalType());
        assertEquals(80, animal.getHp());
        assertEquals(2, animal.getLevel());
        assertEquals("F", animal.getSex());

        verify(mockRepository, times(1)).add(any(Animal.class));
    }

    @Test
    @DisplayName("Should throw exception for null position")
    void testNullPosition() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            carnivoreFactory.buildAnimal(
                    mockMapBuilder, mockRepository, null, "M", 100, 0, 1
            );
        });
    }

    @Test
    @DisplayName("Should throw exception for position outside map bounds (X too large)")
    void testPositionOutOfBoundsX() {
        // Arrange
        Position invalidPosition = new Position(150, 50); // X > width

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            carnivoreFactory.buildAnimal(
                    mockMapBuilder, mockRepository, invalidPosition, "M", 100, 0, 1
            );
        });
    }

    @Test
    @DisplayName("Should throw exception for position outside map bounds (Y too large)")
    void testPositionOutOfBoundsY() {
        // Arrange
        Position invalidPosition = new Position(50, 150); // Y > height

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            carnivoreFactory.buildAnimal(
                    mockMapBuilder, mockRepository, invalidPosition, "M", 100, 0, 1
            );
        });
    }

    @Test
    @DisplayName("Should throw exception for negative X position")
    void testNegativeXPosition() {
        // Arrange
        Position invalidPosition = new Position(-1, 50);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            carnivoreFactory.buildAnimal(
                    mockMapBuilder, mockRepository, invalidPosition, "M", 100, 0, 1
            );
        });
    }

    @Test
    @DisplayName("Should throw exception for invalid sex")
    void testInvalidSex() {
        // Arrange
        Position position = new Position(10, 20);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            carnivoreFactory.buildAnimal(
                    mockMapBuilder, mockRepository, position, "X", 100, 0, 1
            );
        });
    }

    @Test
    @DisplayName("Should throw exception for null sex")
    void testNullSex() {
        // Arrange
        Position position = new Position(10, 20);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            carnivoreFactory.buildAnimal(
                    mockMapBuilder, mockRepository, position, null, 100, 0, 1
            );
        });
    }

    @Test
    @DisplayName("Should throw exception for negative HP")
    void testNegativeHp() {
        // Arrange
        Position position = new Position(10, 20);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            carnivoreFactory.buildAnimal(
                    mockMapBuilder, mockRepository, position, "M", -10, 0, 1
            );
        });
    }

    @Test
    @DisplayName("Should throw exception for negative EXP")
    void testNegativeExp() {
        // Arrange
        Position position = new Position(10, 20);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            carnivoreFactory.buildAnimal(
                    mockMapBuilder, mockRepository, position, "M", 100, -5, 1
            );
        });
    }

    @Test
    @DisplayName("Should throw exception for level less than 1")
    void testInvalidLevel() {
        // Arrange
        Position position = new Position(10, 20);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            carnivoreFactory.buildAnimal(
                    mockMapBuilder, mockRepository, position, "M", 100, 0, 0
            );
        });
    }

    @Test
    @DisplayName("Should accept both 'M' and 'F' as valid sex values")
    void testValidSexValues() {
        // Arrange
        Position position = new Position(10, 20);

        // Act & Assert
        assertDoesNotThrow(() -> {
            carnivoreFactory.buildAnimal(
                    mockMapBuilder, mockRepository, position, "M", 100, 0, 1
            );
        });

        assertDoesNotThrow(() -> {
            herbivoreFactory.buildAnimal(
                    mockMapBuilder, mockRepository, position, "F", 80, 0, 1
            );
        });
    }

    @Test
    @DisplayName("Should accept lowercase sex values")
    void testLowercaseSexValues() {
        // Arrange
        Position position = new Position(10, 20);

        // Act & Assert
        assertDoesNotThrow(() -> {
            carnivoreFactory.buildAnimal(
                    mockMapBuilder, mockRepository, position, "m", 100, 0, 1
            );
        });

        assertDoesNotThrow(() -> {
            herbivoreFactory.buildAnimal(
                    mockMapBuilder, mockRepository, position, "f", 80, 0, 1
            );
        });
    }
}
