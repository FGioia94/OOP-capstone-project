package factoryMethod.AnimalFactory;

import builder.MapBuilder.Position;
import memento.GameSnapshot.AnimalState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

/**
 * Factory for creating carnivore animals.
 * <p>
 * Implements the Factory Method pattern to create carnivore instances
 * with unique IDs and adds them to the animal repository.
 * </p>
 */
public class CarnivoreFactory extends AnimalFactory {

    private static final Logger logger = LogManager.getLogger(CarnivoreFactory.class);

    @Override
    public Animal createAnimal(
            AnimalRepository repository,
            Position position,
            String sex,
            int hp,
            int exp,
            int level) {

        String id = UUID.randomUUID().toString();

        logger.debug("Creating Carnivore with ID={}, Pos={}, Sex={}, HP={}, EXP={}, LVL={}",
                id, position, sex, hp, exp, level);

        Animal carnivore = new Carnivore(
                id,
                position,
                sex,
                hp,
                exp,
                level
        );

        repository.add(carnivore);

        logger.info("Carnivore created and added to repository: ID={}", id);

        return carnivore;
    }
}