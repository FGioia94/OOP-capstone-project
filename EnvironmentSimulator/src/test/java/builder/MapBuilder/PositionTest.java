package builder.MapBuilder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Position record class.
 * Tests the record's functionality including equality, hashCode, and toString.
 */
@DisplayName("Position Unit Tests")
class PositionTest {

    @Test
    @DisplayName("Should create position with correct coordinates")
    void testCreatePosition() {
        // Arrange & Act
        Position position = new Position(5, 10);

        // Assert
        assertEquals(5, position.x());
        assertEquals(10, position.y());
    }

    @Test
    @DisplayName("Should handle negative coordinates")
    void testNegativeCoordinates() {
        // Arrange & Act
        Position position = new Position(-5, -10);

        // Assert
        assertEquals(-5, position.x());
        assertEquals(-10, position.y());
    }

    @Test
    @DisplayName("Should handle zero coordinates")
    void testZeroCoordinates() {
        // Arrange & Act
        Position position = new Position(0, 0);

        // Assert
        assertEquals(0, position.x());
        assertEquals(0, position.y());
    }

    @Test
    @DisplayName("Two positions with same coordinates should be equal")
    void testEquality() {
        // Arrange
        Position position1 = new Position(3, 7);
        Position position2 = new Position(3, 7);

        // Assert
        assertEquals(position1, position2);
        assertEquals(position1.hashCode(), position2.hashCode());
    }

    @Test
    @DisplayName("Two positions with different coordinates should not be equal")
    void testInequality() {
        // Arrange
        Position position1 = new Position(3, 7);
        Position position2 = new Position(3, 8);
        Position position3 = new Position(4, 7);

        // Assert
        assertNotEquals(position1, position2);
        assertNotEquals(position1, position3);
        assertNotEquals(position2, position3);
    }

    @Test
    @DisplayName("Position should have consistent hashCode")
    void testHashCode() {
        // Arrange
        Position position = new Position(5, 10);

        // Act
        int hash1 = position.hashCode();
        int hash2 = position.hashCode();

        // Assert
        assertEquals(hash1, hash2);
    }

    @Test
    @DisplayName("Position toString should contain coordinates")
    void testToString() {
        // Arrange
        Position position = new Position(5, 10);

        // Act
        String result = position.toString();

        // Assert
        assertTrue(result.contains("5"));
        assertTrue(result.contains("10"));
    }
}
