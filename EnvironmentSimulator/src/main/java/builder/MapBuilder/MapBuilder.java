package builder.MapBuilder;

import factoryMethod.AnimalFactory.AnimalComponent;
import memento.GameSnapshot.MapState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapBuilder implements Serializable {

    private static final Logger logger = LogManager.getLogger(MapBuilder.class);

    private int width;
    private int height;
    private List<Position> waterPositions = new ArrayList<>();
    private List<Position> grassPositions = new ArrayList<>();
    private List<Position> obstaclesPositions = new ArrayList<>();

    public EnvironmentMap build() throws InvalidMapConfigurationException {
        logger.debug("Building EnvironmentMap with width={}, height={}", width, height);
        validate();
        return new EnvironmentMap(this);
    }

    private void validate() throws InvalidMapConfigurationException {
        boolean isWidthValid = width > 0 && width < 1000;
        boolean isHeightValid = height > 0 && height < 1000;
        boolean isWaterValid = !waterPositions.isEmpty();
        boolean isGrassValid = !grassPositions.isEmpty();
        boolean isObstaclesValid = !obstaclesPositions.isEmpty();

        logger.debug(
                "Validating MapBuilder: widthValid={}, heightValid={}, waterValid={}, grassValid={}, obstaclesValid={}",
                isWidthValid, isHeightValid, isWaterValid, isGrassValid, isObstaclesValid
        );

        if (!(isWidthValid && isHeightValid && isWaterValid && isGrassValid && isObstaclesValid)) {
            logger.error("MapBuilder validation failed");
            throw new InvalidMapConfigurationException(
                    isWidthValid, width,
                    isHeightValid, height,
                    isWaterValid,
                    isGrassValid,
                    isObstaclesValid
            );
        }
    }

    public MapBuilder setWidth(int width) {
        logger.debug("Setting width={}", width);
        this.width = width;
        return this;
    }

    public MapBuilder setHeight(int height) {
        logger.debug("Setting height={}", height);
        this.height = height;
        return this;
    }

    public MapBuilder setWaterPositions(List<Position> waterPositions) {
        logger.debug("Setting {} water positions", waterPositions.size());
        this.waterPositions = waterPositions;
        return this;
    }

    public MapBuilder setGrassPositions(List<Position> grassPositions) {
        logger.debug("Setting {} grass positions", grassPositions.size());
        this.grassPositions = grassPositions;
        return this;
    }

    public MapBuilder setObstaclesPositions(List<Position> obstaclesPositions) {
        logger.debug("Setting {} obstacle positions", obstaclesPositions.size());
        this.obstaclesPositions = obstaclesPositions;
        return this;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public List<Position> getWaterPositions() { return waterPositions; }
    public List<Position> getGrassPositions() { return grassPositions; }
    public List<Position> getObstaclesPositions() { return obstaclesPositions; }

    public List<Position> getAllValidPositions() {
        List<Position> result = new ArrayList<>();
        List<Position> occupied = new ArrayList<>();

        occupied.addAll(waterPositions);
        occupied.addAll(grassPositions);
        occupied.addAll(obstaclesPositions);

        MapIterator it = new MapIterator(width, height, occupied);

        while (it.hasNext()) {
            result.add(it.next());
        }

        logger.debug("Computed {} valid positions", result.size());
        return result;
    }

    public List<Position> spawnElements(int amount, List<Position> elementPositions) {
        logger.info("Spawning {} elements", amount);

        Random random = new Random();
        List<Position> newPositions = new ArrayList<>(elementPositions);

        for (int i = 0; i < amount; i++) {
            List<Position> valid = getAllValidPositions();

            if (valid.isEmpty()) {
                logger.warn("No valid positions available to spawn new elements");
                break;
            }

            int index = random.nextInt(valid.size());
            Position pos = valid.get(index);
            newPositions.add(pos);

            logger.debug("Spawned element at position {}", pos);
        }

        return newPositions;
    }

    public Position getRandomValidPosition() {
        List<Position> valid = getAllValidPositions();
        if (valid.isEmpty()) {
            logger.warn("No valid positions available for random selection");
            return null;
        }

        Random random = new Random();
        Position pos = valid.get(random.nextInt(valid.size()));

        logger.debug("Selected random valid position {}", pos);
        return pos;
    }

    public void moveAnimal(AnimalComponent animalComponent) {
        // Validate current position
        if (animalComponent.getPosition() != null) {
            annotations.PositionValidator.validateDefault(animalComponent.getPosition());
        }
        
        List<Position> valid = getAllValidPositions();
        if (valid.isEmpty()) {
            logger.warn("No valid positions available for movement");
            return;
        }

        List<Position> movable = new ArrayList<>();

        for (Position pos : valid) {
            int dx = Math.abs(pos.x() - animalComponent.getPosition().x());
            int dy = Math.abs(pos.y() - animalComponent.getPosition().y());
            if (dx + dy <= animalComponent.getRange()) {
                movable.add(pos);
            }
        }

        if (movable.isEmpty()) {
            logger.debug("Animal {} has no movable positions", animalComponent.getId());
            return;
        }

        Random random = new Random();
        Position selected = movable.get(random.nextInt(movable.size()));

        logger.debug("Animal {} moved from {} to {}", animalComponent.getId(), animalComponent.getPosition(), selected);
        animalComponent.setPosition(selected);
    }

    public MapState toState() {
        logger.debug("Serializing MapBuilder to MapState");
        return new MapState(this);
    }

    public void fromState(MapState state) {
        logger.info("Restoring MapBuilder from MapState");

        this.width = state.width();
        this.height = state.height();
        this.waterPositions = state.waterPositions();
        this.grassPositions = state.grassPositions();
        this.obstaclesPositions = state.obstaclesPositions();
    }

    public void clear() {
        logger.debug("Clearing water and grass positions");
        waterPositions.clear();
        grassPositions.clear();
    }

    public void clearAll() {
        logger.debug("Clearing all map positions");
        clear();
        obstaclesPositions.clear();
    }
}