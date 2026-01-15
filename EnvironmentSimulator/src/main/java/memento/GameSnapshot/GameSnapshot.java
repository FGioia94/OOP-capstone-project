package memento.GameSnapshot;

import builder.MapBuilder.MapBuilder;
import factoryMethod.AnimalFactory.AnimalRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;

/**
 * Complete snapshot of the game state for save/load functionality.
 * <p>
 * Implements the Memento pattern by capturing both the animal repository
 * state and map state, allowing the entire game to be saved and restored.
 * Serializable for persistence to disk.
 * </p>
 */
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