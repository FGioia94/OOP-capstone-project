package builder.MapBuilder;

import factoryMethod.AnimalFactory.Animal;
import memento.GameSnapshot.MapState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapBuilder {

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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Position> getWaterPositions() {
        return waterPositions;
    }

    public List<Position> getGrassPositions() {
        return grassPositions;
    }

    public List<Position> getObstaclesPositions() {
        return obstaclesPositions;
    }

    public List<Position> getAllValidPositions() {
        List<Position> result = new ArrayList<>();

        MapIterator it = new MapIterator(this.getWidth(), this.getHeight(), this.getObstaclesPositions());
        while (it.hasNext()) {
            result.add(it.next());
        }

        return result;
    }

    public void spawnElements(int amount, List<Position> elementPositions) {
        Random random = new java.util.Random();

        for (int i = 0; i <= amount; i++) {
            List<Position> validPositions = this.getAllValidPositions();
            int random_position = random.nextInt(validPositions.size());
            elementPositions.add(validPositions.get(random_position));
        }

    }

    public void moveAnimal(Animal animal) {
        Random random = new java.util.Random();
        List<Position> validPositions = this.getAllValidPositions();
        List<Position> movablePositions = new ArrayList<>();
        for (Position pos : validPositions) {
            if ((pos.x() + pos.y()) - (animal.getPosition().x() + animal.getPosition().y()) <= animal.getRange()) {
                movablePositions.add(pos);

            }
        }
        int random_index = random.nextInt(validPositions.size());
        Position selectedPos = movablePositions.get(random_index);
        animal.setPosition(selectedPos);
    }

    public MapState toState() {
        return new MapState(this);
    }

    public static MapBuilder fromState(MapState state) {
        MapBuilder builder = new MapBuilder();
        builder.setWidth(state.width());
        builder.setHeight(state.height());
        builder.setWaterPositions(state.waterPositions());
        builder.setGrassPositions(state.grassPositions());
        builder.setObstaclesPositions(state.obstaclesPositions());
        return builder;
    }
}
