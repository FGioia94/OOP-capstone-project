package memento.GameSnapshot;

import builder.MapBuilder.MapBuilder;
import factoryMethod.AnimalFactory.AnimalRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;

public class GameSnapshot implements Serializable {

    private static final Logger logger = LogManager.getLogger(GameSnapshot.class);

    private final AnimalRepositoryState animalState;
    private final MapState mapState;

    public GameSnapshot(AnimalRepository repository, MapBuilder builder) {

        logger.info("Creating GameSnapshot...");

        this.animalState = repository.toState();
        this.mapState = builder.toState();

        logger.debug("GameSnapshot created: {} animals, map size {}x{}",
                animalState.animals().size(),
                mapState.width(),
                mapState.height()
        );
    }

    public AnimalRepositoryState getAnimalState() {
        return animalState;
    }

    public MapState getMapState() {
        return mapState;
    }
}