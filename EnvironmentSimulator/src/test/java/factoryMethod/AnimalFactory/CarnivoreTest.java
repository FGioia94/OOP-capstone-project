package factoryMethod.AnimalFactory;

import builder.MapBuilder.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Carnivore class.
 */
@DisplayName("Carnivore Unit Tests")
class CarnivoreTest {

    private Position position;

    @BeforeEach
    void setUp() {
        position = new Position(5, 10);
    }

    @Test
    @DisplayName("Should create carnivore with correct attributes")
    void testCreateCarnivore() {
        // Arrange & Act
        Carnivore carnivore = new Carnivore("C001", position, "Male", 100, 0, 1);

        // Assert
        assertEquals("C001", carnivore.getId());
        assertEquals(position, carnivore.getPosition());
        assertEquals("Male", carnivore.getSex());
        assertEquals(100, carnivore.getHp());
        assertEquals(0, carnivore.getExp());
        assertEquals(1, carnivore.getLevel());
        assertEquals("Carnivore", carnivore.getAnimalType());
        assertEquals(5, carnivore.getRange()); // Carnivores have range 5
    }

    @Test
    @DisplayName("Should set and get HP")
    void testSetAndGetHp() {
        // Arrange
        Carnivore carnivore = new Carnivore("C001", position, "Male", 100, 0, 1);

        // Act
        carnivore.setHp(75);

        // Assert
        assertEquals(75, carnivore.getHp());
    }

    @Test
    @DisplayName("Should set and get experience")
    void testSetAndGetExp() {
        // Arrange
        Carnivore carnivore = new Carnivore("C001", position, "Male", 100, 0, 1);

        // Act
        carnivore.setExp(50);

        // Assert
        assertEquals(50, carnivore.getExp());
    }

    @Test
    @DisplayName("Should set and get level")
    void testSetAndGetLevel() {
        // Arrange
        Carnivore carnivore = new Carnivore("C001", position, "Male", 100, 0, 1);

        // Act
        carnivore.setLevel(5);

        // Assert
        assertEquals(5, carnivore.getLevel());
    }

    @Test
    @DisplayName("Should set and get position")
    void testSetAndGetPosition() {
        // Arrange
        Carnivore carnivore = new Carnivore("C001", position, "Male", 100, 0, 1);
        Position newPosition = new Position(15, 20);

        // Act
        carnivore.setPosition(newPosition);

        // Assert
        assertEquals(newPosition, carnivore.getPosition());
    }

    @Test
    @DisplayName("Should set and get pack")
    void testSetAndGetPack() {
        // Arrange
        Carnivore carnivore = new Carnivore("C001", position, "Male", 100, 0, 1);

        // Act
        carnivore.setPack("Pack1");

        // Assert
        assertEquals("Pack1", carnivore.getPack());
    }

    @Test
    @DisplayName("Should initially have no pack")
    void testInitiallyNoPack() {
        // Arrange & Act
        Carnivore carnivore = new Carnivore("C001", position, "Male", 100, 0, 1);

        // Assert
        assertNull(carnivore.getPack());
    }

    @Test
    @DisplayName("Should have empty members list")
    void testGetMembers() {
        // Arrange
        Carnivore carnivore = new Carnivore("C001", position, "Male", 100, 0, 1);

        // Act & Assert
        assertNotNull(carnivore.getMembers());
        assertTrue(carnivore.getMembers().isEmpty());
    }
}
