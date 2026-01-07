package memento.GameSnapshot;

import builder.MapBuilder.MapBuilder;
import factoryMethod.AnimalFactory.AnimalRepository;
import memento.GameSnapshot.AnimalRepositoryState;
import memento.GameSnapshot.MapState;

import java.io.Serializable;

public class GameSnapshot implements Serializable {
    private final AnimalRepositoryState animalState;
    private final MapState mapState;

    public GameSnapshot(AnimalRepository repository, MapBuilder builder) {
        this.animalState = repository.toState();
        this.mapState = builder.toState();
    }

    public AnimalRepositoryState getAnimalState() {
        return animalState;
    }

    public MapState getMapState() {
        return mapState;
    }

}
