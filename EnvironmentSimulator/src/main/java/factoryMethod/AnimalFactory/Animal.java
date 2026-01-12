package factoryMethod.AnimalFactory;

import builder.MapBuilder.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class Animal implements AnimalComponent {

    private static final Logger logger = LogManager.getLogger(Animal.class);

    private int exp;
    protected final int range;
    protected int level;

    protected final String id;
    public final String sex;
    public Position position = new Position(0, 0);
    public int hp;
    public final String animalType;
    private String pack;

    public Animal(
            String id,
            int range,
            Position position,
            String sex,
            int hp,
            int exp,
            int level,
            String animalType) {

        this.id = id;
        this.range = range;
        this.position = position;
        this.sex = sex;
        this.hp = hp;
        this.exp = exp;
        this.level = level;
        this.animalType = animalType;
        this.pack = null;

        logger.info("Created {} (ID={}) at position {} with HP={}, LVL={}, EXP={}",
                animalType, id, position, hp, level, exp);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getRange() {
        return range;
    }

    @Override
    public String getSex() {
        return sex;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setPosition(@annotations.ValidPosition(message = "Invalid animal position") Position position) {
        // Validate using annotation
        annotations.PositionValidator.validateDefault(position);
        logger.debug("Animal ID={} moved from {} to {}", id, this.position, position);
        this.position = position;
    }

    @Override
    public int getHp() {
        return hp;
    }

    @Override
    public void setHp(int hp) {
        logger.debug("Animal ID={} HP changed from {} to {}", id, this.hp, hp);
        this.hp = hp;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public int getExp() {
        return exp;
    }

    @Override
    public void setExp(int exp) {
        logger.debug("Animal ID={} EXP changed from {} to {}", id, this.exp, exp);
        this.exp = exp;
    }

    @Override
    public void setLevel(int level) {
        logger.debug("Animal ID={} level changed from {} to {}", id, this.level, level);
        this.level = level;
    }

    @Override
    public String getAnimalType() {
        return animalType;
    }

    @Override
    public String getPack() {
        return pack;
    }

    @Override
    public void setPack(String pack) {
        logger.debug("Animal ID={} assigned to pack {}", id, pack);
        this.pack = pack;
    }

    @Override
    public List<AnimalComponent> getMembers() {
        return new ArrayList<>();
    }

    abstract Animal reproduce();
}