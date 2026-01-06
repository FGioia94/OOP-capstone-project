package strategy.IO;

import builder.MapBuilder.MapBuilder;
import factoryMethod.AnimalFactory.AnimalRepository;

import java.io.Serializable;

public class GameSnapshot implements Serializable {
    private final AnimalRepositoryState animalState;
    private final MapState mapState;

    public GameSnapshot(AnimalRepository repository, MapBuilder builder) {
        this.animalState = repository.exportState();
        this.mapState = builder.exportState();
    }

    public AnimalRepositoryState getAnimalState() {
        return animalState;
    }

    public MapState getMapState() {
        return mapState;
    }
}
