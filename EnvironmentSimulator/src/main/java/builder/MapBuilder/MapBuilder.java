package builder.MapBuilder;

import java.util.ArrayList;
import java.util.List;

public class MapBuilder {

    private int width;
    private int height;
    private List<Integer[]> waterPositions = new ArrayList<>();
    private List<Integer[]> grassPositions = new ArrayList<>();
    private List<Integer[]> obstaclesPositions = new ArrayList<>();

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

    public MapBuilder addWater(List<Integer[]> waterPositions) {
        this.waterPositions = waterPositions;
        return this;
    }

    public MapBuilder addGrass(List<Integer[]> grassPositions) {
        this.grassPositions = grassPositions;
        return this;
    }

    public MapBuilder addObstacles(List<Integer[]> obstaclesPositions) {
        this.obstaclesPositions = obstaclesPositions;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Integer[]> getWaterPositions() {
        return waterPositions;
    }

    public List<Integer[]> getGrassPositions() {
        return grassPositions;
    }

    public List<Integer[]> getObstaclesPositions() {
        return obstaclesPositions;
    }
}
