package builder.MapBuilder;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnvironmentMap {

    private static final Logger logger = LogManager.getLogger(EnvironmentMap.class);

    private final int height;
    private final int width;

    public List<Position> waterPositions = new ArrayList<>();
    public List<Position> grassPositions = new ArrayList<>();
    public List<Position> obstaclesPositions = new ArrayList<>();

    public EnvironmentMap(MapBuilder builder) {
        this.height = builder.getHeight();
        this.width = builder.getWidth();
        this.waterPositions = builder.getWaterPositions();
        this.grassPositions = builder.getGrassPositions();
        this.obstaclesPositions = builder.getObstaclesPositions();

        logger.debug("EnvironmentMap created: {}x{} | water={} | grass={} | obstacles={}",
                width,
                height,
                waterPositions.size(),
                grassPositions.size(),
                obstaclesPositions.size()
        );
    }
}