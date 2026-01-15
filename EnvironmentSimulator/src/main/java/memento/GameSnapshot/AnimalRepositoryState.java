package memento.GameSnapshot;

import java.io.Serializable;
import java.util.List;

/**
 * Memento representing the saved state of the entire animal repository.
 * <p>
 * Contains a list of all animal states, allowing the complete animal
 * population to be saved and restored. Part of the Memento pattern.
 * </p>
 */
public record AnimalRepositoryState(List<AnimalState> animals) implements Serializable {
}
