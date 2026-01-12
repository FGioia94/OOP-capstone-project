package factoryMethod.AnimalFactory;

import builder.MapBuilder.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AnimalRepository using Mockito.
 * Tests repository operations with mocked animals.
 */
@DisplayName("AnimalRepository Unit Tests with Mockito")
class AnimalRepositoryTest {

    private AnimalRepository repository;

    @Mock
    private AnimalComponent mockAnimal1;

    @Mock
    private AnimalComponent mockAnimal2;

    @Mock
    private AnimalComponent mockAnimal3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new AnimalRepository();

        // Setup mock behaviors
        when(mockAnimal1.getId()).thenReturn("A001");
        when(mockAnimal1.getAnimalType()).thenReturn("Carnivore");
        when(mockAnimal1.getPosition()).thenReturn(new Position(1, 1));

        when(mockAnimal2.getId()).thenReturn("A002");
        when(mockAnimal2.getAnimalType()).thenReturn("Herbivore");
        when(mockAnimal2.getPosition()).thenReturn(new Position(2, 2));

        when(mockAnimal3.getId()).thenReturn("A003");
        when(mockAnimal3.getAnimalType()).thenReturn("Carnivore");
        when(mockAnimal3.getPosition()).thenReturn(new Position(3, 3));
    }

    @Test
    @DisplayName("Should add animal to repository")
    void testAddAnimal() {
        // Act
        repository.add(mockAnimal1);

        // Assert
        assertEquals(1, repository.getAll().size());
        verify(mockAnimal1, atLeastOnce()).getId();
    }

    @Test
    @DisplayName("Should retrieve animal by ID")
    void testGetAnimalById() {
        // Arrange
        repository.add(mockAnimal1);

        // Act
        AnimalComponent retrieved = repository.get("A001");

        // Assert
        assertNotNull(retrieved);
        assertEquals(mockAnimal1, retrieved);
        verify(mockAnimal1, atLeastOnce()).getId();
    }

    @Test
    @DisplayName("Should return null for non-existent ID")
    void testGetNonExistentAnimal() {
        // Act
        AnimalComponent retrieved = repository.get("NONEXISTENT");

        // Assert
        assertNull(retrieved);
    }

    @Test
    @DisplayName("Should get all animals")
    void testGetAllAnimals() {
        // Arrange
        repository.add(mockAnimal1);
        repository.add(mockAnimal2);
        repository.add(mockAnimal3);

        // Act
        Collection<AnimalComponent> all = repository.getAll();

        // Assert
        assertEquals(3, all.size());
        assertTrue(all.contains(mockAnimal1));
        assertTrue(all.contains(mockAnimal2));
        assertTrue(all.contains(mockAnimal3));
    }

    @Test
    @DisplayName("Should remove animal from repository")
    void testRemoveAnimal() {
        // Arrange
        repository.add(mockAnimal1);
        repository.add(mockAnimal2);

        // Act
        repository.remove("A001");

        // Assert
        assertEquals(1, repository.getAll().size());
        assertNull(repository.get("A001"));
        assertNotNull(repository.get("A002"));
    }

    @Test
    @DisplayName("Should handle removing non-existent animal")
    void testRemoveNonExistentAnimal() {
        // Arrange
        repository.add(mockAnimal1);

        // Act
        repository.remove("NONEXISTENT");

        // Assert
        assertEquals(1, repository.getAll().size());
    }

    @Test
    @DisplayName("Should clear all animals")
    void testClearRepository() {
        // Arrange
        repository.add(mockAnimal1);
        repository.add(mockAnimal2);
        repository.add(mockAnimal3);

        // Act
        repository.clear();

        // Assert
        assertEquals(0, repository.getAll().size());
        assertTrue(repository.getAll().isEmpty());
    }

    @Test
    @DisplayName("Should get animals by type")
    void testGetAnimalsByType() {
        // Arrange
        repository.add(mockAnimal1); // Carnivore
        repository.add(mockAnimal2); // Herbivore
        repository.add(mockAnimal3); // Carnivore

        // Act
        Collection<AnimalComponent> carnivores = repository.getAllByType("Carnivore");
        Collection<AnimalComponent> herbivores = repository.getAllByType("Herbivore");

        // Assert
        assertEquals(2, carnivores.size());
        assertEquals(1, herbivores.size());
        assertTrue(carnivores.contains(mockAnimal1));
        assertTrue(carnivores.contains(mockAnimal3));
        assertTrue(herbivores.contains(mockAnimal2));
    }

    @Test
    @DisplayName("Should return empty collection for non-existent type")
    void testGetAnimalsByNonExistentType() {
        // Arrange
        repository.add(mockAnimal1);

        // Act
        Collection<AnimalComponent> result = repository.getAllByType("NonExistentType");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should handle multiple additions and retrievals")
    void testMultipleOperations() {
        // Arrange & Act
        repository.add(mockAnimal1);
        repository.add(mockAnimal2);
        
        AnimalComponent retrieved1 = repository.get("A001");
        
        repository.add(mockAnimal3);
        repository.remove("A002");
        
        Collection<AnimalComponent> all = repository.getAll();

        // Assert
        assertEquals(2, all.size());
        assertNotNull(retrieved1);
        assertTrue(all.contains(mockAnimal1));
        assertFalse(all.contains(mockAnimal2));
        assertTrue(all.contains(mockAnimal3));
    }

    @Test
    @DisplayName("Should verify animal interactions during operations")
    void testAnimalInteractionsDuringOperations() {
        // Act
        repository.add(mockAnimal1);
        repository.add(mockAnimal2);
        repository.getAllByType("Carnivore");
        repository.get("A001");

        // Assert - Verify interactions with mocked animals
        verify(mockAnimal1, atLeastOnce()).getId();
        verify(mockAnimal1, atLeastOnce()).getAnimalType();
        verify(mockAnimal2, atLeastOnce()).getId();
        verify(mockAnimal2, atLeastOnce()).getAnimalType();
    }
}
