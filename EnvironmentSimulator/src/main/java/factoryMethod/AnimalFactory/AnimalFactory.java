package factoryMethod.AnimalFactory;

import builder.MapBuilder.MapBuilder;
import builder.MapBuilder.Position;
import memento.GameSnapshot.AnimalState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract factory for creating animals using the Factory Method pattern.
 * <p>
 * Defines the factory method for creating different types of animals
 * (carnivores and herbivores) with validation and initialization logic.
 * Subclasses implement the specific animal creation logic.
 * </p>
 */
public abstract class AnimalFactory {

    private static final Logger logger = LogManager.getLogger(AnimalFactory.class);

    // FACTORY METHOD
    public Animal buildAnimal(MapBuilder builder,
                              AnimalRepository repository,
                              Position position,
                              String sex,
                              int hp,
                              int exp,
                              int level) {

        logger.debug("Building animal with params: pos={}, sex={}, hp={}, exp={}, lvl={}",
                position, sex, hp, exp, level);

        validateCreation(builder, position, sex, hp, exp, level);

        Animal animal = createAnimal(repository, position, sex, hp, exp, level);

        logger.info("Animal created: ID={}, Type={}, Pos={}, Sex={}, HP={}, LVL={}",
                animal.getId(),
                animal.getAnimalType(),
                animal.getPosition(),
                animal.getSex(),
                animal.getHp(),
                animal.getLevel());

        return animal;
    }

    protected abstract Animal createAnimal(
            AnimalRepository repository,
            Position position,
            String sex,
            int hp,
            int exp,
            int level);

    public Animal createAnimalFromState(AnimalRepository repository,
                                        AnimalState state) {

        logger.debug("Restoring animal from snapshot: ID={}, Type={}, Pos={}, Sex={}, HP={}, EXP={}, LVL={}",
                state.id(),
                state.animalType(),
                state.position(),
                state.sex(),
                state.hp(),
                state.exp(),
                state.level());

        Animal animal = createAnimal(
                repository,
                state.position(),
                state.sex(),
                state.hp(),
                state.exp(),
                state.level()
        );

        logger.info("Animal restored from snapshot: ID={}, Type={}", animal.getId(), animal.getAnimalType());

        return animal;
    }

    // VALIDATION HELPERS
    private static void validatePosition(MapBuilder builder, Position position) {
        if (position == null) {
            logger.error("Position validation failed: null position");
            throw new IllegalArgumentException("positions invalid");
        }

        int x = position.x();
        int y = position.y();

        if (x < 0 || x >= builder.getWidth()) {
            logger.error("Invalid X position: {}", x);
            throw new IllegalArgumentException("Invalid X position: " + x);
        }

        if (y < 0 || y >= builder.getHeight()) {
            logger.error("Invalid Y position: {}", y);
            throw new IllegalArgumentException("Invalid Y position: " + y);
        }
    }

    private static void validateSex(String sex) {
        if (sex == null) {
            logger.error("Sex validation failed: null");
            throw new IllegalArgumentException("Sex must be 'M' or 'F'");
        }

        String s = sex.toUpperCase();
        if (!s.equals("M") && !s.equals("F")) {
            logger.error("Invalid sex value: {}", sex);
            throw new IllegalArgumentException("Sex must be 'M' or 'F'");
        }
    }

    private static void validateHp(int hp) {
        if (hp < 0) {
            logger.error("Invalid HP: {}", hp);
            throw new IllegalArgumentException("HP must be non-negative");
        }
    }

    private static void validateExp(int exp) {
        if (exp < 0) {
            logger.error("Invalid EXP: {}", exp);
            throw new IllegalArgumentException("EXP must be non-negative");
        }
    }

    private static void validateLevel(int level) {
        if (level < 1) {
            logger.error("Invalid level: {}", level);
            throw new IllegalArgumentException("Level must be at least 1");
        }
    }

    public static void validateCreation(MapBuilder builder,
                                        Position position,
                                        String sex,
                                        int hp,
                                        int exp,
                                        int level) {

        logger.trace("Validating animal creation parameters.");

        validatePosition(builder, position);
        validateSex(sex);
        validateHp(hp);
        validateExp(exp);
        validateLevel(level);

        logger.trace("Validation successful.");
    }
}