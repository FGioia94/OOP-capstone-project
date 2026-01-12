package factoryMethod.AnimalFactory;

import builder.MapBuilder.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Herbivore class.
 */
@DisplayName("Herbivore Unit Tests")
class HerbivoreTest {

    private Position position;

    @BeforeEach
    void setUp() {
        position = new Position(3, 7);
    }

    @Test
    @DisplayName("Should create herbivore with correct attributes")
    void testCreateHerbivore() {
        // Arrange & Act
        Herbivore herbivore = new Herbivore("H001", position, "Female", 80, 10, 2);

        // Assert
        assertEquals("H001", herbivore.getId());
        assertEquals(position, herbivore.getPosition());
        assertEquals("Female", herbivore.getSex());
        assertEquals(80, herbivore.getHp());
        assertEquals(10, herbivore.getExp());
        assertEquals(2, herbivore.getLevel());
        assertEquals("Herbivore", herbivore.getAnimalType());
        assertEquals(3, herbivore.getRange()); // Herbivores have range 3
    }

    @Test
    @DisplayName("Should set and get HP")
    void testSetAndGetHp() {
        // Arrange
        Herbivore herbivore = new Herbivore("H001", position, "Female", 80, 10, 2);

        // Act
        herbivore.setHp(60);

        // Assert
        assertEquals(60, herbivore.getHp());
    }

    @Test
    @DisplayName("Should set and get experience")
    void testSetAndGetExp() {
        // Arrange
        Herbivore herbivore = new Herbivore("H001", position, "Female", 80, 10, 2);

        // Act
        herbivore.setExp(25);

        // Assert
        assertEquals(25, herbivore.getExp());
    }

    @Test
    @DisplayName("Should set and get level")
    void testSetAndGetLevel() {
        // Arrange
        Herbivore herbivore = new Herbivore("H001", position, "Female", 80, 10, 2);

        // Act
        herbivore.setLevel(3);

        // Assert
        assertEquals(3, herbivore.getLevel());
    }

    @Test
    @DisplayName("Should set and get position")
    void testSetAndGetPosition() {
        // Arrange
        Herbivore herbivore = new Herbivore("H001", position, "Female", 80, 10, 2);
        Position newPosition = new Position(10, 15);

        // Act
        herbivore.setPosition(newPosition);

        // Assert
        assertEquals(newPosition, herbivore.getPosition());
    }

    @Test
    @DisplayName("Should set and get pack")
    void testSetAndGetPack() {
        // Arrange
        Herbivore herbivore = new Herbivore("H001", position, "Female", 80, 10, 2);

        // Act
        herbivore.setPack("HerdA");

        // Assert
        assertEquals("HerdA", herbivore.getPack());
    }

    @Test
    @DisplayName("Should initially have no pack")
    void testInitiallyNoPack() {
        // Arrange & Act
        Herbivore herbivore = new Herbivore("H001", position, "Female", 80, 10, 2);

        // Assert
        assertNull(herbivore.getPack());
    }

    @Test
    @DisplayName("Herbivores should have different range than Carnivores")
    void testHerbivoreRangeDifferentFromCarnivore() {
        // Arrange
        Herbivore herbivore = new Herbivore("H001", position, "Female", 80, 10, 2);
        Carnivore carnivore = new Carnivore("C001", position, "Male", 100, 0, 1);

        // Assert
        assertNotEquals(herbivore.getRange(), carnivore.getRange());
        assertEquals(3, herbivore.getRange());
        assertEquals(5, carnivore.getRange());
    }
}
