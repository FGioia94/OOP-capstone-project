package memento.GameSnapshot;

import builder.MapBuilder.MapBuilder;
import builder.MapBuilder.Position;

import java.util.List;

public record MapState(int width,
                       int height,
                       List<Position> grassPositions,
                       List<Position> waterPositions,
                       List<Position> obstaclesPositions) {

    public MapState(MapBuilder builder) {
        this(builder.getWidth(),
                builder.getHeight(),
                builder.getGrassPositions(),
                builder.getWaterPositions(),
                builder.getObstaclesPositions());
    }
}
