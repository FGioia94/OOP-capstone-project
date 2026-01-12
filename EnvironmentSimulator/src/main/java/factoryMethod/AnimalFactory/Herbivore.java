package factoryMethod.AnimalFactory;

import builder.MapBuilder.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Herbivore extends Animal {

    private static final Logger logger = LogManager.getLogger(Herbivore.class);

    public Herbivore(
            String id,
            Position position,
            String sex,
            int hp,
            int exp,
            int level) {

        super(id, 3, position, sex, hp, exp, level, "Herbivore");

        logger.info("Herbivore created: ID={}, Sex={}, Pos={}, HP={}, EXP={}, LVL={}",
                id, sex, position, hp, exp, level);
    }

    @Override
    Animal reproduce() {
        logger.debug("Herbivore ID={} attempted reproduction, but reproduce() is not implemented.", getId());
        return null;
    }
}