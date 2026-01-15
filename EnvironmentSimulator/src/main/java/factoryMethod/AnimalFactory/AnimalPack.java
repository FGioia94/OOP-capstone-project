package factoryMethod.AnimalFactory;

import builder.MapBuilder.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite implementation representing a group of animals.
 * <p>
 * Implements the Composite pattern by allowing multiple animals to be
 * treated as a single unit. Pack attributes (position, HP, etc.) are
 * computed as aggregates of member animals.
 * </p>
 */
public class AnimalPack implements AnimalComponent {

    private static final Logger logger = LogManager.getLogger(AnimalPack.class);

    private final String id;
    private final List<AnimalComponent> members = new ArrayList<>();
    final String animalType;

    public AnimalPack(String id) {
        this.id = id;
        this.animalType = "Pack";

        logger.info("Created new AnimalPack with ID={}", id);
    }

    public void add(AnimalComponent c) {
        members.add(c);
        logger.debug("Animal ID={} added to Pack ID={}", c.getId(), id);
    }

    public void remove(AnimalComponent c) {
        members.remove(c);
        logger.debug("Animal ID={} removed from Pack ID={}", c.getId(), id);
    }

    @Override
    public List<AnimalComponent> getMembers() {
        return members;
    }

    // ---------------------------
    // POSITION
    // ---------------------------
    @Override
    public Position getPosition() {
        if (members.isEmpty()) {
            logger.warn("Pack ID={} has no members. Returning default position (0,0).", id);
            return new Position(0, 0);
        }

        int totalX = 0;
        int totalY = 0;

        for (AnimalComponent animal : members) {
            Position pos = animal.getPosition();
            totalX += pos.x();
            totalY += pos.y();
        }

        Position center = new Position(totalX / members.size(), totalY / members.size());

        logger.trace("Computed center position for Pack ID={} -> {}", id, center);

        return center;
    }

    @Override
    public void setPosition(Position p) {
        Position oldCenter = getPosition();
        int offsetX = p.x() - oldCenter.x();
        int offsetY = p.y() - oldCenter.y();

        logger.debug("Moving Pack ID={} from {} to {} (offset {},{})",
                id, oldCenter, p, offsetX, offsetY);

        for (AnimalComponent animal : members) {
            Position newPos = new Position(animal.getPosition().x() + offsetX,
                    animal.getPosition().y() + offsetY);
            animal.setPosition(newPos);
        }
    }

    // ---------------------------
    // HP / EXP / LEVEL
    // ---------------------------
    @Override
    public int getHp() {
        if (members.isEmpty()) {
            logger.warn("Pack ID={} has no members. HP=0.", id);
            return 0;
        }

        int totalHp = members.stream().mapToInt(AnimalComponent::getHp).sum();
        int avgHp = totalHp / members.size();

        logger.trace("Computed HP for Pack ID={} -> {}", id, avgHp);

        return avgHp;
    }

    @Override
    public void setHp(int hp) {
        logger.warn("setHp called on Pack ID={}, but packs do not support direct HP assignment.", id);
    }

    @Override
    public int getExp() {
        int exp = members.stream().mapToInt(AnimalComponent::getExp).sum();
        logger.trace("Computed EXP for Pack ID={} -> {}", id, exp);
        return exp;
    }

    @Override
    public void setExp(int exp) {
        logger.warn("setExp called on Pack ID={}, but packs do not support direct EXP assignment.", id);
    }

    @Override
    public int getLevel() {
        int level = members.stream().mapToInt(AnimalComponent::getLevel).sum();
        logger.trace("Computed Level for Pack ID={} -> {}", id, level);
        return level;
    }

    @Override
    public void setLevel(int level) {
        logger.warn("setLevel called on Pack ID={}, but packs do not support direct Level assignment.", id);
    }

    // ---------------------------
    // METADATA
    // ---------------------------
    @Override
    public String getAnimalType() {
        return "Pack";
    }

    @Override
    public String getSex() {
        return "";
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getRange() {
        if (members.isEmpty()) {
            logger.warn("Pack ID={} has no members. Range=0.", id);
            return 0;
        }

        int totalRange = members.stream().mapToInt(AnimalComponent::getRange).sum();
        int avgRange = totalRange / members.size();

        logger.trace("Computed Range for Pack ID={} -> {}", id, avgRange);

        return avgRange;
    }

    @Override
    public String getPack() {
        return null; // Packs do not belong to packs
    }

    @Override
    public void setPack(String pack) {
        logger.warn("setPack called on Pack ID={}, but packs cannot belong to other packs.", id);
    }
}