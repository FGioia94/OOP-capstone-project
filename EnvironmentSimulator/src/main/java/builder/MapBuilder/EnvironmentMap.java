package builder.MapBuilder;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentMap {
    private final int height;
    private final int width;
    public List<Integer[]> waterPositions = new ArrayList<Integer[]>();
    public List<Integer[]> grassPositions = new ArrayList<Integer[]>();
    public List<Integer[]> obstaclesPositions = new ArrayList<Integer[]>();

    public EnvironmentMap(MapBuilder builder) {
        this.height = builder.getHeight();
        this.width = builder.getWidth();
        this.waterPositions = builder.getWaterPositions();
        this.grassPositions = builder.getGrassPositions();
        this.obstaclesPositions = builder.getObstaclesPositions();
    }


}
