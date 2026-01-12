package factoryMethod.AnimalFactory;

import builder.MapBuilder.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class HerbivoreFactory extends AnimalFactory {

    private static final Logger logger = LogManager.getLogger(HerbivoreFactory.class);

    @Override
    public Animal createAnimal(
            AnimalRepository repository,
            Position position,
            String sex,
            int hp,
            int exp,
            int level) {

        String id = UUID.randomUUID().toString();

        logger.debug("Creating Herbivore with ID={}, Pos={}, Sex={}, HP={}, EXP={}, LVL={}",
                id, position, sex, hp, exp, level);

        Animal herbivore = new Herbivore(
                id,
                position,
                sex,
                hp,
                exp,
                level
        );

        repository.add(herbivore);

        logger.info("Herbivore created and added to repository: ID={}", id);

        return herbivore;
    }
}