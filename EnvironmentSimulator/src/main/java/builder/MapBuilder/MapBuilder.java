package builder.MapBuilder;

import factoryMethod.AnimalFactory.Animal;
import memento.GameSnapshot.MapState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapBuilder implements Serializable {

    private int width;
    private int height;
    private List<Position> waterPositions = new ArrayList<>();
    private List<Position> grassPositions = new ArrayList<>();
    private List<Position> obstaclesPositions = new ArrayList<>();

    public EnvironmentMap build() throws InvalidMapConfigurationException {
        validate();
        return new EnvironmentMap(this);
    }

    private void validate() throws InvalidMapConfigurationException {
        boolean isWidthValid = width > 0 && width < 1000;
        boolean isHeightValid = height > 0 && height < 1000;
        boolean isWaterValid = !waterPositions.isEmpty();
        boolean isGrassValid = !grassPositions.isEmpty();
        boolean isObstaclesValid = !obstaclesPositions.isEmpty();

        if (!(isWidthValid && isHeightValid && isWaterValid && isGrassValid && isObstaclesValid)) {
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
        this.width = width;
        return this;
    }

    public MapBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    public MapBuilder setWaterPositions(List<Position> waterPositions) {
        this.waterPositions = waterPositions;
        return this;
    }

    public MapBuilder setGrassPositions(List<Position> grassPositions) {
        this.grassPositions = grassPositions;
        return this;
    }

    public MapBuilder setObstaclesPositions(List<Position> obstaclesPositions) {
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

        return result;
    }

    public List<Position> spawnElements(int amount, List<Position> elementPositions) {
        System.out.println("Spawning " + amount + " elements.");
        Random random = new Random();
        List<Position> newPositions = new ArrayList<>(elementPositions);

        for (int i = 0; i < amount; i++) {
            List<Position> valid = getAllValidPositions();

            if (valid.isEmpty()) {
                System.out.println("No valid positions available to spawn new elements.");
                break;
            }

            int index = random.nextInt(valid.size());
            newPositions.add(valid.get(index));
        }

        return newPositions;
    }

    public Position getRandomValidPosition() {
        List<Position> valid = getAllValidPositions();
        if (valid.isEmpty()) return null;

        Random random = new Random();
        return valid.get(random.nextInt(valid.size()));
    }

    public void moveAnimal(Animal animal) {
        List<Position> valid = getAllValidPositions();
        if (valid.isEmpty()) return;

        List<Position> movable = new ArrayList<>();

        for (Position pos : valid) {
            int dx = Math.abs(pos.x() - animal.getPosition().x());
            int dy = Math.abs(pos.y() - animal.getPosition().y());
            if (dx + dy <= animal.getRange()) {
                movable.add(pos);
            }
        }

        if (movable.isEmpty()) return;

        Random random = new Random();
        Position selected = movable.get(random.nextInt(movable.size()));
        animal.setPosition(selected);
    }

    public MapState toState() {
        return new MapState(this);
    }

    public void fromState(MapState state) {
        System.out.println("building from state");

        this.width = state.width();
        this.height = state.height();
        this.waterPositions = state.waterPositions();
        this.grassPositions = state.grassPositions();
        this.obstaclesPositions = state.obstaclesPositions();
    }

    public void clear() {
        waterPositions.clear();
        grassPositions.clear();
    }

    public void clearAll() {
        clear();
        obstaclesPositions.clear();
    }
}