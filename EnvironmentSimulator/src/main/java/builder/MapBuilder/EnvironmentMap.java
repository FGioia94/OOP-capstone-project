package builder.MapBuilder;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentMap {
    /* I used the builder pattern to create the EnvironmentMap.
    Right now, the EnvironmentMap is not crucial for the game logic, but it can be extended to hold the actual UI data.
    * */
    private final int height;
    private final int width;
    public List<Position> waterPositions = new ArrayList<Position>();
    public List<Position> grassPositions = new ArrayList<Position>();
    public List<Position> obstaclesPositions = new ArrayList<Position>();

    public EnvironmentMap(MapBuilder builder) {
        this.height = builder.getHeight();
        this.width = builder.getWidth();
        this.waterPositions = builder.getWaterPositions();
        this.grassPositions = builder.getGrassPositions();
        this.obstaclesPositions = builder.getObstaclesPositions();
    }


}
