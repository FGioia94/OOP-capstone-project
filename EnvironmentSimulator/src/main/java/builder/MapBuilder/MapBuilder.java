package builder.MapBuilder;

import factoryMethod.AnimalFactory.AnimalComponent;
import memento.GameSnapshot.MapState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Builder responsible for constructing an {@link EnvironmentMap} instance.
 * <p>
 * This class follows the builder pattern, allowing callers to configure map
 * dimensions and terrain distributions before generating a validated and
 * immutable {@code EnvironmentMap}. It also provides several utility methods
 * for querying valid positions, spawning elements, moving animals, and
 * serializing/deserializing map state.
 * </p>
 *
 * <p>
 * The builder enforces structural constraints through {@link #validate()},
 * throwing an {@link InvalidMapConfigurationException} when the configuration
 * is incomplete or inconsistent. Logging is used extensively to support
 * debugging and traceability.
 * </p>
 */
public class MapBuilder implements Serializable {

    private static final Logger logger = LogManager.getLogger(MapBuilder.class);

    private int width;

    /** Height of the map grid. */
    private int height;

    /** Positions classified as water tiles. */
    private List<Position> waterPositions = new ArrayList<>();

    /** Positions classified as grass tiles. */
    private List<Position> grassPositions = new ArrayList<>();

    /** Positions classified as obstacle tiles. */
    private List<Position> obstaclesPositions = new ArrayList<>();

    /**
     * Builds and returns a validated {@link EnvironmentMap}.
     *
     * @return a fully constructed environment map. RIGHT NOW, THE BUILDER IS WHAT I
     *         AM USING THE MOST,
     *         BUT THIS ARCHITECTURE ALLOWS FOR SCALABILITY IN CASE I WANT TO
     *         INTRODUCE A GUI LATER ON.
     * @throws InvalidMapConfigurationException if the configuration is invalid
     */
    public EnvironmentMap build() throws InvalidMapConfigurationException {
        logger.debug("Building EnvironmentMap with width={}, height={}", width, height);
        validate();
        return new EnvironmentMap(this);
    }

    /**
     * Validates the current builder configuration.
     * <p>
     * Ensures that width, height, and terrain lists satisfy basic constraints.
     * Throws an exception if any component is invalid.
     * </p>
     *
     * @throws InvalidMapConfigurationException if validation fails
     */
    private void validate() throws InvalidMapConfigurationException {
        boolean isWidthValid = width > 0 && width < 1000;
        boolean isHeightValid = height > 0 && height < 1000;
        boolean isWaterValid = !waterPositions.isEmpty();
        boolean isGrassValid = !grassPositions.isEmpty();
        boolean isObstaclesValid = !obstaclesPositions.isEmpty();

        logger.debug(
                "Validating MapBuilder: widthValid={}, heightValid={}, waterValid={}, grassValid={}, obstaclesValid={}",
                isWidthValid, isHeightValid, isWaterValid, isGrassValid, isObstaclesValid);

        if (!(isWidthValid && isHeightValid && isWaterValid && isGrassValid && isObstaclesValid)) {
            logger.error("MapBuilder validation failed");
            throw new InvalidMapConfigurationException(
                    isWidthValid, width,
                    isHeightValid, height,
                    isWaterValid,
                    isGrassValid,
                    isObstaclesValid);
        }
    }

    /**
     * Sets the map width.
     *
     * @param width the desired width
     * @return this builder instance
     */
    public MapBuilder setWidth(int width) {
        logger.debug("Setting width={}", width);
        this.width = width;
        return this;
    }

    /**
     * Sets the map height.
     *
     * @param height the desired height
     * @return this builder instance
     */
    public MapBuilder setHeight(int height) {
        logger.debug("Setting height={}", height);
        this.height = height;
        return this;
    }

    /**
     * Sets the list of water positions.
     *
     * @param waterPositions list of water tiles
     * @return this builder instance
     */
    public MapBuilder setWaterPositions(List<Position> waterPositions) {
        logger.debug("Setting {} water positions", waterPositions.size());
        this.waterPositions = waterPositions;
        return this;
    }

    /**
     * Sets the list of grass positions.
     *
     * @param grassPositions list of grass tiles
     * @return this builder instance
     */
    public MapBuilder setGrassPositions(List<Position> grassPositions) {
        logger.debug("Setting {} grass positions", grassPositions.size());
        this.grassPositions = grassPositions;
        return this;
    }

    /**
     * Sets the list of obstacle positions.
     *
     * @param obstaclesPositions list of obstacle tiles
     * @return this builder instance
     */
    public MapBuilder setObstaclesPositions(List<Position> obstaclesPositions) {
        logger.debug("Setting {} obstacle positions", obstaclesPositions.size());
        this.obstaclesPositions = obstaclesPositions;
        return this;
    }

    /** @return the configured width */
    public int getWidth() {
        return width;
    }

    /** @return the configured height */
    public int getHeight() {
        return height;
    }

    /** @return list of water positions */
    public List<Position> getWaterPositions() {
        return waterPositions;
    }

    /** @return list of grass positions */
    public List<Position> getGrassPositions() {
        return grassPositions;
    }

    /** @return list of obstacle positions */
    public List<Position> getObstaclesPositions() {
        return obstaclesPositions;
    }

    /**
     * Computes and returns all valid (non‑occupied) positions on the map.
     *
     * @return list of available positions
     */
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

    /**
     * Spawns a number of new elements at random valid positions.
     *
     * @param amount           number of elements to spawn
     * @param elementPositions existing element positions
     * @return updated list including newly spawned elements
     */
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

    /**
     * Returns a random valid (non‑occupied) position.
     *
     * @return a random valid position, or {@code null} if none exist
     */
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

    /**
     * Moves the given animal to a random valid position within its movement range.
     *
     * @param animalComponent the animal to move
     */
    public void moveAnimal(AnimalComponent animalComponent) {
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

    /**
     * Serializes this builder into a {@link MapState} snapshot.
     *
     * @return a new map state representing the current configuration
     */
    public MapState toState() {
        logger.debug("Serializing MapBuilder to MapState");
        return new MapState(this);
    }

    /**
     * Restores the builder's configuration from a {@link MapState}.
     *
     * @param state the state to restore from
     */
    public void fromState(MapState state) {
        logger.info("Restoring MapBuilder from MapState");

        this.width = state.width();
        this.height = state.height();
        this.waterPositions = state.waterPositions();
        this.grassPositions = state.grassPositions();
        this.obstaclesPositions = state.obstaclesPositions();
    }

    /**
     * Clears water and grass positions.
     */
    public void clear() {
        logger.debug("Clearing water and grass positions");
        waterPositions.clear();
        grassPositions.clear();
    }

    /**
     * Clears all terrain positions, including obstacles.
     */
    public void clearAll() {
        logger.debug("Clearing all map positions");
        clear();
        obstaclesPositions.clear();
    }
}