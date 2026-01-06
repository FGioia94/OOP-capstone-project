package memento.GameSnapshot;

import java.io.Serializable;
import java.util.List;

public record AnimalRepositoryState(List<AnimalState> animals) implements Serializable {
}
